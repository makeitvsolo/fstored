package github.makeitvsolo.fstored.storage.integration.test.application.scenario.file;

import github.makeitvsolo.fstored.storage.application.usecase.file.MakeFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.MoveFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.RemoveFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.MoveFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.integration.test.FstoredStorageIntegrationTest;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

public class MakeRemoveFileScenarioTests extends FstoredStorageIntegrationTest {

    private MakeFileUsecase<?> makeFileUsecase = new MakeFileUsecase<>(fileStorage, composeMinioHandle);
    private MoveFileUsecase<?> renameFileUsecase = new MoveFileUsecase<>(fileStorage, composeMinioHandle);
    private RemoveFileUsecase<?> removeFileUsecase = new RemoveFileUsecase<>(fileStorage, composeMinioHandle);

    private FileDto file = new FileDto("root", "/name.ext");
    private FileDto renamedFile = new FileDto("root", "/newname.ext");
    private MoveFileDto renameFile = new MoveFileDto(
            "root", "/name.ext", "/newname.ext"
    );

    private MinioHandle fileHandle = composeMinioHandle.composeAsFile(
                    file.root(), file.path()
            )
            .unwrap();

    private MinioHandle renamedFileHandle = composeMinioHandle.composeAsFile(
                    renamedFile.root(), renamedFile.path()
            )
            .unwrap();

    @Test
    public void atStart_FileDoesNotExists() {
        var rootHandle = composeMinioHandle.composeAsRoot("root").unwrap();
        folderStorage.make(rootHandle);

        assertTrue(folderStorage.exists(rootHandle));
        assertFalse(fileStorage.exists(fileHandle));
    }

    @Nested
    public class WhenFileDoesNotExists {

        @Test
        public void fileCanBeCreated_WhenFileDoesNotExists() {
            makeFileUsecase.invoke(file);

            assertTrue(fileStorage.exists(fileHandle));
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        public class WhenFileCreated {

            @Test
            @Order(1)
            public void fileCannotBeCreatedAgain_WhenFileCreated() {
                assertThrows(FileAlreadyExistsException.class, () -> makeFileUsecase.invoke(file));
            }

            @Test
            @Order(2)
            public void fileCanBeRenamed_WhenFileCreated() {
                renameFileUsecase.invoke(renameFile);

                assertTrue(fileStorage.exists(renamedFileHandle));
                assertFalse(fileStorage.exists(fileHandle));
            }

            @Test
            @Order(3)
            public void fileCanBeRemoved_WhenFileCreated() {
                removeFileUsecase.invoke(renamedFile);

                assertFalse(fileStorage.exists(renamedFileHandle));
            }
        }
    }
}
