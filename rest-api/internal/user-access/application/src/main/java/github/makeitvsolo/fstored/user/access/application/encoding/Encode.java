package github.makeitvsolo.fstored.user.access.application.encoding;

public interface Encode {

    String encode(String string);
    boolean matches(String raw, String encoded);
}
