package model;

import java.sql.Timestamp;
import java.util.Objects;

public class User {
	private int id; // DBの user_id を入れる
	private String username;
	private String password;
	private String email;
	private Timestamp createdAt; // created_at
	private Timestamp updatedAt; // updated_at

	public User() {
	}

	public User(int id, String username, String password, String email, Timestamp createdAt, Timestamp updatedAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		return "User{" + "id=" + id + ", username='" + username + '\'' + ", email='" + email + '\'' + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;
		User user = (User) o;
		return id == user.id && Objects.equals(username, user.username) && Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, email);
	}
}
