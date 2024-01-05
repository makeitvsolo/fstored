package github.makeitvsolo.fstored.boot.config.internal.user.access;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.user.access.application.session.CreateSessionToken;
import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.session.redis.configure.ConfigureRedisSessionRepository;
import github.makeitvsolo.fstored.user.access.session.redis.token.CreateRedisSessionToken;
import github.makeitvsolo.fstored.user.access.session.redis.token.RedisSessionToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SessionConfiguration {

    private final String redisHost;
    private final Integer redisPort;
    private final Long sessionTimeToLive;
    private final Integer maxSessionsPerUser;

    public SessionConfiguration(final Environment env) {
        this.redisHost = env.getProperty("redis.host");
        this.redisPort = env.getProperty("redis.port", Integer.class);
        this.sessionTimeToLive = env.getProperty("redis.session.ttl", Long.class);
        this.maxSessionsPerUser = env.getProperty("redis.session.max-per-user", Integer.class);
    }

    @Bean
    public CreateSessionToken<RedisSessionToken> createSession(final Unique<String> unique) {
        return new CreateRedisSessionToken(
                unique, sessionTimeToLive
        );
    }

    @Bean
    public SessionRepository<RedisSessionToken> sessionRepository() {
        return ConfigureRedisSessionRepository.with()
                .host(redisHost)
                .port(redisPort)
                .maxSessionsPerUser(maxSessionsPerUser)
                .configured();
    }
}
