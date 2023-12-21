package github.makeitvsolo.fstored.boot.api.exception.handling;

import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Import({
        WebExceptionHandling.class
})
@RestControllerAdvice
public class FstoredApplicationExceptionHandling {
}
