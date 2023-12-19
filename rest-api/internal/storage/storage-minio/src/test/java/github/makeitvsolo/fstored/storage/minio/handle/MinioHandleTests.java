package github.makeitvsolo.fstored.storage.minio.handle;

import github.makeitvsolo.fstored.storage.minio.FstoredStorageMinioUnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class MinioHandleTests extends FstoredStorageMinioUnitTest {

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root/
            root/name.ext
            root/name/
            root/path/name.ext
            """)
    public void fromObjectName_IsOk(String objectName) {
        assertTrue(MinioHandle.fromObjectName(objectName).isOk());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            /
            root
            /root
            """)
    public void fromObjectName_IsErr(String objectName) {
        assertTrue(MinioHandle.fromObjectName(objectName).isErr());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root/
            root/name/
            root/name.ext/
            root/path/name/
            """)
    public void isFolder_ReturnsTrue(String objectName) {
        var handle = MinioHandle.fromObjectName(objectName).unwrap();

        assertTrue(handle.isFolder());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", textBlock = """
            root/name
            root/name.ext
            root/path/name.ext
            """)
    public void isFolder_ReturnsFalse(String objectName) {
        var handle = MinioHandle.fromObjectName(objectName).unwrap();

        assertFalse(handle.isFolder());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "->", textBlock = """
            root/               ->  root
            root/name.ext       ->  root
            root/path/          ->  root
            root/path/name.ext  ->  root
            """)
    public void root_ReturnsRootName(String objectName, String expectedRoot) {
        var handle = MinioHandle.fromObjectName(objectName).unwrap();

        assertEquals(expectedRoot, handle.root());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "->", textBlock = """
            root/               ->  /
            root/name.ext       ->  /name.ext
            root/path/          ->  /path/
            root/path/inner/    ->  /path/inner/
            root/path/name.ext  ->  /path/name.ext
            """)
    public void path_ReturnsPath(String objectName, String expectedPath) {
        var handle = MinioHandle.fromObjectName(objectName).unwrap();

        assertEquals(expectedPath, handle.path());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "->", textBlock = """
            root/               ->  root
            root/name.ext       ->  name.ext
            root/path/          ->  path
            root/path/inner/    ->  inner
            root/path/name.ext  ->  name.ext
            """)
    public void infix_ReturnsInfix(String objectName, String expectedPath) {
        var handle = MinioHandle.fromObjectName(objectName).unwrap();

        assertEquals(expectedPath, handle.infix());
    }
}
