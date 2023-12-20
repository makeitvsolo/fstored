package github.makeitvsolo.fstored.user.access.encoding.bcrypt.configure;

import at.favre.lib.crypto.bcrypt.BCrypt;
import github.makeitvsolo.fstored.user.access.encoding.bcrypt.BcryptEncode;
import github.makeitvsolo.fstored.user.access.encoding.bcrypt.exception.BcryptEncodeConfigurationException;

public final class ConfigureBcryptEncode {

    private Integer cost;
    private byte[] salt16;

    ConfigureBcryptEncode() {

    }

    public static ConfigureBcryptEncode with() {
        return new ConfigureBcryptEncode();
    }

    public ConfigureBcryptEncode cost(final int cost) {
        this.cost = cost;
        return this;
    }

    public ConfigureBcryptEncode salt16(final byte[] salt16) {
        this.salt16 = salt16;
        return this;
    }

    public BcryptEncode configured() {
        if (cost == null) {
            throw new BcryptEncodeConfigurationException("missing cost");
        }

        if (cost < BCrypt.MIN_COST || cost > BCrypt.MAX_COST) {
            throw new BcryptEncodeConfigurationException(
                    String.format("cost must be between %d and %d", BCrypt.MIN_COST, BCrypt.MAX_COST)
            );
        }

        if (salt16 == null) {
            throw new BcryptEncodeConfigurationException("missing salt");
        }

        if (salt16.length != BCrypt.SALT_LENGTH) {
            throw new BcryptEncodeConfigurationException(
                    String.format("salt must be %d length",  BCrypt.SALT_LENGTH)
            );
        }

        return new BcryptEncode(cost, salt16);
    }
}
