package github.makeitvsolo.fstored.storage.integration.test.application.scenario.folder;

import github.makeitvsolo.fstored.storage.application.usecase.file.MakeFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.FolderSearchUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.MakeFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MetaDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.RootSearchDto;
import github.makeitvsolo.fstored.storage.domain.meta.FileMetaData;
import github.makeitvsolo.fstored.storage.domain.meta.FolderMetaData;
import github.makeitvsolo.fstored.storage.integration.test.FstoredStorageIntegrationTest;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MakeSearchScenarioTests extends FstoredStorageIntegrationTest {

    private MakeFileUsecase<?> makeFileUsecase = new MakeFileUsecase<>(
            fileStorage, composeMinioHandle
    );
    private MakeFolderUsecase<?> makeFolderUsecase = new MakeFolderUsecase<>(
            folderStorage, composeMinioHandle
    );
    private FolderSearchUsecase<?> searchUsecase = new FolderSearchUsecase<>(
            folderStorage, composeMinioHandle, intoMetaMapper
    );

    private FileDto file = new FileDto("root", "/name.ext");
    private FolderDto folder = new FolderDto("root", "/name/");

    private FileMetaData fileMeta = new FileMetaData(
            file.root(), file.path(), 0L, LocalDateTime.MIN
    );
    private FolderMetaData folderMeta = new FolderMetaData(
            folder.root(), folder.path()
    );

    private MinioHandle fileHandle = composeMinioHandle.composeAsFile(
                    file.root(), file.path()
            )
            .unwrap();

    private MinioHandle folderHandle = composeMinioHandle.composeAsFolder(
                    folder.root(), folder.path()
            )
            .unwrap();

    @Test
    @Order(1)
    public void atStart_FileAndFolder() {
        var rootHandle = composeMinioHandle.composeAsRoot("root").unwrap();
        folderStorage.make(rootHandle);

        makeFileUsecase.invoke(file);
        makeFolderUsecase.invoke(folder);

        assertTrue(fileStorage.exists(fileHandle));
        assertTrue(fileStorage.exists(folderHandle));
    }

    @Test
    @Order(2)
    public void fileAndDirectoryWillBeFound() {
        var expected = Stream.of(
                        new MetaDto(
                                fileMeta.path(),
                                fileMeta.resource(),
                                Optional.of(fileMeta.size()),
                                Optional.of(fileMeta.modifiedAt())
                        ),

                        new MetaDto(
                                folderMeta.path(),
                                folderMeta.resource(),
                                Optional.empty(),
                                Optional.empty()
                        )
                )
                .sorted(Comparator.comparing(MetaDto::path))
                .toList();

        var payload = new RootSearchDto("root", "name");

        var metas = searchUsecase.invoke(payload)
                .objects()
                .stream()
                .sorted(Comparator.comparing(MetaDto::path))
                .toList();

        assertEquals(expected.size(), metas.size());

        for (var idx = 0; idx < expected.size(); idx++) {
            var expectedMeta = expected.get(idx);
            var actualMeta = metas.get(idx);

            assertEquals(expectedMeta.path(), actualMeta.path());
            assertEquals(expectedMeta.resource(), actualMeta.resource());
        }
    }
}
