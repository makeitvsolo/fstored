package github.makeitvsolo.fstored.user.access.integration.test;

import github.makeitvsolo.fstored.core.unique.UniqueString;
import github.makeitvsolo.fstored.user.access.encoding.bcrypt.BcryptEncode;
import github.makeitvsolo.fstored.user.access.encoding.bcrypt.configure.ConfigureBcryptEncode;
import github.makeitvsolo.fstored.user.access.persistence.sql.SqlUserRepository;
import github.makeitvsolo.fstored.user.access.persistence.sql.configure.ConfigureSqlUserRepository;
import github.makeitvsolo.fstored.user.access.session.redis.RedisSessionRepository;
import github.makeitvsolo.fstored.user.access.session.redis.configure.ConfigureRedisSessionRepository;
import github.makeitvsolo.fstored.user.access.session.redis.token.CreateRedisSessionToken;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Testcontainers
public abstract class FstoredUserAccessIntegrationTest {

    private static final int BCRYPT_COST = 6;
    private static final String BCRYPT_SALT = "supersecret_salt";

    private static final String POSTGRES_IMAGE = "postgres:15";
    private static final int POSTGRES_PORT = 5432;
    private static final String POSTGRES_DATABASE = "test";
    private static final String POSTGRES_USER = "testuser";
    private static final String POSTGRES_PASSWORD = "testpassword";
    private static final String POSTGRES_START_COMMAND = "postgres -c fsync=off";
    private static final String POSTGRES_HEALTH_LOG_MESSAGE = ".*database system is ready to accept connections.*\\s";

    private static final String REDIS_IMAGE = "redis:6.0";
    private static final int REDIS_PORT = 6379;
    private static final long REDIS_SESSION_TTL_SECONDS = 500;
    private static final int REDIS_MAX_SESSIONS = 1;
    private static final String REDIS_START_COMMAND = "redis-server";
    private static final String REDIS_HEALTH_LOG_MESSAGE = ".*Ready to accept connections*\\s";

    @Container
    private static final GenericContainer<?> postgresContainer = new GenericContainer<>(
            DockerImageName.parse(POSTGRES_IMAGE)
    )
            .withExposedPorts(POSTGRES_PORT)
            .withEnv("POSTGRES_DB", POSTGRES_DATABASE)
            .withEnv("POSTGRES_USER", POSTGRES_USER)
            .withEnv("POSTGRES_PASSWORD", POSTGRES_PASSWORD)
            .withCommand(POSTGRES_START_COMMAND)
            .waitingFor(
                    Wait.forLogMessage(POSTGRES_HEALTH_LOG_MESSAGE, 2)
                            .withStartupTimeout(Duration.ofSeconds(60))
            );

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>(
            DockerImageName.parse(REDIS_IMAGE)
    )
            .withExposedPorts(REDIS_PORT)
            .withCommand(REDIS_START_COMMAND)
            .waitingFor(
                    Wait.forLogMessage(REDIS_HEALTH_LOG_MESSAGE, 1)
                            .withStartupTimeout(Duration.ofSeconds(60))
            );

    protected final UniqueString uniqueId = new UniqueString();

    protected final BcryptEncode security = ConfigureBcryptEncode.with()
            .cost(BCRYPT_COST)
            .salt16(BCRYPT_SALT.getBytes(StandardCharsets.UTF_8))
            .configured();

    protected final SqlUserRepository userRepository = ConfigureSqlUserRepository.with()
            .datasourceUrl(
                    String.format(
                            "jdbc:postgresql://%s:%d/%s",
                            postgresContainer.getHost(),
                            postgresContainer.getMappedPort(POSTGRES_PORT),
                            POSTGRES_DATABASE
                    )
            )
            .username(POSTGRES_USER)
            .password(POSTGRES_PASSWORD)
            .configured();

    protected final CreateRedisSessionToken createSession = new CreateRedisSessionToken(
            uniqueId, REDIS_SESSION_TTL_SECONDS
    );

    protected final RedisSessionRepository sessionRepository = ConfigureRedisSessionRepository.with()
            .host(redisContainer.getHost())
            .port(redisContainer.getMappedPort(REDIS_PORT))
            .maxSessionsPerUser(REDIS_MAX_SESSIONS)
            .configured();
}
