package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.RootAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MountRootFolderUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FolderStorage<TestHandle> storage;

    @Mock
    private ComposeFolderHandle<TestHandle> folderHandle;

    @InjectMocks
    private MountRootFolderUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Test
    public void invoke_MountsRootFolder() {
        var root = "root";

        when(folderHandle.composeAsRoot(root))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(false);

        usecase.invoke(root);

        verify(storage).make(handle);
    }

    @Test
    public void invoke_Throws_WhenRootHandleIsWrong() {
        var root = "root";

        when(folderHandle.composeAsRoot(root))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFolderHandleException.class, () -> usecase.invoke(root));
    }

    @Test
    public void invoke_Throws_WhenRootAlreadyExists() {
        var root = "root";

        when(folderHandle.composeAsRoot(root))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);

        assertThrows(RootAlreadyExistsException.class, () -> usecase.invoke(root));
    }
}
