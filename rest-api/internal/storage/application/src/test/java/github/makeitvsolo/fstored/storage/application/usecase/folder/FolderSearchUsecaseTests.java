package github.makeitvsolo.fstored.storage.application.usecase.folder;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FolderStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFolderHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderSearchDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MatchingObjectsDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MetaDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.RootSearchDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.RootDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;
import github.makeitvsolo.fstored.storage.domain.mapping.MetaMapper;
import github.makeitvsolo.fstored.storage.domain.meta.FileMetaData;
import github.makeitvsolo.fstored.storage.domain.meta.FolderMetaData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FolderSearchUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FolderStorage<TestHandle> storage;

    @Mock
    private ComposeFolderHandle<TestHandle> folderHandle;

    @Mock
    private MetaMapper<MetaDto> mapper;

    @InjectMocks
    private FolderSearchUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Test
    public void invoke_WithRootSearch_ReturnsMatchingObjects() {
        var firstFile = new FileMetaData("root", "/name.ext", 0L, LocalDateTime.MIN);
        var secondFile = new FileMetaData("root", "/path/name.ext", 0L, LocalDateTime.MIN);
        var folder = new FolderMetaData("root", "/some/name/");
        var matchingObjects = List.of(firstFile, secondFile, folder);

        var expectedFirstFile = new MetaDto(
                firstFile.path(),
                firstFile.resource(),
                Optional.of(firstFile.size()),
                Optional.of(firstFile.modifiedAt())
        );
        var expectedSecondFile = new MetaDto(
                secondFile.path(),
                secondFile.resource(),
                Optional.of(secondFile.size()),
                Optional.of(secondFile.modifiedAt())
        );
        var expectedFolder = new MetaDto(
                folder.path(),
                folder.resource(),
                Optional.empty(),
                Optional.empty()
        );
        var expected = new MatchingObjectsDto(List.of(expectedFirstFile, expectedSecondFile, expectedFolder));

        var payload = new RootSearchDto("root", "name");
        when(folderHandle.composeAsRoot(payload.root()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);
        when(storage.findRecursively(handle, payload.prefix()))
                .thenReturn(matchingObjects);
        when(firstFile.mapBy(mapper))
                .thenReturn(expectedFirstFile);
        when(secondFile.mapBy(mapper))
                .thenReturn(expectedSecondFile);
        when(folder.mapBy(mapper))
                .thenReturn(expectedFolder);

        assertEquals(expected, usecase.invoke(payload));
    }

    @Test
    public void invoke_WithRootSearch_Throws_WhenHandleIsWrong() {
        var payload = new RootSearchDto("root", "name");

        when(folderHandle.composeAsRoot(payload.root()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFolderHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_WithRootSearch_Throws_WhenRootDoesNotExists() {
        var payload = new RootSearchDto("root", "name");

        when(folderHandle.composeAsRoot(payload.root()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(false);

        assertThrows(RootDoesNotExistsException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_WithFolderSearch_ReturnsMatchingObjects() {
        var firstFile = new FileMetaData("root", "/path/name.ext", 0L, LocalDateTime.MIN);
        var secondFile = new FileMetaData("root", "/path/to/name.ext", 0L, LocalDateTime.MIN);
        var folder = new FolderMetaData("root", "/path/name/");
        var matchingObjects = List.of(firstFile, secondFile, folder);

        var expectedFirstFile = new MetaDto(
                firstFile.path(),
                firstFile.resource(),
                Optional.of(firstFile.size()),
                Optional.of(firstFile.modifiedAt())
        );
        var expectedSecondFile = new MetaDto(
                secondFile.path(),
                secondFile.resource(),
                Optional.of(secondFile.size()),
                Optional.of(secondFile.modifiedAt())
        );
        var expectedFolder = new MetaDto(
                folder.path(),
                folder.resource(),
                Optional.empty(),
                Optional.empty()
        );
        var expected = new MatchingObjectsDto(List.of(expectedFirstFile, expectedSecondFile, expectedFolder));

        var payload = new FolderSearchDto("root", "/path/", "name");
        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);
        when(storage.findRecursively(handle, payload.prefix()))
                .thenReturn(matchingObjects);
        when(firstFile.mapBy(mapper))
                .thenReturn(expectedFirstFile);
        when(secondFile.mapBy(mapper))
                .thenReturn(expectedSecondFile);
        when(folder.mapBy(mapper))
                .thenReturn(expectedFolder);

        assertEquals(expected, usecase.invoke(payload));
    }

    @Test
    public void invoke_WithFolderSearch_Throws_WhenHandleIsWrong() {
        var payload = new FolderSearchDto("root", "/path/", "name");

        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFolderHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_WithFolderSearch_Throws_WhenRootDoesNotExists() {
        var payload = new FolderSearchDto("root", "/path/", "name");

        when(folderHandle.composeAsFolder(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(false);

        assertThrows(FolderDoesNotExistsException.class, () -> usecase.invoke(payload));
    }
}
