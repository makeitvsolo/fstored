package github.makeitvsolo.fstored.boot.config.internal.storage.usecase;

import github.makeitvsolo.fstored.storage.application.mapping.IntoFolderContentDtoMapper;
import github.makeitvsolo.fstored.storage.application.mapping.IntoMetaDtoMapper;
import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.usecase.folder.*;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FolderUsecasesConfiguration {

    @Bean
    public FetchFolderUsecase<MinioHandle> fetchFolderUsecase(
            final FolderStorage<MinioHandle> folderStorage,
            final ComposeFolderHandle<MinioHandle> composeFolderHandle
    ) {
        return new FetchFolderUsecase<>(
                folderStorage,
                composeFolderHandle,
                new IntoFolderContentDtoMapper()
        );
    }

    @Bean
    public FolderSearchUsecase<MinioHandle> folderSearchUsecase(
            final FolderStorage<MinioHandle> folderStorage,
            final ComposeFolderHandle<MinioHandle> composeFolderHandle
    ) {
        return new FolderSearchUsecase<>(
                folderStorage,
                composeFolderHandle,
                new IntoMetaDtoMapper()
        );
    }

    @Bean
    public MakeFolderUsecase<MinioHandle> makeFolderUsecase(
            final FolderStorage<MinioHandle> folderStorage,
            final ComposeFolderHandle<MinioHandle> composeFolderHandle
    ) {
        return new MakeFolderUsecase<>(
                folderStorage,
                composeFolderHandle
        );
    }

    @Bean
    public MountRootFolderUsecase<MinioHandle> mountRootFolderUsecase(
            final FolderStorage<MinioHandle> folderStorage,
            final ComposeFolderHandle<MinioHandle> composeFolderHandle
    ) {
        return new MountRootFolderUsecase<>(
                folderStorage,
                composeFolderHandle
        );
    }

    @Bean
    public MoveFolderUsecase<MinioHandle> moveFolderUsecase(
            final FolderStorage<MinioHandle> folderStorage,
            final ComposeFolderHandle<MinioHandle> composeFolderHandle
    ) {
        return new MoveFolderUsecase<>(
                folderStorage,
                composeFolderHandle
        );
    }

    @Bean
    public RemoveFolderUsecase<MinioHandle> removeFolderUsecase(
            final FolderStorage<MinioHandle> folderStorage,
            final ComposeFolderHandle<MinioHandle> composeFolderHandle
    ) {
        return new RemoveFolderUsecase<>(
                folderStorage,
                composeFolderHandle
        );
    }
}
