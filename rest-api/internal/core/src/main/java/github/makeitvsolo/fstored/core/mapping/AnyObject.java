package github.makeitvsolo.fstored.core.mapping;

public interface AnyObject {

    <R> R mapBy(Mapper<? extends R> mapper);
}
