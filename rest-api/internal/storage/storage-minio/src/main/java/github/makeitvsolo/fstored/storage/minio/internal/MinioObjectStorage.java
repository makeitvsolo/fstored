package github.makeitvsolo.fstored.storage.minio.internal;

import github.makeitvsolo.fstored.storage.application.storage.source.BinarySource;
import github.makeitvsolo.fstored.storage.domain.meta.FileMetaData;
import github.makeitvsolo.fstored.storage.domain.meta.FolderMetaData;
import github.makeitvsolo.fstored.storage.domain.meta.MetaData;
import github.makeitvsolo.fstored.storage.minio.exception.MinioInternalException;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.Directive;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.SnowballObject;
import io.minio.StatObjectArgs;
import io.minio.UploadSnowballObjectsArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MinioObjectStorage {

    private final MinioClient client;
    private final String rootBucket;

    public MinioObjectStorage(final MinioClient client, final String rootBucket) {
        this.client = client;
        this.rootBucket = rootBucket;
    }

    public void makeRootBucket() {
        try {
            var exists = client.bucketExists(BucketExistsArgs.builder()
                    .bucket(rootBucket)
                    .build()
            );

            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder()
                        .bucket(rootBucket)
                        .build()
                );
            }
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public boolean exists(final MinioHandle handle) {
        try {
            var unusedResponse = client.statObject(StatObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(handle.objectName())
                    .build()
            );

            return true;
        } catch (ErrorResponseException ex) {
            return false;
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public void make(final MinioHandle handle) {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(handle.objectName())
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                    .build()
            );
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public void write(final MinioHandle handle, final BinarySource source) {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(handle.objectName())
                    .stream(source.stream(), source.size(), -1)
                    .build()
            );
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public void writeMultiple(final Map<MinioHandle, BinarySource> sources) {
        try {
            var snowball = new ArrayList<SnowballObject>(sources.size());

            sources.forEach((handle, source) -> {
                snowball.add(new SnowballObject(
                        handle.objectName(),
                        source.stream(),
                        source.size(),
                        null
                ));
            });

            client.uploadSnowballObjects(UploadSnowballObjectsArgs.builder()
                    .bucket(rootBucket)
                    .objects(snowball)
                    .build()
            );
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public void remove(final MinioHandle handle) {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(handle.objectName())
                    .build()
            );
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public void removeMultiple(final List<MinioHandle> objects) {
        try {
            var deleteObjects = objects.stream()
                    .map(handle -> new DeleteObject(handle.objectName()))
                    .toList();

            var results = client.removeObjects(RemoveObjectsArgs.builder()
                    .bucket(rootBucket)
                    .objects(deleteObjects)
                    .build()
            );

            for (var result : results) {
                var unusedError = result.get();
            }
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public void copy(final MinioHandle source, final MinioHandle destination) {
        try {
            client.copyObject(CopyObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(destination.objectName())
                    .metadataDirective(Directive.REPLACE)
                    .source(CopySource.builder()
                            .bucket(rootBucket)
                            .object(source.objectName())
                            .build()
                    )
                    .build()
            );
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public Optional<BinarySource> findSource(final MinioHandle handle) {
        try {
            var stat = client.statObject(StatObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(handle.objectName())
                    .build()
            );

            var stream = client.getObject(GetObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(handle.objectName())
                    .build()
            );

            return Optional.of(new BinarySource(
                    stream,
                    stat.size()
            ));
        } catch (ErrorResponseException ex) {
            return Optional.empty();
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public List<MinioHandle> findObjects(final String prefix, final boolean recursive) {
        try {
            var results = client.listObjects(ListObjectsArgs.builder()
                    .bucket(rootBucket)
                    .prefix(prefix)
                    .recursive(recursive)
                    .build()
            );

            var objects = new ArrayList<MinioHandle>();
            for (var result : results) {
                var item = result.get();
                objects.add(
                        new MinioHandle(item.objectName())
                );
            }

            return objects;
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }

    public List<MetaData> findChildren(
            final MinioHandle parent, final Optional<String> filter, final boolean recursive
    ) {
        try {
            var results = client.listObjects(ListObjectsArgs.builder()
                    .bucket(rootBucket)
                    .prefix(parent.objectName())
                    .recursive(recursive)
                    .build()
            );

            var metas = new ArrayList<MetaData>();
            var filterLowerCase = filter.map(String::toLowerCase);

            for (var result : results) {
                var item = result.get();

                if (item.objectName().equals(parent.objectName())) {
                    continue;
                }

                var handle = new MinioHandle(item.objectName());

                if (filterLowerCase.isPresent()) {
                    var handleInfixLowerCase = handle.infix().toLowerCase();

                    if (!handleInfixLowerCase.contains(filterLowerCase.get())) {
                        continue;
                    }
                }

                if (handle.isFolder()) {
                    metas.add(new FolderMetaData(
                            handle.root(), handle.path()
                    ));
                } else {
                    metas.add(new FileMetaData(
                            handle.root(),
                            handle.path(),
                            item.size(),
                            item.lastModified().toLocalDateTime()
                    ));
                }
            }

            return metas;
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }
}
