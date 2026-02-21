package model;

import java.sql.Timestamp;
import java.util.Objects;

public class Task {
	private int id; // DBの task_id
	private int userId; // DBの user_id
	private String title;
	private String description;
	private String status;
	private String priority;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private boolean favorite; // ★追加：お気に入りフラグ

	public boolean isFavorite() { // ★追加：お気に入りフラグ getter/setter
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public Task() {
	}

	public Task(int id, int userId, String title, String description, String status, String priority,
			Timestamp createdAt, Timestamp updatedAt) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.status = status;
		this.priority = priority;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Task{" + "id=" + id + ", userId=" + userId + ", title='" + title + '\'' + ", status='" + status + '\''
				+ ", priority='" + priority + '\'' + ", favorite=" + favorite // ★お気に入り機能追加
				+ ", createdAt=" + createdAt + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Task))
			return false;
		Task task = (Task) o;
		return id == task.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
