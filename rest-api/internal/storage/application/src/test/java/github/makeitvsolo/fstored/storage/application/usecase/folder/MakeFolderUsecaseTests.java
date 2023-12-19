package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MakeFolderUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FolderStorage<TestHandle> storage;

    @Mock
    private ComposeFolderHandle<TestHandle> folderHandle;

    @InjectMocks
    private MakeFolderUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Test
    public void invoke_MakesFolder() {
        var payload = new FolderDto("root", "/name/");

        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(false);

        usecase.invoke(payload);

        verify(storage).make(handle);
    }

    @Test
    public void invoke_Throws_WhenFolderHandleIsWrong() {
        var payload = new FolderDto("root", "/name/");

        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFolderHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenFolderAlreadyExists() {
        var payload = new FolderDto("root", "/name/");

        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);

        assertThrows(FolderAlreadyExistsException.class, () -> usecase.invoke(payload));
    }
}
