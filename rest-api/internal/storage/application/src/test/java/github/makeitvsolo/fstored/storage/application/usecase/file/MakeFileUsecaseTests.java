package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MakeFileUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FileStorage<TestHandle> storage;

    @Mock
    private ComposeFileHandle<TestHandle> fileHandle;

    @InjectMocks
    private MakeFileUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Test
    public void invoke_MakesFile() {
        var payload = new FileDto("root", "/name.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(false);

        usecase.invoke(payload);

        verify(storage).make(handle);
    }

    @Test
    public void invoke_Throws_WhenHandleIsWrong() {
        var payload = new FileDto("root", "/name.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFileHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenFileAlreadyExists() {
        var payload = new FileDto("root", "/name.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);

        assertThrows(FileAlreadyExistsException.class, () -> usecase.invoke(payload));
    }
}
