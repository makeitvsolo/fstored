package github.makeitvsolo.fstored.boot.config.internal.storage;

import github.makeitvsolo.fstored.storage.minio.configure.ConfigureMinioObjectStorage;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MinioStorageConfiguration {

    private final String minioUrl;
    private final String minioUser;
    private final String minioPassword;
    private final String minioRootBucket;

    public MinioStorageConfiguration(final Environment env) {
        this.minioUrl = env.getProperty("minio.url");
        this.minioUser = env.getProperty("minio.username");
        this.minioPassword = env.getProperty("minio.password");
        this.minioRootBucket = env.getProperty("minio.root-bucket");
    }

    @Bean
    public MinioObjectStorage minioStorage() {
        return ConfigureMinioObjectStorage.with()
                .minioUrl(minioUrl)
                .minioCredentials(minioUser, minioPassword)
                .rootBucket(minioRootBucket, ConfigureMinioObjectStorage.RootBucket.MakeIfNotExists)
                .configured();
    }
}
