package github.makeitvsolo.fstored.storage.integration.test;

import github.makeitvsolo.fstored.storage.application.mapping.IntoFileContentDtoMapper;
import github.makeitvsolo.fstored.storage.application.mapping.IntoFolderContentDtoMapper;
import github.makeitvsolo.fstored.storage.application.mapping.IntoMetaDtoMapper;
import github.makeitvsolo.fstored.storage.minio.MinioFileStorage;
import github.makeitvsolo.fstored.storage.minio.MinioFolderStorage;
import github.makeitvsolo.fstored.storage.minio.configure.ConfigureMinioFileStorage;
import github.makeitvsolo.fstored.storage.minio.configure.ConfigureMinioFolderStorage;
import github.makeitvsolo.fstored.storage.minio.configure.ConfigureMinioObjectStorage;
import github.makeitvsolo.fstored.storage.minio.handle.ComposeMinioHandle;
import github.makeitvsolo.fstored.storage.minio.internal.MinioObjectStorage;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@Testcontainers
public abstract class FstoredStorageIntegrationTest {

    private static final String MINIO_IMAGE = "minio/minio";
    private static final int MINIO_PORT = 9000;
    private static final String MINIO_START_COMMAND = "server /data";
    private static final String MINIO_HEALTH_CHECK_COMMAND = "/minio/health/live";

    private static final String MINIO_ROOT_USER = "testuser";
    private static final String MINIO_ROOT_PASSWORD = "testpassword";
    private static final String MINIO_ROOT_BUCKET = "testbucket";

    @Container
    private static final GenericContainer<?> container = new GenericContainer<>(
            DockerImageName.parse(MINIO_IMAGE)
    )
            .withExposedPorts(MINIO_PORT)
            .withEnv("MINIO_ROOT_USER", MINIO_ROOT_USER)
            .withEnv("MINIO_ROOT_PASSWORD", MINIO_ROOT_PASSWORD)
            .withCommand(MINIO_START_COMMAND)
            .waitingFor(
                    Wait.forHttp(MINIO_HEALTH_CHECK_COMMAND)
                            .forPort(MINIO_PORT)
                            .withStartupTimeout(Duration.ofSeconds(60))
            );

    private final MinioObjectStorage objectStorage = ConfigureMinioObjectStorage.with()
            .minioUrl(String.format(
                    "http://%s:%s",
                    container.getHost(),
                    container.getMappedPort(MINIO_PORT)
            ))
            .minioCredentials(MINIO_ROOT_USER, MINIO_ROOT_PASSWORD)
            .rootBucket(MINIO_ROOT_BUCKET, ConfigureMinioObjectStorage.RootBucket.MakeIfNotExists)
            .configured();


    protected final ComposeMinioHandle composeMinioHandle = new ComposeMinioHandle();
    protected final IntoFileContentDtoMapper intoFileContentMapper = new IntoFileContentDtoMapper();
    protected final IntoFolderContentDtoMapper intoFolderContentMapper = new IntoFolderContentDtoMapper();
    protected final IntoMetaDtoMapper intoMetaMapper = new IntoMetaDtoMapper();

    protected final MinioFolderStorage folderStorage = ConfigureMinioFolderStorage.with()
            .objectStorage(objectStorage)
            .configured();

    protected final MinioFileStorage fileStorage = ConfigureMinioFileStorage.with()
            .objectStorage(objectStorage)
            .configured();
}
