package github.makeitvsolo.fstored.storage.application.usecase.file;

import github.makeitvsolo.fstored.core.error.handling.result.Result;
import github.makeitvsolo.fstored.storage.application.FstoredStorageApplicationUnitTest;
import github.makeitvsolo.fstored.storage.application.storage.FileStorage;
import github.makeitvsolo.fstored.storage.application.storage.handle.ComposeFileHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.TestHandle;
import github.makeitvsolo.fstored.storage.application.storage.handle.WrongTestHandleError;
import github.makeitvsolo.fstored.storage.application.storage.source.BinarySource;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileSourceDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.WriteFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.WriteMultipleFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WriteFileUsecaseTests extends FstoredStorageApplicationUnitTest {

    @Mock
    private FileStorage<TestHandle> storage;

    @Mock
    private ComposeFileHandle<TestHandle> fileHandle;

    @InjectMocks
    private WriteFileUsecase<TestHandle> usecase;

    @Mock
    private TestHandle handle;

    @Mock
    private TestHandle firstHandle;

    @Mock
    private TestHandle secondHandle;

    @Captor
    private ArgumentCaptor<BinarySource> source;

    @Captor
    private ArgumentCaptor<Map<TestHandle, BinarySource>> sources;

    @Test
    public void invoke_WritesFile() {
        var overwrite = false;
        var payload = new WriteFileDto(
                "root",
                "/name.ext",
                0L,
                new ByteArrayInputStream(new byte[]{}),
                overwrite
        );

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(false);

        usecase.invoke(payload);

        verify(storage).write(eq(handle), source.capture());

        var capturedSource = source.getValue();
        assertEquals(payload.stream(), capturedSource.stream());
        assertEquals(payload.size(), capturedSource.size());
    }

    @Test
    public void invoke_Throws_WhenHandleIsWrong() {
        var overwrite = false;
        var payload = new WriteFileDto(
                "root",
                "/name.ext",
                0L,
                new ByteArrayInputStream(new byte[]{}),
                overwrite
        );

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.err(new WrongTestHandleError()));

        assertThrows(WrongFileHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_WithOverwrite_OverwritesFile_WhenFileAlreadyExists() {
        var overwrite = true;
        var payload = new WriteFileDto(
                "root",
                "/name.ext",
                0L,
                new ByteArrayInputStream(new byte[]{}),
                overwrite
        );

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);

        usecase.invoke(payload);

        verify(storage).write(eq(handle), source.capture());

        var capturedSource = source.getValue();
        assertEquals(payload.stream(), capturedSource.stream());
        assertEquals(payload.size(), capturedSource.size());
    }

    @Test
    public void invoke_WithoutOverwrite_Throws_WhenFileAlreadyExists() {
        var overwrite = false;
        var payload = new WriteFileDto(
                "root",
                "/name.ext",
                0L,
                new ByteArrayInputStream(new byte[]{}),
                overwrite
        );

        when(fileHandle.composeAsFile(payload.root(), payload.path()))
                .thenReturn(Result.ok(handle));
        when(storage.exists(handle))
                .thenReturn(true);

        assertThrows(FileAlreadyExistsException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_WithMultiple_WritesMultiple() {
        var firstFile = new FileSourceDto(
                "name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );
        var secondFile = new FileSourceDto(
                "inner/name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );

        var overwrite = false;
        var payload = new WriteMultipleFileDto("root", "/", List.of(firstFile, secondFile), overwrite);

        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), firstFile.relativeName()))
                .thenReturn(Result.ok(firstHandle));
        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), secondFile.relativeName()))
                .thenReturn(Result.ok(secondHandle));
        when(storage.exists(firstHandle))
                .thenReturn(false);
        when(storage.exists(secondHandle))
                .thenReturn(false);

        usecase.invoke(payload);

        verify(storage).writeMultiple(sources.capture());

        var capturedSources = sources.getValue();
        assertEquals(firstFile.stream(), capturedSources.get(firstHandle).stream());
        assertEquals(secondFile.stream(), capturedSources.get(secondHandle).stream());
        assertEquals(firstFile.size(), capturedSources.get(firstHandle).size());
        assertEquals(secondFile.size(), capturedSources.get(secondHandle).size());
    }

    @Test
    public void invoke_WithMultiple_Throws_WhenAnyHandleIsWrong() {
        var firstFile = new FileSourceDto(
                "name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );
        var secondFile = new FileSourceDto(
                "inner/name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );

        var overwrite = false;
        var payload = new WriteMultipleFileDto("root", "/", List.of(firstFile, secondFile), overwrite);

        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), firstFile.relativeName()))
                .thenReturn(Result.err(new WrongTestHandleError()));
        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), secondFile.relativeName()))
                .thenReturn(Result.ok(secondHandle));

        assertThrows(WrongFileHandleException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_WithMultiple_WithOverwrite_OverwritesExistingFile_WhenFileAlreadyExists() {
        var firstFile = new FileSourceDto(
                "name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );
        var secondFile = new FileSourceDto(
                "inner/name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );

        var overwrite = true;
        var payload = new WriteMultipleFileDto("root", "/", List.of(firstFile, secondFile), overwrite);

        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), firstFile.relativeName()))
                .thenReturn(Result.ok(firstHandle));
        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), secondFile.relativeName()))
                .thenReturn(Result.ok(secondHandle));
        when(storage.exists(firstHandle))
                .thenReturn(true);
        when(storage.exists(secondHandle))
                .thenReturn(false);

        usecase.invoke(payload);

        verify(storage).writeMultiple(sources.capture());

        var capturedSources = sources.getValue();
        assertEquals(firstFile.stream(), capturedSources.get(firstHandle).stream());
        assertEquals(secondFile.stream(), capturedSources.get(secondHandle).stream());
        assertEquals(firstFile.size(), capturedSources.get(firstHandle).size());
        assertEquals(secondFile.size(), capturedSources.get(secondHandle).size());
    }

    @Test
    public void invoke_WithMultiple_WithoutOverwrite_Throws_WhenAnyFileAlreadyExists() {
        var firstFile = new FileSourceDto(
                "name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );
        var secondFile = new FileSourceDto(
                "inner/name.ext", new ByteArrayInputStream(new byte[]{}), 0L
        );

        var overwrite = false;
        var payload = new WriteMultipleFileDto("root", "/", List.of(firstFile, secondFile), overwrite);

        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), firstFile.relativeName()))
                .thenReturn(Result.ok(firstHandle));
        when(fileHandle.composeAsFileRelative(payload.root(), payload.path(), secondFile.relativeName()))
                .thenReturn(Result.ok(secondHandle));
        when(storage.exists(firstHandle))
                .thenReturn(true);
        when(storage.exists(secondHandle))
                .thenReturn(false);

        assertThrows(FileAlreadyExistsException.class, () -> usecase.invoke(payload));
    }
}
