package github.makeitvsolo.fstored.storage.integration.test.application.scenario.file;

import github.makeitvsolo.fstored.storage.application.usecase.file.WriteFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileSourceDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.WriteMultipleFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.integration.test.FstoredStorageIntegrationTest;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriteMultipleOverwriteMultipleFileScenarioTests extends FstoredStorageIntegrationTest {

    private WriteFileUsecase<?> writeFileUsecase = new WriteFileUsecase<>(
            fileStorage, composeMinioHandle
    );

    private WriteMultipleFileDto writeFiles = new WriteMultipleFileDto(
            "root",
            "/",
            List.of(
                    new FileSourceDto(
                            "first.ext",
                            new ByteArrayInputStream("first".getBytes(StandardCharsets.UTF_8)),
                            "first".length()
                    ),
                    new FileSourceDto(
                            "inner/second.ext",
                            new ByteArrayInputStream("second".getBytes(StandardCharsets.UTF_8)),
                            "second".length()
                    )
            ),
            false
    );

    private WriteMultipleFileDto overwriteFiles = new WriteMultipleFileDto(
            "root",
            "/",
            List.of(
                    new FileSourceDto(
                            "first.ext",
                            new ByteArrayInputStream("overwritten first".getBytes(StandardCharsets.UTF_8)),
                            "overwritten first".length()
                    ),
                    new FileSourceDto(
                            "inner/second.ext",
                            new ByteArrayInputStream("overwritten second".getBytes(StandardCharsets.UTF_8)),
                            "overwritten second".length()
                    )
            ),
            true
    );

    private MinioHandle innerFolderHandle = composeMinioHandle.composeAsFolder("root", "/inner/")
            .unwrap();

    private MinioHandle firstFileHandle = composeMinioHandle.composeAsFile("root", "/first.ext")
            .unwrap();

    private MinioHandle secondFileHandle = composeMinioHandle.composeAsFile("root", "/inner/second.ext")
            .unwrap();

    @Test
    public void atStart_FilesDoesNotExists() {
        var rootHandle = composeMinioHandle.composeAsRoot("root").unwrap();
        folderStorage.make(rootHandle);

        assertTrue(folderStorage.exists(rootHandle));
        assertFalse(fileStorage.exists(firstFileHandle));
        assertFalse(fileStorage.exists(secondFileHandle));
    }

    @Nested
    public class WhenFileDoNotExists {

        @Test
        public void filesCanBeUploaded_WhenFileDoNotExists() {
            writeFileUsecase.invoke(writeFiles);

            assertTrue(fileStorage.exists(firstFileHandle));
            assertTrue(folderStorage.exists(innerFolderHandle));
            assertTrue(fileStorage.exists(secondFileHandle));
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        public class WhenFilesUploaded {

            @Test
            @Order(1)
            public void filesCannotBeUploadedAgain_WhenFilesUploaded() {
                assertThrows(FileAlreadyExistsException.class, () -> writeFileUsecase.invoke(writeFiles));
            }

            @Test
            @Order(2)
            public void filesCanBeOverwritten_WhenFilesUploaded() {
                writeFileUsecase.invoke(overwriteFiles);

                assertTrue(fileStorage.exists(firstFileHandle));
                assertTrue(folderStorage.exists(innerFolderHandle));
                assertTrue(fileStorage.exists(secondFileHandle));
            }
        }
    }
}
