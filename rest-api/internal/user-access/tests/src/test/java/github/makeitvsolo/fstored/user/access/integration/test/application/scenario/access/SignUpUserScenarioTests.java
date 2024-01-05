package github.makeitvsolo.fstored.user.access.integration.test.application.scenario.access;

import github.makeitvsolo.fstored.user.access.application.usecase.access.SignUpUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserCredentialsDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.UserAlreadyExistsException;
import github.makeitvsolo.fstored.user.access.integration.test.FstoredUserAccessIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignUpUserScenarioTests extends FstoredUserAccessIntegrationTest {

    private SignUpUserUsecase signUpUserUsecase = new SignUpUserUsecase(
            userRepository, uniqueId, security
    );

    private UserCredentialsDto user = new UserCredentialsDto("name", "passwd");
    private UserCredentialsDto userWithSameName = new UserCredentialsDto("name", "passwd");

    @Test
    public void atStart_UserDoesNotExists() {
        userRepository.init();

        assertFalse(userRepository.existsByName(user.name()));
    }

    @Nested
    public class WhenUserDoesNotExists {

        @Test
        public void userCanBeCreated_WhenUserDoesNotExists() {
            signUpUserUsecase.invoke(user);

            assertTrue(userRepository.existsByName(user.name()));
        }

        @Nested
        public class WhenUserCreated {

            @Test
            public void userWithSameNameCannotBeCreatedAgain_WhenUserCreated() {
                assertThrows(UserAlreadyExistsException.class, () -> signUpUserUsecase.invoke(userWithSameName));
            }
        }
    }
}
