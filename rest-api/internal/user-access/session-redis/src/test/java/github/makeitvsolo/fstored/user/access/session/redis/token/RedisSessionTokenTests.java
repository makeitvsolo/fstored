package github.makeitvsolo.fstored.user.access.session.redis.token;

import github.makeitvsolo.fstored.user.access.session.redis.FstoredUserAccessSessionRedisUnitTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RedisSessionTokenTests extends FstoredUserAccessSessionRedisUnitTest {

    @Test
    public void timeToLiveInSeconds_ReturnsValueThatBetweenNowAndExpirationTime_WhenNowIsAfterThanExpirationTime() {
        var now = LocalDateTime.now();
        var timeToLive = 150L;
        var expirationTime = now.plusSeconds(timeToLive);

        var session = new RedisSessionToken("token", "user id", "user name", expirationTime);

        assertEquals(timeToLive, session.timeToLiveInSeconds(now));
    }

    @Test
    public void timeToLive_ReturnsZero_WhenNowIsBeforeThanExpirationTime() {
        var now = LocalDateTime.now();
        var timeToLive = 0L;
        var expirationTime = now.minusSeconds(150L);

        var session = new RedisSessionToken("token", "user id", "user name", expirationTime);

        assertEquals(timeToLive, session.timeToLiveInSeconds(now));
    }

    @Test
    public void expired_ReturnsFalse_WhenNowIsBeforeThanExpirationTime() {
        var now = LocalDateTime.now();
        var timeToLiveInSeconds = 150L;
        var expirationTime = now.plusSeconds(timeToLiveInSeconds);

        var session = new RedisSessionToken("token", "user id", "user name", expirationTime);

        assertFalse(session.expired());
    }

    @Test
    public void expired_ReturnsTrue_WhenNowIsAfterThanExpirationTime() {
        var now = LocalDateTime.now();
        var expirationTime = now.minusSeconds(150L);

        var session = new RedisSessionToken("token", "user id", "user name", expirationTime);

        assertTrue(session.expired());
    }
}
