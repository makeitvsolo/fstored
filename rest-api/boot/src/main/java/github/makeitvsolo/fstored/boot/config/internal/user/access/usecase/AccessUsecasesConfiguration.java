package github.makeitvsolo.fstored.boot.config.internal.user.access.usecase;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.user.access.application.encoding.Encode;
import github.makeitvsolo.fstored.user.access.application.persistence.UserRepository;
import github.makeitvsolo.fstored.user.access.application.session.CreateSessionToken;
import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.application.usecase.access.AuthenticateUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignInUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignOutUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignUpUserUsecase;
import github.makeitvsolo.fstored.user.access.session.redis.token.RedisSessionToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessUsecasesConfiguration {

    @Bean
    public SignUpUserUsecase signUpUserUsecase(
            final UserRepository userRepository,
            final Unique<String> unique,
            final Encode encode
    ) {
        return new SignUpUserUsecase(
                userRepository,
                unique,
                encode
        );
    }

    @Bean
    public SignInUserUsecase<RedisSessionToken> signInUserUsecase(
            final UserRepository userRepository,
            final SessionRepository<RedisSessionToken> sessionRepository,
            final CreateSessionToken<RedisSessionToken> createSession,
            final Encode encode
    ) {
        return new SignInUserUsecase<>(
                userRepository,
                sessionRepository,
                createSession,
                encode
        );
    }

    @Bean
    public SignOutUserUsecase<RedisSessionToken> signOutUserUsecase(
            final SessionRepository<RedisSessionToken> sessionRepository
    ) {
        return new SignOutUserUsecase<>(
                sessionRepository
        );
    }

    @Bean
    public AuthenticateUserUsecase<RedisSessionToken> authenticateUserUsecase(
            final SessionRepository<RedisSessionToken> sessionRepository
    ) {
        return new AuthenticateUserUsecase<>(
                sessionRepository
        );
    }
}
