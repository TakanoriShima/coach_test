package repository;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseRepository {

	public User findById(int id) {
		String sql = "SELECT user_id, username, password, email, created_at, updated_at "
				+ "FROM users WHERE user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapUser(rs);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e, "UserRepository.findById");
		}
		return null;
	}

	public List<User> findAll() {
		String sql = "SELECT user_id, username, password, email, created_at, updated_at "
				+ "FROM users ORDER BY user_id";

		List<User> users = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				users.add(mapUser(rs));
			}
		} catch (SQLException e) {
			handleSQLException(e, "UserRepository.findAll");
		}
		return users;
	}

	public User findByUsername(String username) {
		String sql = "SELECT user_id, username, password, email, created_at, updated_at "
				+ "FROM users WHERE username = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, username);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapUser(rs);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e, "UserRepository.findByUsername");
		}
		return null;
	}

	private User mapUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setEmail(rs.getString("email"));
		user.setCreatedAt(rs.getTimestamp("created_at"));
		user.setUpdatedAt(rs.getTimestamp("updated_at"));
		return user;
	}
}
