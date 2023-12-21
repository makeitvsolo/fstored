package github.makeitvsolo.fstored.boot.config.spring.session.authentication;

import github.makeitvsolo.fstored.boot.config.spring.session.cookie.SessionCookie;
import github.makeitvsolo.fstored.boot.config.spring.session.exception.MissingSessionException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.AuthenticateUserUsecase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

public class AuthenticatedArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticateUserUsecase<?> authenticateUserUsecase;

    public AuthenticatedArgumentResolver(final AuthenticateUserUsecase<?> authenticateUserUsecase) {
        this.authenticateUserUsecase = authenticateUserUsecase;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterAnnotation(Authenticated.class) != null;
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) throws Exception {
        var request = (HttpServletRequest) webRequest.getNativeRequest();

        var cookies = request.getCookies();
        if (cookies == null) {
            throw new MissingSessionException();
        }

        var token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(SessionCookie.TOKEN_ID))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(MissingSessionException::new);

        return authenticateUserUsecase.invoke(token);
    }
}
