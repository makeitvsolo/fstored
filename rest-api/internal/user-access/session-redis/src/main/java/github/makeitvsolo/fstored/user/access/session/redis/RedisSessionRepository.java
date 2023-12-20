package github.makeitvsolo.fstored.user.access.session.redis;

import github.makeitvsolo.fstored.user.access.application.session.SessionRepository;
import github.makeitvsolo.fstored.user.access.session.redis.exception.RedisSessionRepositoryInternalException;
import github.makeitvsolo.fstored.user.access.session.redis.token.RedisSessionToken;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public final class RedisSessionRepository implements SessionRepository<RedisSessionToken> {

    private static final String TOKEN_PREFIX = "token:";
    private static final String USER_ID_PREFIX = "user:";

    private static final String USER_ID = "subid";
    private static final String USER_NAME = "sub";
    private static final String EXPIRATION_TIME = "exp";

    private final JedisPool jedis;
    private final int maxSessionsPerUser;

    public RedisSessionRepository(final JedisPool jedis, final int maxSessionsPerUser) {
        this.jedis = jedis;
        this.maxSessionsPerUser = maxSessionsPerUser;
    }

    @Override
    public void save(final RedisSessionToken session) {
        try (var cn = jedis.getResource()) {
            var tx = cn.multi();

            var token = TOKEN_PREFIX.concat(session.token());
            var userId = USER_ID_PREFIX.concat(session.userId());
            var timeToLive = session.timeToLiveInSeconds(LocalDateTime.now());
            var firstElement = 0;
            var lastElement = maxSessionsPerUser - 1;

            var rawToken = Map.ofEntries(
                    Map.entry(USER_ID, session.userId()),
                    Map.entry(USER_NAME, session.userName()),
                    Map.entry(EXPIRATION_TIME, session.expiresAt().format(DateTimeFormatter.ISO_DATE_TIME))
            );

            tx.hset(token, rawToken);
            tx.lpush(userId, token);
            tx.ltrim(userId, firstElement, lastElement);
            tx.expire(token, timeToLive);
            tx.expire(userId, timeToLive);

            tx.exec();
        } catch (Exception ex) {
            throw new RedisSessionRepositoryInternalException(ex);
        }
    }

    @Override
    public void remove(final RedisSessionToken session) {
        try (var cn = jedis.getResource()) {
            var tx = cn.multi();

            var token = TOKEN_PREFIX.concat(session.token());
            var userId = USER_ID_PREFIX.concat(session.userId());
            var elementsToRemove = -1;

            tx.lrem(userId, elementsToRemove, token);
            tx.del(token);

            tx.exec();
        } catch (Exception ex) {
            throw new RedisSessionRepositoryInternalException(ex);
        }
    }

    @Override
    public Optional<RedisSessionToken> findByToken(final String rawToken) {
        try (var cn = jedis.getResource()) {
            var token = TOKEN_PREFIX.concat(rawToken);

            var rawSession = cn.hgetAll(token);

            if (rawSession.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(new RedisSessionToken(
                    rawToken,
                    rawSession.get(USER_ID),
                    rawSession.get(USER_NAME),
                    LocalDateTime.parse(rawSession.get(EXPIRATION_TIME), DateTimeFormatter.ISO_DATE_TIME)
            ));
        } catch (Exception ex) {
            throw new RedisSessionRepositoryInternalException(ex);
        }
    }
}
