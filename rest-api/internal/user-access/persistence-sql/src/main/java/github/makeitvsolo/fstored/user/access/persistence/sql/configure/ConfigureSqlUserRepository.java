package github.makeitvsolo.fstored.user.access.persistence.sql.configure;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import github.makeitvsolo.fstored.user.access.persistence.sql.SqlUserRepository;
import github.makeitvsolo.fstored.user.access.persistence.sql.exception.SqlUserRepositoryConfigurationException;

public final class ConfigureSqlUserRepository {

    private String url;
    private String username;
    private String password;

    ConfigureSqlUserRepository() {

    }

    public static ConfigureSqlUserRepository with() {
        return new ConfigureSqlUserRepository();
    }

    public ConfigureSqlUserRepository datasourceUrl(final String url) {
        this.url = url;
        return this;
    }

    public ConfigureSqlUserRepository username(final String username) {
        this.username = username;
        return this;
    }

    public ConfigureSqlUserRepository password(final String password) {
        this.password = password;
        return this;
    }

    public SqlUserRepository configured() {
        if (url == null) {
            throw new SqlUserRepositoryConfigurationException("missing datasource url");
        }

        if (username == null) {
            throw new SqlUserRepositoryConfigurationException("missing username");
        }

        if (password == null) {
            throw new SqlUserRepositoryConfigurationException("missing password");
        }

        var config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        var datasource = new HikariDataSource(config);
        return new SqlUserRepository(datasource);
    }
}
