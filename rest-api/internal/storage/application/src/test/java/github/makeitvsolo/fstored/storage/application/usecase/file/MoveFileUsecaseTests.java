package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.MoveFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoveFileUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FileStorage<TestHandle> storage;

    @Mock
    private ComposeFileHandle<TestHandle> fileHandle;

    @InjectMocks
    private MoveFileUsecase<TestHandle> usecase;

    @Mock
    private TestHandle source;

    @Mock
    private TestHandle destination;

    @Test
    public void invoke_MovesFile() {
        var payload = new MoveFileDto("root", "/name.ext", "/dst/rename.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.source()))
                .thenReturn(Result.ok(source));
        when(fileHandle.composeAsFile(payload.root(), payload.destination()))
                .thenReturn(Result.ok(destination));
        when(storage.exists(source))
                .thenReturn(true);
        when(storage.exists(destination))
                .thenReturn(false);

        usecase.invoke(payload);

        verify(storage).move(source, destination);
    }

    @Test
    public void invoke_Throws_WhenSourceHandleIsWrong() {
        var payload = new MoveFileDto("root", "/name.ext", "/dst/rename.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.source()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFileHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenDestinationHandleIsWrong() {
        var payload = new MoveFileDto("root", "/name.ext", "/dst/rename.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.source()))
                .thenReturn(Result.ok(source));
        when(fileHandle.composeAsFile(payload.root(), payload.destination()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFileHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenSourceDoesNotExists() {
        var payload = new MoveFileDto("root", "/name.ext", "/dst/rename.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.source()))
                .thenReturn(Result.ok(source));
        when(fileHandle.composeAsFile(payload.root(), payload.destination()))
                .thenReturn(Result.ok(destination));
        when(storage.exists(source))
                .thenReturn(false);

        assertThrows(FileDoesNotExistsException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenDestinationAlreadyExists() {
        var payload = new MoveFileDto("root", "/name.ext", "/dst/rename.ext");

        when(fileHandle.composeAsFile(payload.root(), payload.source()))
                .thenReturn(Result.ok(source));
        when(fileHandle.composeAsFile(payload.root(), payload.destination()))
                .thenReturn(Result.ok(destination));
        when(storage.exists(source))
                .thenReturn(true);
        when(storage.exists(destination))
                .thenReturn(true);

        assertThrows(FileAlreadyExistsException.class, () -> usecase.invoke(payload));
    }
}
