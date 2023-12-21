package github.makeitvsolo.fstored.boot.config.spring.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebCorsConfiguration implements WebMvcConfigurer {

    private final List<String> origins;
    private final List<String> headers;
    private final List<String> methods;

    public WebCorsConfiguration(final Environment env) {
        this.origins = Arrays.stream(
                        env.getProperty("web.cors.allowed-origins")
                                .split(",")
                )
                .toList();

        this.headers = Arrays.stream(
                        env.getProperty("web.cors.allowed-headers")
                                .split(",")
                )
                .toList();

        this.methods = Arrays.stream(
                        env.getProperty("web.cors.allowed-methods")
                                .split(",")
                )
                .toList();
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedHeaders(headers.toArray(new String[0]))
                .allowedMethods(methods.toArray(new String[0]))
                .allowCredentials(true);
    }
}
