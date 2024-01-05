package github.makeitvsolo.fstored.storage.minio.handle;

import github.makeitvsolo.fstored.storage.minio.FstoredStorageMinioUnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ComposeMinioHandleTests extends FstoredStorageMinioUnitTest {

    private ComposeMinioHandle composeMinioHandle = new ComposeMinioHandle();

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root
            root-1
            root_2
            root.3
            """)
    public void composeAsRoot_IsOk(String root) {
        var handle = composeMinioHandle.composeAsRoot(root).unwrap();

        assertTrue(handle.isFolder());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root/
            /root
            /root/
            some/root
            another/root/
            """)
    public void composeAsRoot_IsErr(String root) {
        assertTrue(composeMinioHandle.composeAsRoot(root).isErr());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root,   /name/
            root,   /path/name/
            root,   /name.ext/
            root,   /path/name-2/
            root,   /some/path/name_3/
            """)
    public void composeAsFolder_IsOk(String root, String path) {
        var handle = composeMinioHandle.composeAsFolder(root, path).unwrap();

        assertTrue(handle.isFolder());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root,   //
            root,   /path
            root,   path/
            root,   /path/name
            root,   path/name/
            """)
    public void composeAsFolder_IsErr(String root, String path) {
        assertTrue(composeMinioHandle.composeAsFolder(root, path).isErr());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root,   /name
            root,   /path/name
            root,   /name.ext
            root,   /path/name-2
            root,   /some/path/name_3
            """)
    public void composeAsFile_IsOk(String root, String path) {
        var handle = composeMinioHandle.composeAsFile(root, path).unwrap();

        assertFalse(handle.isFolder());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root,   //
            root,   /name/
            root,   name
            root,   /path/name/
            root,   path/name/
            """)
    public void composeAsFile_IsErr(String root, String path) {
        assertTrue(composeMinioHandle.composeAsFile(root, path).isErr());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root,   /,              name
            root,   /path/,         inner/name
            root,   /,              inner/name.ext
            root,   /path/,         name-2
            root,   /some/path/,    inner/name_3
            """)
    public void composeAsFileRelative_IsOk(String root, String path, String relativeName) {
        var handle = composeMinioHandle.composeAsFileRelative(root, path, relativeName).unwrap();

        assertFalse(handle.isFolder());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root,   //,             name
            root,   /path,          name
            root,   some/path/,     name
            root,   /,              /name
            root,   /,              name/inner/
            root,   /,              /name/inner/
            """)
    public void composeAsFileRelative_IsErr(String root, String path, String relativeName) {
        assertTrue(composeMinioHandle.composeAsFileRelative(root, path, relativeName).isErr());
    }
}
