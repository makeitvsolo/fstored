package github.makeitvsolo.fstored.storage.integration.test.application.scenario.file;

import github.makeitvsolo.fstored.storage.application.usecase.file.FetchFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.RemoveFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.WriteFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.WriteFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.integration.test.FstoredStorageIntegrationTest;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class WriteRemoveFileScenarioTests extends FstoredStorageIntegrationTest {

    private WriteFileUsecase<?> writeFileUsecase = new WriteFileUsecase<>(fileStorage, composeMinioHandle);
    private RemoveFileUsecase<?> removeFileUsecase = new RemoveFileUsecase<>(fileStorage, composeMinioHandle);
    private FetchFileUsecase<?> fetchFileUsecase = new FetchFileUsecase<>(
            fileStorage, composeMinioHandle, intoFileContentMapper
    );

    private WriteFileDto writeFile = new WriteFileDto(
            "root",
            "/name.ext",
            "content".length(),
            new ByteArrayInputStream("content".getBytes(StandardCharsets.UTF_8)),
            false
    );

    private WriteFileDto overwriteFile = new WriteFileDto(
            "root",
            "/name.ext",
            "overwritten content".length(),
            new ByteArrayInputStream("overwritten content".getBytes(StandardCharsets.UTF_8)),
            true
    );

    private MinioHandle writtenFileHandle = composeMinioHandle.composeAsFile(
                    writeFile.root(), writeFile.path()
            )
            .unwrap();

    private MinioHandle overwrittenFileHandle = composeMinioHandle.composeAsFile(
                    overwriteFile.root(), overwriteFile.path()
            )
            .unwrap();

    @Test
    public void atStart_FileDoesNotExists() {
        var rootHandle = composeMinioHandle.composeAsRoot("root").unwrap();
        folderStorage.make(rootHandle);

        assertTrue(folderStorage.exists(rootHandle));
        assertFalse(fileStorage.exists(writtenFileHandle));
    }

    @Nested
    public class WhenFileDoesNotExists {

        @Test
        public void fileCanBeUploaded_WhenFileDoesNotExists() {
            writeFileUsecase.invoke(writeFile);

            assertTrue(fileStorage.exists(writtenFileHandle));
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        public class WhenFileUploaded {

            @Test
            @Order(1)
            public void fileCannotBeUploadedAgain_WhenFileUploaded() {
                assertThrows(FileAlreadyExistsException.class, () -> writeFileUsecase.invoke(writeFile));
            }

            @Test
            @Order(2)
            public void fileCanBeFound_WhenFileUploaded() throws IOException {
                var payload = new FileDto(
                        writeFile.root(), writeFile.path()
                );

                var file = fetchFileUsecase.invoke(payload);

                assertEquals(writeFile.path(), file.path());
                assertEquals(writeFile.size(), file.size());

                {
                    try (
                            var uploadContent = writeFile.stream();
                            var fileContent = file.stream()
                    ) {
                        assertArrayEquals(uploadContent.readAllBytes(), fileContent.readAllBytes());
                    }
                }
            }

            @Test
            @Order(3)
            public void fileCanBeOverwritten_WhenFileUploaded() {
                writeFileUsecase.invoke(overwriteFile);

                assertTrue(fileStorage.exists(overwrittenFileHandle));
            }

            @Test
            @Order(4)
            public void overwrittenFileCanBeFound_WhenFileUploaded() throws IOException {
                var payload = new FileDto(
                        overwriteFile.root(), overwriteFile.path()
                );

                var file = fetchFileUsecase.invoke(payload);

                assertEquals(overwriteFile.path(), file.path());
                assertEquals(overwriteFile.size(), file.size());

                {
                    try (
                            var overwrittenContent = overwriteFile.stream();
                            var fileContent = file.stream()
                    ) {
                        assertArrayEquals(overwrittenContent.readAllBytes(), fileContent.readAllBytes());
                    }
                }
            }

            @Test
            @Order(5)
            public void fileCanBeRemoved_WhenFileUploaded() {
                var payload = new FileDto(
                        overwriteFile.root(), overwriteFile.path()
                );

                removeFileUsecase.invoke(payload);

                assertFalse(fileStorage.exists(overwrittenFileHandle));
            }
        }
    }
}
