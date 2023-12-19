package github.makeitvsolo.fstored.storage.application.storage.source;

import java.io.InputStream;

public final class BinarySource {

    private final InputStream stream;
    private final long size;

    public BinarySource(final InputStream stream, final long size) {
        this.stream = stream;
        this.size = size;
    }

    public InputStream stream() {
        return stream;
    }

    public long size() {
        return size;
    }
}
