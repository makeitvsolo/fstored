package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RemoveFileUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FileStorage<TestHandle> storage;

    @Mock
    private ComposeFileHandle<TestHandle> fileHandle;

    @InjectMocks
    private RemoveFileUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Test
    public void invoke_RemovesFile() {
        var payload = new FileDto("root", "/name.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);

        usecase.invoke(payload);

        verify(storage).remove(handle);
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
        when(storage.exists(handle))
                .thenReturn(false);

        assertThrows(FileDoesNotExistsException.class, () -> usecase.invoke(payload));
    }
}
