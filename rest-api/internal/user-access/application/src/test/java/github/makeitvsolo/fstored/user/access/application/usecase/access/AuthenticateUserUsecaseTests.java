package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.user.access.application.FstoredUserAccessApplicationUnitTest;
import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.application.session.TestSessionToken;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionAlreadyExpiredException;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionDoesNotExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticateUserUsecaseTests extends FstoredUserAccessApplicationUnitTest {

    @Mock
    private SessionRepository<TestSessionToken> repository;

    @InjectMocks
    private AuthenticateUserUsecase<TestSessionToken> usecase;

    @Mock
    private TestSessionToken session;

    @Test
    public void invoke_ReturnsActiveUserFromSession() {
        var token = "token id";

        when(repository.findByToken(token))
                .thenReturn(Optional.of(session));
        when(session.expired())
                .thenReturn(false);

        var expected = new UserDto(session.userId(), session.userName());

        assertEquals(expected, usecase.invoke(token));
    }

    @Test
    public void invoke_Throws_WhenSessionDoesNotExists() {
        var token = "token id";

        when(repository.findByToken(token))
                .thenReturn(Optional.empty());

        assertThrows(SessionDoesNotExistsException.class, () -> usecase.invoke(token));
    }

    @Test
    public void invoke_Throws_WhenSessionAlreadyExpired() {
        var token = "token id";

        when(repository.findByToken(token))
                .thenReturn(Optional.of(session));
        when(session.expired())
                .thenReturn(true);

        assertThrows(SessionAlreadyExpiredException.class, () -> usecase.invoke(token));
    }
}
