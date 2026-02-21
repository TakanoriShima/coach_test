package repository;

import java.sql.Connection;
import java.sql.SQLException;

import util.ConnectionFactory;

public abstract class BaseRepository {

	protected Connection getConnection() throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		try {
			System.out.println("[DB] catalog=" + conn.getCatalog());
		} catch (Exception ignore) {
		}
		return conn;
	}

	protected void handleSQLException(SQLException e, String operation) {
		System.err.println("[Repository] Database error in " + operation + ": " + e.getMessage());
		e.printStackTrace();
	}
}
