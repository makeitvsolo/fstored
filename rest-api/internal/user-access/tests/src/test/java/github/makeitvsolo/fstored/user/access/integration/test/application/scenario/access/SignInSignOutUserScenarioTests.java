package github.makeitvsolo.fstored.user.access.integration.test.application.scenario.access;

import github.makeitvsolo.fstored.user.access.application.usecase.access.AuthenticateUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignInUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignOutUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignUpUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.AccessTokenDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserCredentialsDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.InvalidUserPasswordException;
import github.makeitvsolo.fstored.user.access.integration.test.FstoredUserAccessIntegrationTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SignInSignOutUserScenarioTests extends FstoredUserAccessIntegrationTest {

    private SignUpUserUsecase signUpUserUsecase = new SignUpUserUsecase(
            userRepository, uniqueId, security
    );

    private SignInUserUsecase<?> signInUserUsecase = new SignInUserUsecase<>(
            userRepository, sessionRepository, createSession, security
    );

    private SignOutUserUsecase<?> signOutUserUsecase = new SignOutUserUsecase<>(
            sessionRepository
    );

    private AuthenticateUserUsecase<?> authenticateUserUsecase = new AuthenticateUserUsecase<>(
            sessionRepository
    );

    private UserCredentialsDto user = new UserCredentialsDto("name", "passwd");
    private UserCredentialsDto userWithInvalidPassword = new UserCredentialsDto(
            "name", "but passwd is invalid"
    );

    @Test
    public void atStart_UserExists() {
        userRepository.init();
        signUpUserUsecase.invoke(user);

        assertTrue(userRepository.existsByName(user.name()));
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class WhenUserExists {

        @Test
        @Order(1)
        public void sessionForUserCanBeCreated_WhenUserExists() {
            var session = signInUserUsecase.invoke(user);
            assertTrue(sessionRepository.findByToken(session.token()).isPresent());
        }

        @Test
        @Order(2)
        public void sessionForUserCannotBeCreated_WhenUserExistsButPasswordIsInvalid() {
            assertThrows(InvalidUserPasswordException.class, () -> signInUserUsecase.invoke(userWithInvalidPassword));
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        public class WhenSessionCreated {

            private AccessTokenDto session;

            @BeforeEach
            public void beforeEach() {
                session = signInUserUsecase.invoke(user);
                assertTrue(sessionRepository.findByToken(session.token()).isPresent());
            }

            @Test
            @Order(1)
            public void userCanBeAuthenticated_WhenSessionCreated() {
                var activeUser = authenticateUserUsecase.invoke(session.token());
                assertEquals(user.name(), activeUser.name());
            }

            @Test
            @Order(2)
            public void sessionCanBeUpdated_WhenSessionCreated() {
                var updatedSession = signInUserUsecase.invoke(user);
                assertTrue(sessionRepository.findByToken(updatedSession.token()).isPresent());
            }

            @Test
            @Order(3)
            public void sessionCanBeRemoved_WhenSessionCreated() {
                signOutUserUsecase.invoke(session.token());
                assertTrue(sessionRepository.findByToken(session.token()).isEmpty());
            }
        }
    }
}
