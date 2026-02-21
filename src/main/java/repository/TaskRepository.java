package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TaskRepository extends BaseRepository {

	public Task findById(int id) {
		String sql = "SELECT task_id, user_id, title, description, status, priority, "
				+ "       is_favorite, created_at, updated_at " + "FROM tasks WHERE task_id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e, "TaskRepository.findById");
		}
		return null;
	}

	// ★追加
	public boolean update(Task task) throws SQLException {

		// 所有者チェック（安全のため）
		if (!isOwner(task.getId(), task.getUserId())) {
			return false;
		}

		String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, priority = ?, "
				+ "updated_at = CURRENT_TIMESTAMP " + "WHERE task_id = ? AND user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, task.getTitle());
			ps.setString(2, task.getDescription());
			ps.setString(3, task.getStatus());
			ps.setString(4, task.getPriority());
			ps.setInt(5, task.getId());
			ps.setInt(6, task.getUserId());

			return ps.executeUpdate() > 0;
		}
	}

	public boolean save(Task task) {
		// task_id は AUTO_INCREMENT なので入れない
		// created_at / updated_at はDB側の DEFAULT で入る前提
		String sql = "INSERT INTO tasks (user_id, title, description, status, priority) " + "VALUES (?, ?, ?, ?, ?)";

		// status/priority が null のときはデフォルトに寄せる（DB defaultでもOKだけど安全側）
		String status = (task.getStatus() == null || task.getStatus().isBlank()) ? "pending" : task.getStatus();
		String priority = (task.getPriority() == null || task.getPriority().isBlank()) ? "medium" : task.getPriority();

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, task.getUserId());
			ps.setString(2, task.getTitle());
			ps.setString(3, task.getDescription()); // nullでもOK
			ps.setString(4, status);
			ps.setString(5, priority);

			int inserted = ps.executeUpdate();
			if (inserted == 0) {
				return false;
			}

			// 生成された task_id を取る
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					int generatedId = keys.getInt(1);
					task.setId(generatedId);
				}
			}

			return true;

		} catch (SQLException e) {
			handleSQLException(e, "TaskRepository.save");
			return false;
		}
	}

	public List<Task> findAll() {
		String sql = "SELECT task_id, user_id, title, description, status, priority, created_at, updated_at "
				+ "FROM tasks ORDER BY created_at DESC";

		List<Task> tasks = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				tasks.add(map(rs));
			}
		} catch (SQLException e) {
			handleSQLException(e, "TaskRepository.findAll");
		}
		return tasks;
	}

	public List<Task> findByUserId(int userId) {
		String sql = "SELECT task_id, user_id, title, description, status, priority, created_at, updated_at "
				+ "FROM tasks WHERE user_id = ? ORDER BY created_at DESC";

		List<Task> tasks = new ArrayList<>();

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tasks.add(map(rs));
				}
			}
		} catch (SQLException e) {
			handleSQLException(e, "TaskRepository.findByUserId");
		}
		return tasks;
	}

	/** 所有者確認：task_id と user_id が一致する行があるか */
	public boolean isOwner(long taskId, int userId) {
		String sql = "SELECT COUNT(*) FROM tasks WHERE task_id = ? AND user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, taskId);
			ps.setInt(2, userId);

			try (ResultSet rs = ps.executeQuery()) {
				rs.next(); // COUNT(*) は必ず1行返る
				int count = rs.getInt(1);
				return count > 0;
			}

		} catch (SQLException e) {
			handleSQLException(e, "TaskRepository.isOwner");
			return false;
		}
	}

	/** 自分のタスク件数 */
	public int countByUserId(int userId) {
		String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}

		} catch (SQLException e) {
			handleSQLException(e, "TaskRepository.countByUserId");
			return 0;
		}
	}

	/** 所有者条件付き削除 */
	public int deleteByIdAndUserId(long taskId, int userId) {
		String sql = "DELETE FROM tasks WHERE task_id = ? AND user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, (int) taskId);
			ps.setInt(2, userId);

			return ps.executeUpdate(); // 1なら削除成功、0なら存在しない or 権限なし

		} catch (SQLException e) {
			handleSQLException(e, "TaskRepository.deleteByIdAndUserId");
			return 0;
		}
	}

	// ★追加：キーワード検索（ソート付き）
	public List<Task> search(int userId, String keyword, String sort) throws SQLException {
		String sql;
		if ("ASC".equals(sort)) {
			sql = "SELECT * FROM tasks WHERE user_id = ? AND title LIKE ? ORDER BY created_at ASC";
		} else {
			sql = "SELECT * FROM tasks WHERE user_id = ? AND title LIKE ? ORDER BY created_at DESC";
		}

		List<Task> tasks = new ArrayList<>();

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, userId);
			ps.setString(2, "%" + keyword + "%"); // 部分一致 :contentReference[oaicite:1]{index=1}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tasks.add(map(rs));
				}
			}
		}
		return tasks;
	}

	// ★追加：検索条件に該当する総件数（ページング用）
	public int countTasks(int userId, String keyword) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM tasks WHERE user_id = ?");

		boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
		if (hasKeyword) {
			sql.append(" AND title LIKE ?");
		}

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

			int idx = 1;
			ps.setInt(idx++, userId);

			if (hasKeyword) {
				ps.setString(idx++, "%" + keyword + "%");
			}

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	// ★追加：ページング対応（keywordあり/なし両対応）＋ソート
	public List<Task> searchWithPaging(int userId, String keyword, String sort, int pageSize, int offset)
			throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM tasks WHERE user_id = ?");

		boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
		if (hasKeyword) {
			sql.append(" AND title LIKE ?");
		}

		// ★お気に入り優先 + ソート
		if ("ASC".equalsIgnoreCase(sort)) {
			sql.append(" ORDER BY is_favorite DESC, created_at ASC");
		} else {
			sql.append(" ORDER BY is_favorite DESC, created_at DESC");
		}

		sql.append(" LIMIT ? OFFSET ?");

		List<Task> tasks = new ArrayList<>();

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

			int idx = 1;
			ps.setInt(idx++, userId);

			if (hasKeyword) {
				ps.setString(idx++, "%" + keyword.trim() + "%");
			}

			ps.setInt(idx++, pageSize);
			ps.setInt(idx++, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tasks.add(map(rs));
				}
			}
		}
		return tasks;
	}

	// ★追加：全件取得（ソート付き）
	public List<Task> findAllByUserIdWithSort(int userId, String sort) throws SQLException {
		String sql;
		if ("ASC".equals(sort)) {
			sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY created_at ASC";
		} else {
			sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY created_at DESC";
		}

		List<Task> tasks = new ArrayList<>();

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tasks.add(map(rs));
				}
			}
		}
		return tasks;
	}

	// お気に入り切り替え（true ⇔ false）
	public boolean toggleFavorite(int taskId, int userId) throws SQLException {

		// 所有者チェック（超重要）
		if (!isOwner(taskId, userId)) {
			return false;
		}

		String sql = "UPDATE tasks " + "SET is_favorite = NOT is_favorite, updated_at = CURRENT_TIMESTAMP "
				+ "WHERE task_id = ? AND user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, taskId);
			ps.setInt(2, userId);

			int updated = ps.executeUpdate();
			return updated > 0;
		}
	}

	private Task map(ResultSet rs) throws SQLException {
		Task task = new Task();
		task.setId(rs.getInt("task_id"));
		task.setUserId(rs.getInt("user_id"));
		task.setTitle(rs.getString("title"));
		task.setDescription(rs.getString("description"));
		task.setStatus(rs.getString("status"));
		task.setPriority(rs.getString("priority"));
		task.setFavorite(rs.getBoolean("is_favorite")); // ★お気に入り機能
		task.setCreatedAt(rs.getTimestamp("created_at"));
		task.setUpdatedAt(rs.getTimestamp("updated_at"));
		return task;
	}

}
