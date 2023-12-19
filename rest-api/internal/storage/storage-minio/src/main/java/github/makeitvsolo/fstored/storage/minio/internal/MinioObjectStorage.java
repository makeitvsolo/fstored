package github.makeitvsolo.fstored.storage.minio.internal;

import github.makeitvsolo.fstored.storage.application.storage.source.BinarySource;
import github.makeitvsolo.fstored.storage.domain.File;
import github.makeitvsolo.fstored.storage.minio.exception.MinioInternalException;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.Directive;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SnowballObject;
import io.minio.StatObjectArgs;
import io.minio.UploadSnowballObjectsArgs;
import io.minio.errors.ErrorResponseException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
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

    public Optional<File> findFile(final MinioHandle handle) {
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

            return Optional.of(new File(
                    handle.root(),
                    handle.path(),
                    stat.size(),
                    stream
            ));
        } catch (ErrorResponseException ex) {
            return Optional.empty();
        } catch (Exception ex) {
            throw new MinioInternalException(ex);
        }
    }
}
