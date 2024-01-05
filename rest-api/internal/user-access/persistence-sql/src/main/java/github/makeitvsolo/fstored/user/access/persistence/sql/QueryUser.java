package github.makeitvsolo.fstored.user.access.persistence.sql;

public final class QueryUser {

    static class Insert {

        static final int ID_PARAMETER = 1;
        static final int NAME_PARAMETER = 2;
        static final int PASSWORD_PARAMETER = 3;
        static final String SQL = """
                INSERT INTO users (id, name, password)
                VALUES (?, ?, ?)
                """;
    }

    static class FetchByName {

        static final int NAME_PARAMETER = 1;
        static final String SQL = """
                SELECT id, name, password
                FROM users
                WHERE name = ?
                """;
    }

    static class ExistsByName {

        static final int NAME_PARAMETER = 1;
        static final String SQL = """
                SELECT EXISTS(
                    SELECT id
                    FROM users
                    WHERE name = ?
                ) as exists
                """;
    }

    static class Defaults {

        static class Init {

            static final String SQL = """
                    CREATE TABLE IF NOT EXISTS users (
                        id VARCHAR(60),
                        name VARCHAR NOT NULL UNIQUE,
                        password VARCHAR NOT NULL,
                        PRIMARY KEY (id)
                    );
                    """;
        }

        static class Cleanup {

            static final String SQL = """
                    TRUNCATE users;
                    """;
        }
    }
}
