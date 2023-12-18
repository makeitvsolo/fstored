package github.makeitvsolo.fstored.core.unique;

import java.util.UUID;

public final class UniqueString implements Unique<String> {

    @Override
    public String unique() {
        return UUID.randomUUID().toString();
    }
}
