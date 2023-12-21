package github.makeitvsolo.fstored.boot.config.internal.user.access;

import github.makeitvsolo.fstored.user.access.application.encoding.Encode;
import github.makeitvsolo.fstored.user.access.encoding.bcrypt.configure.ConfigureBcryptEncode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;

@Configuration
public class EncodingConfiguration {

    private final Integer cost;
    private final String salt16;

    public EncodingConfiguration(final Environment env) {
        this.cost = env.getProperty("encoding.cost", Integer.class);
        this.salt16 = env.getProperty("encoding.salt");
    }

    @Bean
    public Encode encode() {
        return ConfigureBcryptEncode.with()
                .cost(cost)
                .salt16(salt16.getBytes(StandardCharsets.UTF_8))
                .configured();
    }
}
