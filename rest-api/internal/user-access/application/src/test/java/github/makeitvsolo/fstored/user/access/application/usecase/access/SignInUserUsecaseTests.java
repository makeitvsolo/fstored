package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.user.access.application.FstoredUserAccessApplicationUnitTest;
import github.makeitvsolo.fstored.user.access.application.encoding.Encode;
import github.makeitvsolo.fstored.user.access.application.persistence.UserRepository;
import github.makeitvsolo.fstored.user.access.application.session.CreateSessionToken;
import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.application.session.TestSessionToken;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.AccessTokenDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserCredentialsDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.InvalidUserPasswordException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.UserDoesNotExistsException;
import github.makeitvsolo.fstored.user.access.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignInUserUsecaseTests extends FstoredUserAccessApplicationUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository<TestSessionToken> sessionRepository;

    @Mock
    private CreateSessionToken<TestSessionToken> userSession;

    @Mock
    private Encode security;

    @InjectMocks
    private SignInUserUsecase<TestSessionToken> usecase;

    @Mock
    private TestSessionToken session;

    @Test
    public void invoke_SavesSession() {
        var existingUser = new User("user id", "name", "passwd");
        var payload = new UserCredentialsDto("name", "passwd");

        var expected = new AccessTokenDto(
                session.token(), session.expiresAt(), new UserDto(existingUser.id(), existingUser.name())
        );

        when(userRepository.findByName(payload.name()))
                .thenReturn(Optional.of(existingUser));
        when(security.matches(payload.password(), existingUser.password()))
                .thenReturn(true);
        when(userSession.createFor(existingUser))
                .thenReturn(session);

        var actual = usecase.invoke(payload);

        verify(sessionRepository).save(session);
        assertEquals(expected, actual);
    }

    @Test
    public void invoke_Throws_WhenUserDoesNotExists() {
        var payload = new UserCredentialsDto("name", "passwd");

        when(userRepository.findByName(payload.name()))
                .thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistsException.class, () -> usecase.invoke(payload));
    }

    @Test
    public void invoke_Throws_WhenPasswordIsInvalid() {
        var existingUser = new User("user id", "name", "passwd");

        var payload = new UserCredentialsDto("name", "passwd");
        when(userRepository.findByName(payload.name()))
                .thenReturn(Optional.of(existingUser));
        when(security.matches(payload.password(), existingUser.password()))
                .thenReturn(false);

        assertThrows(InvalidUserPasswordException.class, () -> usecase.invoke(payload));
    }
}
