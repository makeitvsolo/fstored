package github.makeitvsolo.fstored.user.access.persistence.sql;

import github.makeitvsolo.fstored.user.access.application.persistence.UserRepository;
import github.makeitvsolo.fstored.user.access.domain.User;
import github.makeitvsolo.fstored.user.access.persistence.sql.exception.SqlUserRepositoryInternalException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

public final class SqlUserRepository implements UserRepository {

    private final DataSource dataSource;

    public SqlUserRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(final User user) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(QueryUser.Insert.SQL)
        ) {
            connection.setAutoCommit(false);

            statement.setString(QueryUser.Insert.ID_PARAMETER, user.id());
            statement.setString(QueryUser.Insert.NAME_PARAMETER, user.name());
            statement.setString(QueryUser.Insert.PASSWORD_PARAMETER, user.password());

            statement.execute();
            connection.commit();
        } catch (SQLException ex) {
            throw new SqlUserRepositoryInternalException(ex);
        }
    }

    @Override
    public Optional<User> findByName(final String name) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(QueryUser.FetchByName.SQL)
        ) {
            statement.setString(QueryUser.FetchByName.NAME_PARAMETER, name);

            statement.execute();
            var cursor = statement.getResultSet();

            if (cursor.next()) {
                return Optional.of(new User(
                        cursor.getString("id"),
                        cursor.getString("name"),
                        cursor.getString("password")
                ));
            }

            return Optional.empty();
        } catch (SQLException ex) {
            throw new SqlUserRepositoryInternalException(ex);
        }
    }

    @Override
    public boolean existsByName(final String name) {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(QueryUser.ExistsByName.SQL)
        ) {
            statement.setString(QueryUser.ExistsByName.NAME_PARAMETER, name);

            statement.execute();
            var cursor = statement.getResultSet();

            if (cursor.next()) {
                return cursor.getBoolean("exists");
            }

            return false;
        } catch (SQLException ex) {
            throw new SqlUserRepositoryInternalException(ex);
        }
    }

    public void init() {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(QueryUser.Defaults.Init.SQL)
        ) {
            connection.setAutoCommit(false);

            statement.execute();

            connection.commit();
        } catch (SQLException ex) {
            throw new SqlUserRepositoryInternalException(ex);
        }
    }

    public void cleanup() {
        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(QueryUser.Defaults.Cleanup.SQL)
        ) {
            connection.setAutoCommit(false);

            statement.execute();

            connection.commit();
        } catch (SQLException ex) {
            throw new SqlUserRepositoryInternalException(ex);
        }
    }
}
