package github.makeitvsolo.fstored.storage.minio.configure;

import github.makeitvsolo.fstored.storage.minio.exception.MinioConfigurationException;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;
import io.minio.MinioClient;

public final class ConfigureMinioObjectStorage {

    private String url;
    private String username;
    private String password;

    private String bucket;
    private RootBucket usage;

    public enum RootBucket {

        MakeIfNotExists,
        UseExisting,
    }

    ConfigureMinioObjectStorage() {

    }

    public static ConfigureMinioObjectStorage with() {
        return new ConfigureMinioObjectStorage();
    }

    public ConfigureMinioObjectStorage minioUrl(final String url) {
        this.url = url;
        return this;
    }

    public ConfigureMinioObjectStorage minioCredentials(final String username, final String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public ConfigureMinioObjectStorage rootBucket(final String bucket, final RootBucket usage) {
        this.bucket = bucket;
        this.usage = usage;
        return this;
    }

    public MinioObjectStorage configured() {
        if (url == null) {
            throw new MinioConfigurationException("missing minio url");
        }

        if (username == null || password == null) {
            throw new MinioConfigurationException("missing minio credentials");
        }

        var objectStorage = new MinioObjectStorage(
                MinioClient.builder()
                        .endpoint(url)
                        .credentials(username, password)
                        .build(),
                bucket
        );

        if (bucket != null) {
            switch (usage) {
                case MakeIfNotExists -> objectStorage.makeRootBucket();

                default -> {
                }
            }
        }

        return objectStorage;
    }
}
