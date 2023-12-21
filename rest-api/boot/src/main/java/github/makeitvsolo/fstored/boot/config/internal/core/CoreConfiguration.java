package github.makeitvsolo.fstored.boot.config.internal.core;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.core.unique.UniqueString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfiguration {

    @Bean
    public Unique<String> unique() {
        return new UniqueString();
    }
}
