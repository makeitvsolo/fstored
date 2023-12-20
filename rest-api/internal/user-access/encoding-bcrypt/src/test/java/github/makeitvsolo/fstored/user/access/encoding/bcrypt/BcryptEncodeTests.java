package github.makeitvsolo.fstored.user.access.encoding.bcrypt;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class BcryptEncodeTests extends FstoredUserAccessEncodingBcryptUnitTest {

    private static final int defaultCost = 6;
    private static final byte[] defaultSalt = "supersecret_salt".getBytes(StandardCharsets.UTF_8);

    private BcryptEncode security = new BcryptEncode(defaultCost, defaultSalt);

    @Test
    public void encodedPasswordNotEqualsWithRaw() {
        var password = "passwd";

        assertNotEquals(password, security.encode(password));
    }

    @Test
    public void matches_ReturnsTrue_WhenBothRawPasswordsAreEquals() {
        var password = "passwd";
        var encoded = security.encode(password);

        assertTrue(security.matches(password, encoded));
    }

    @Test
    public void matches_ReturnsFalse_WhenRawPasswordsAreNotEquals() {
        var first = "first";
        var second = "second";

        var encodedFirst = security.encode(first);

        assertFalse(security.matches(second, encodedFirst));
    }
}
