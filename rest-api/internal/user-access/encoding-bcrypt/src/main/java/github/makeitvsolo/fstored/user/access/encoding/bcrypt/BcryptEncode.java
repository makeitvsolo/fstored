package github.makeitvsolo.fstored.user.access.encoding.bcrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import github.makeitvsolo.fstored.user.access.application.encoding.Encode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class BcryptEncode implements Encode {

    private final int cost;
    private final byte[] salt16;

    public BcryptEncode(final int cost, final byte[] salt16) {
        this.cost = cost;
        this.salt16 = salt16;
    }

    @Override
    public String encode(final String string) {
        return Arrays.toString(BCrypt.withDefaults()
                .hash(cost, salt16, string.getBytes(StandardCharsets.UTF_8))
        );
    }

    @Override
    public boolean matches(final String raw, final String encoded) {
        return encode(raw).equals(encoded);
    }
}
