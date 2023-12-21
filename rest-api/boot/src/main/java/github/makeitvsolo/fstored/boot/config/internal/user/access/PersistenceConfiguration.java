package github.makeitvsolo.fstored.boot.config.internal.user.access;

import github.makeitvsolo.fstored.user.access.application.persistence.UserRepository;
import github.makeitvsolo.fstored.user.access.persistence.sql.configure.ConfigureSqlUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PersistenceConfiguration {

    private final String postgresUrl;
    private final String postgresUser;
    private final String postgresPassword;

    public PersistenceConfiguration(final Environment env) {
        this.postgresUrl = env.getProperty("postgres.url");
        this.postgresUser = env.getProperty("postgres.username");
        this.postgresPassword = env.getProperty("postgres.password");
    }

    @Bean
    public UserRepository userRepository() {
        return ConfigureSqlUserRepository.with()
                .datasourceUrl(postgresUrl)
                .username(postgresUser)
                .password(postgresPassword)
                .configured();
    }
}
