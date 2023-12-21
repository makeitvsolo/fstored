package github.makeitvsolo.fstored.boot.config.internal.storage;

import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.minio.MinioFileStorage;
import github.makeitvsolo.fstored.storage.minio.MinioFolderStorage;
import github.makeitvsolo.fstored.storage.minio.handle.ComposeMinioHandle;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Bean
    public FileStorage<MinioHandle> fileStorage(final MinioObjectStorage minioStorage) {
        return new MinioFileStorage(minioStorage);
    }

    @Bean
    public FolderStorage<MinioHandle> folderStorage(final MinioObjectStorage minioStorage) {
        return new MinioFolderStorage(minioStorage);
    }

    @Bean
    public ComposeFileHandle<MinioHandle> composeFileHandle() {
        return new ComposeMinioHandle();
    }

    @Bean
    public ComposeFolderHandle<MinioHandle> composeFolderHandle() {
        return new ComposeMinioHandle();
    }
}
