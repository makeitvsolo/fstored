package github.makeitvsolo.fstored.user.access.session.redis.configure;

import github.makeitvsolo.fstored.user.access.session.redis.RedisSessionRepository;
import github.makeitvsolo.fstored.user.access.session.redis.exception.RedisSessionRepositoryConfigurationException;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class ConfigureRedisSessionRepository {

    private static final int MIN_SESSIONS_PER_USER = 1;

    private String host;
    private Integer port;
    private Integer maxSessionsPerUser;

    ConfigureRedisSessionRepository() {

    }

    public static ConfigureRedisSessionRepository with() {
        return new ConfigureRedisSessionRepository();
    }

    public ConfigureRedisSessionRepository host(final String host) {
        this.host = host;
        return this;
    }

    public ConfigureRedisSessionRepository port(final int port) {
        this.port = port;
        return this;
    }

    public ConfigureRedisSessionRepository maxSessionsPerUser(final int maxSessionsPerUser) {
        this.maxSessionsPerUser = maxSessionsPerUser;
        return this;
    }

    public RedisSessionRepository configured() {
        if (host == null) {
            throw new RedisSessionRepositoryConfigurationException("missing host");
        }

        if (port == null) {
            throw new RedisSessionRepositoryConfigurationException("missing port");
        }

        if (maxSessionsPerUser == null) {
            throw new RedisSessionRepositoryConfigurationException("missing max sessions");
        }

        if (maxSessionsPerUser < MIN_SESSIONS_PER_USER) {
            throw new RedisSessionRepositoryConfigurationException(
                    String.format("max sessions should be greater equals than %d", MIN_SESSIONS_PER_USER)
            );
        }

        var config = new JedisPoolConfig();

        return new RedisSessionRepository(
                new JedisPool(config, host, port),
                maxSessionsPerUser
        );
    }
}
