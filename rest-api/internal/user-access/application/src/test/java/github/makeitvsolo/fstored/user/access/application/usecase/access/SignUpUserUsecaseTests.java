package github.makeitvsolo.fstored.user.access.application.usecase.access;

import github.makeitvsolo.fstored.core.unique.Unique;
import github.makeitvsolo.fstored.user.access.application.FstoredUserAccessApplicationUnitTest;
import github.makeitvsolo.fstored.user.access.application.encoding.Encode;
import github.makeitvsolo.fstored.user.access.application.persistence.UserRepository;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserCredentialsDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.exception.UserAlreadyExistsException;
import github.makeitvsolo.fstored.user.access.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignUpUserUsecaseTests extends FstoredUserAccessApplicationUnitTest {

    @Mock
    private UserRepository repository;

    @Mock
    private Unique<String> userId;

    @Mock
    private Encode security;

    @InjectMocks
    private SignUpUserUsecase usecase;

    @Test
    public void invoke_SavesUser() {
        var payload = new UserCredentialsDto("name", "passwd");
        var user = new User("unique string", payload.name(), "encoded password");

        var expected = new UserDto("unique string", payload.name());

        when(repository.existsByName(payload.name()))
                .thenReturn(false);
        when(userId.unique())
                .thenReturn("unique string");
        when(security.encode(payload.password()))
                .thenReturn("encoded passwd");

        var actual = usecase.invoke(payload);

        verify(repository).save(user);
        verify(security).encode(payload.password());

        assertEquals(expected, actual);
    }

    @Test
    public void invoke_Throws_WhenUserAlreadyExists() {
        var payload = new UserCredentialsDto("name", "passwd");

        when(repository.existsByName(payload.name()))
                .thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> usecase.invoke(payload));
    }
}
