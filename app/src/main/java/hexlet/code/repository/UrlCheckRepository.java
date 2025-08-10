package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {

    public static void save(UrlCheck check) throws SQLException {

        var sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description) VALUES (?, ?, ?, ?, ?)";

        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, check.getUrlId());
            preparedStatement.setInt(2, check.getStatusCode());
            preparedStatement.setString(3, check.getTitle());
            preparedStatement.setString(4, check.getH1());
            preparedStatement.setString(5, check.getDescription());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }

        }
    }

    public static List<UrlCheck> find(Long urlId) throws SQLException {

        var sql = "SELECT * FROM url_checks WHERE url_id = ?";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");

                var check = new UrlCheck(urlId, statusCode, title, h1, description);

                check.setId(id);
                check.setCreatedAt(createdAt);
                result.add(check);
            }

            return result;

        }
    }

    public static List<UrlCheck> getEntities() throws SQLException {

        var sql = "SELECT * FROM url_checks";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var urlId = resultSet.getLong("url_id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");

                var check = new UrlCheck(urlId, statusCode, title, h1, description);

                check.setId(id);
                check.setCreatedAt(createdAt);
                result.add(check);
            }

            return result;

        }
    }

    public static void removeAll() throws SQLException {

        var sql = "DELETE FROM url_checks";

        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {

            stmt.execute(sql);

        }
    }
}
