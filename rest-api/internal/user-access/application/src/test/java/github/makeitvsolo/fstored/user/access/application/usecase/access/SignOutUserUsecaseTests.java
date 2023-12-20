package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.user.access.application.FstoredUserAccessApplicationUnitTest;
import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.application.session.TestSessionToken;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.SessionDoesNotExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignOutUserUsecaseTests extends FstoredUserAccessApplicationUnitTest {

    @Mock
    private SessionRepository<TestSessionToken> repository;

    @InjectMocks
    private SignOutUserUsecase<TestSessionToken> usecase;

    @Mock
    private TestSessionToken session;

    @Test
    public void invoke_RemovesSession() {
        var token = "token id";

        when(repository.findByToken(token))
                .thenReturn(Optional.of(session));

        usecase.invoke(token);

        verify(repository).remove(session);
    }

    @Test
    public void invoke_Throws_WhenSessionDoesNotExists() {
        var token = "token id";

        when(repository.findByToken(token))
                .thenReturn(Optional.empty());

        assertThrows(SessionDoesNotExistsException.class, () -> usecase.invoke(token));
    }
}
