package github.makeitvsolo.fstored.boot.config.spring.session;

import github.makeitvsolo.fstored.boot.config.spring.session.authentication.AuthenticatedArgumentResolver;
import github.makeitvsolo.fstored.boot.config.spring.session.token.SessionTokenArgumentResolver;
import github.makeitvsolo.fstored.user.access.application.usecase.access.AuthenticateUserUsecase;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebSessionConfiguration implements WebMvcConfigurer {

    private final AuthenticateUserUsecase<?> authenticateUserUsecase;

    public WebSessionConfiguration(final AuthenticateUserUsecase<?> authenticateUserUsecase) {
        this.authenticateUserUsecase = authenticateUserUsecase;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SessionTokenArgumentResolver());
        argumentResolvers.add(new AuthenticatedArgumentResolver(
                authenticateUserUsecase
        ));
    }
}
