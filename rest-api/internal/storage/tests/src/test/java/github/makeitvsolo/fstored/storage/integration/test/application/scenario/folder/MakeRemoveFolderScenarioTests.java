package github.makeitvsolo.fstored.storage.integration.test.application.scenario.folder;

import github.makeitvsolo.fstored.storage.application.usecase.file.MakeFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.MakeFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.MoveFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.RemoveFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MoveFolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderAlreadyExistsException;
import github.makeitvsolo.fstored.storage.integration.test.FstoredStorageIntegrationTest;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

public class MakeRemoveFolderScenarioTests extends FstoredStorageIntegrationTest {

    private MakeFolderUsecase<?> makeFolderUsecase = new MakeFolderUsecase<>(
            folderStorage, composeMinioHandle
    );
    private MakeFileUsecase<?> makeFileUsecase = new MakeFileUsecase<>(
            fileStorage, composeMinioHandle
    );
    private MoveFolderUsecase<?> moveFolderUsecase = new MoveFolderUsecase<>(
            folderStorage, composeMinioHandle
    );
    private RemoveFolderUsecase<?> removeFolderUsecase = new RemoveFolderUsecase<>(
            folderStorage, composeMinioHandle
    );

    private FolderDto folder = new FolderDto("root", "/name/");
    private FileDto file = new FileDto("root", "/name/file.ext");
    private FolderDto renamedFolder = new FolderDto("root", "/newname/");
    private FileDto movedFile = new FileDto("root", "/newname/file.ext");
    private MoveFolderDto renameFolder = new MoveFolderDto("root", "/name/", "/newname/");

    private MinioHandle folderHandle = composeMinioHandle.composeAsFolder(
                    folder.root(), folder.path()
            )
            .unwrap();

    private MinioHandle fileHandle = composeMinioHandle.composeAsFile(
                    file.root(), file.path()
            )
            .unwrap();

    private MinioHandle renamedFolderHandle = composeMinioHandle.composeAsFolder(
                    renamedFolder.root(), renamedFolder.path()
            )
            .unwrap();

    private MinioHandle movedFileHandle = composeMinioHandle.composeAsFile(
                    movedFile.root(), movedFile.path()
            )
            .unwrap();

    @Test
    public void atStart_FolderDoesNotExists() {
        var rootHandle = composeMinioHandle.composeAsRoot("root").unwrap();
        folderStorage.make(rootHandle);

        assertTrue(folderStorage.exists(rootHandle));
        assertFalse(folderStorage.exists(folderHandle));
    }

    @Nested
    public class WhenFolderDoesNotExists {

        @Test
        public void folderCanBeCreated_WhenFolderDoesNotExists() {
            makeFolderUsecase.invoke(folder);

            assertTrue(folderStorage.exists(folderHandle));
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        public class WhenFolderCreated {

            @Test
            @Order(1)
            public void folderCannotBeCreatedAgain_WhenFolderCreated() {
                assertThrows(FolderAlreadyExistsException.class, () -> makeFolderUsecase.invoke(folder));
            }

            @Test
            @Order(2)
            public void fileCanBeMakesInFolder_WhenFolderCreated() {
                makeFileUsecase.invoke(file);

                assertTrue(fileStorage.exists(fileHandle));
            }

            @Test
            @Order(3)
            public void folderCanBeRenamed_WhenFolderCreated() {
                moveFolderUsecase.invoke(renameFolder);

                assertFalse(folderStorage.exists(folderHandle));
                assertFalse(fileStorage.exists(fileHandle));
                assertTrue(folderStorage.exists(renamedFolderHandle));
                assertTrue(fileStorage.exists(movedFileHandle));
            }

            @Test
            @Order(4)
            public void folderCanBeRemoved_WhenFolderCreated() {
                removeFolderUsecase.invoke(renamedFolder);

                assertFalse(folderStorage.exists(renamedFolderHandle));
                assertFalse(fileStorage.exists(movedFileHandle));
            }
        }
    }
}
