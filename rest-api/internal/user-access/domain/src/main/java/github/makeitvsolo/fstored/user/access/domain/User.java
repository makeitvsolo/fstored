package github.makeitvsolo.fstored.user.access.domain;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.user.access.domain.mapping.UserMapper;

public final class User {

    private final String id;
    private final String name;
    private final String password;

    public User(final String id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static User create(final Unique<String> id, final String name, final String password) {
        return new User(id.unique(), name, password);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String password() {
        return password;
    }

    public <R, M extends UserMapper<R>> R mapBy(final M mapper) {
        return mapper.into(id, name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof User other)) {
            return false;
        }

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
