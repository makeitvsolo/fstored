package github.makeitvsolo.fstored.boot.config.internal.storage.usecase;

import github.makeitvsolo.fstored.storage.application.mapping.IntoFileContentDtoMapper;
import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.usecase.file.FetchFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.MakeFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.MoveFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.RemoveFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.WriteFileUsecase;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUsecasesConfiguration {

    @Bean
    public FetchFileUsecase<MinioHandle> fetchFileUsecase(
            final FileStorage<MinioHandle> fileStorage,
            final ComposeFileHandle<MinioHandle> composeFileHandle
    ) {
        return new FetchFileUsecase<>(
                fileStorage,
                composeFileHandle,
                new IntoFileContentDtoMapper()
        );
    }

    @Bean
    public MakeFileUsecase<MinioHandle> makeFileUsecase(
            final FileStorage<MinioHandle> fileStorage,
            final ComposeFileHandle<MinioHandle> composeFileHandle
    ) {
        return new MakeFileUsecase<>(
                fileStorage,
                composeFileHandle
        );
    }

    @Bean
    public MoveFileUsecase<MinioHandle> moveFileUsecase(
            final FileStorage<MinioHandle> fileStorage,
            final ComposeFileHandle<MinioHandle> composeFileHandle
    ) {
        return new MoveFileUsecase<>(
                fileStorage,
                composeFileHandle
        );
    }

    @Bean
    public RemoveFileUsecase<MinioHandle> removeFileUsecase(
            final FileStorage<MinioHandle> fileStorage,
            final ComposeFileHandle<MinioHandle> composeFileHandle
    ) {
        return new RemoveFileUsecase<>(
                fileStorage,
                composeFileHandle
        );
    }

    @Bean
    public WriteFileUsecase<MinioHandle> writeFileUsecase(
            final FileStorage<MinioHandle> fileStorage,
            final ComposeFileHandle<MinioHandle> composeFileHandle
    ) {
        return new WriteFileUsecase<>(
                fileStorage,
                composeFileHandle
        );
    }
}
