package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderContentDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MetaDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.RootDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;
import github.makeitvsolo.fstored.storage.domain.Folder;
import github.makeitvsolo.fstored.storage.domain.mapping.FolderMapper;
import github.makeitvsolo.fstored.storage.domain.meta.FileMetaData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FetchFolderUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FolderStorage<TestHandle> storage;

    @Mock
    private ComposeFolderHandle<TestHandle> folderHandle;

    @Mock
    private FolderMapper<FolderContentDto> mapper;

    @InjectMocks
    private FetchFolderUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Test
    public void invoke_WithRoot_ReturnsFolderContent() {
        var innerFile = new FileMetaData("root", "/name.ext", 0L, LocalDateTime.MIN);
        var existingFolder = new Folder(
                "root", "/", List.of(innerFile)
        );

        var expected = new FolderContentDto(
                existingFolder.path(),
                List.of(new MetaDto(
                        innerFile.path(),
                        innerFile.resource(),
                        Optional.of(innerFile.size()),
                        Optional.of(innerFile.modifiedAt())
                ))
        );

        var root = "root";
        when(folderHandle.composeAsRoot(root))
                .thenReturn(Result.ok(handle));
        when(storage.find(handle))
                .thenReturn(Optional.of(existingFolder));
        when(existingFolder.mapBy(mapper))
                .thenReturn(expected);

        assertEquals(expected, usecase.invoke(root));
    }

    @Test
    public void invoke_WithRoot_Throws_WhenRootHandleIsWrong() {
        var root = "root";

        when(folderHandle.composeAsRoot(root))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFolderHandleException.class, () -> usecase.invoke(root));
    }

    @Test
    public void invoke_WithRoot_Throws_WhenRootDoesNotExists() {
        var root = "root";

        when(folderHandle.composeAsRoot(root))
                .thenReturn(Result.ok(handle));
        when(storage.find(handle))
                .thenReturn(Optional.empty());

        assertThrows(RootDoesNotExistsException.class, () -> usecase.invoke(root));
    }

    @Test
    public void invoke_WithFolder_ReturnsFolderContent() {
        var innerFile = new FileMetaData("root", "/path/name.ext", 0L, LocalDateTime.MIN);
        var existingFolder = new Folder(
                "root", "/path/", List.of(innerFile)
        );

        var expected = new FolderContentDto(
                existingFolder.path(),
                List.of(new MetaDto(
                        innerFile.path(),
                        innerFile.resource(),
                        Optional.of(innerFile.size()),
                        Optional.of(innerFile.modifiedAt())
                ))
        );

        var payload = new FolderDto("root", "/path/");
        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.find(handle))
                .thenReturn(Optional.of(existingFolder));
        when(existingFolder.mapBy(mapper))
                .thenReturn(expected);

        assertEquals(expected, usecase.invoke(payload));
    }

    @Test
    public void invoke_WithFolder_Throws_WhenFolderHandleIsWrong() {
        var payload = new FolderDto("root", "/path/");

        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFolderHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_WithFolder_Throws_WhenFolderDoesNotExists() {
        var payload = new FolderDto("root", "/path/");

        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.find(handle))
                .thenReturn(Optional.empty());

        assertThrows(FolderDoesNotExistsException.class, () -> usecase.invoke(payload));
    }
}
