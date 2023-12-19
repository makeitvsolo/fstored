package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileContentDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;
import github.makeitvsolo.fstored.storage.domain.File;
import github.makeitvsolo.fstored.storage.domain.mapping.FileMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FetchFileUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FileStorage<TestHandle> storage;

    @Mock
    private ComposeFileHandle<TestHandle> fileHandle;

    @Mock
    private FileMapper<FileContentDto> mapper;

    @InjectMocks
    private FetchFileUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Test
    public void invoke_ReturnsFileContent() {
        var existingFile = new File(
                "root", "/name.ext", 0L, new ByteArrayInputStream(new byte[]{})
        );

        var expected = new FileContentDto(
                existingFile.path(), existingFile.stream(), existingFile.size()
        );

        var payload = new FileDto("root", "/name.ext");
        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.find(handle))
                .thenReturn(Optional.of(existingFile));
        when(existingFile.mapBy(mapper))
                .thenReturn(expected);

        assertEquals(expected, usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenFileHandleIsWrong() {
        var payload = new FileDto("root", "/name.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFileHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenFileDoesNotExists() {
        var payload = new FileDto("root", "/name.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.find(handle))
                .thenReturn(Optional.empty());

        assertThrows(FileDoesNotExistsException.class, () -> usecase.invoke(payload));
    }
}
