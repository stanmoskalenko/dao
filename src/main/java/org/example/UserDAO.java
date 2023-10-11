package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection conn) {
        connection = conn;
    }

    public void initialize() throws SQLException {
        var initializeQuery = "CREATE TABLE user (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
        try (var initializeStmt = connection.prepareStatement(initializeQuery)) {
            initializeStmt.execute();
        }
    }

    public void save(User user) throws SQLException {

        try (var resultSet = connection.getMetaData().getTables(null, null, "USER", null)) {
            if (!resultSet.next()) {
                initialize();
            }
        }

        if (user.getId() == null) {
            var insertQuery = "INSERT INTO user (username, phone) VALUES (?, ?)";
            try (var insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, user.getName());
                insertStmt.setString(2, user.getPhone());
                insertStmt.executeUpdate();
                var generatedKeys = insertStmt.getGeneratedKeys();

                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            var updateQuery = "UPDATE user SET username = ?, phone = ? WHERE id = ?";
            try (var updateStmt = connection.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS)) {
                updateStmt.setString(1, user.getName());
                updateStmt.setString(2, user.getPhone());
                updateStmt.setLong(3, user.getId());
                updateStmt.executeUpdate();
                var generatedKeys = updateStmt.getGeneratedKeys();

                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        }
    }

    public void delete(Long id) throws SQLException {
        var deleteQuery = "DELETE FROM user WHERE id = ?";
        try (var deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setLong(1, id);
            deleteStmt.executeUpdate();
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        var selectQuery = "SELECT * FROM user WHERE id = ?";
        try (var stmt = connection.prepareStatement(selectQuery)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var username = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                var user = new User(username, phone);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }
}