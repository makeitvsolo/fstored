package github.makeitvsolo.fstored.storage.integration.test.application.scenario.folder;

import github.makeitvsolo.fstored.storage.application.usecase.folder.MountRootFolderUsecase;
import github.makeitvsolo.fstored.storage.integration.test.FstoredStorageIntegrationTest;
import github.makeitvsolo.fstored.storage.minio.handle.MinioHandle;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MountRootFolderScenarioTests extends FstoredStorageIntegrationTest {

    private MountRootFolderUsecase<?> mountRootUsecase = new MountRootFolderUsecase<>(
            folderStorage, composeMinioHandle
    );

    private String root = "root";

    private MinioHandle rootHandle = composeMinioHandle.composeAsRoot(root).unwrap();

    @Test
    public void atStart_RootDoesNotExists() {
        assertFalse(folderStorage.exists(rootHandle));
    }

    @Nested
    public class WhenRootDoesNotExists {

        @Test
        public void rootCanBeMounted_WhenRootDoesNotExists() {
            mountRootUsecase.invoke(root);
            assertTrue(folderStorage.exists(rootHandle));
        }
    }
}
