package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.ConnectionFactory;

public class ConnectionTest {

	public static void main(String[] args) {

		// try-with-resources：Connectionは自動でcloseされる
		try (Connection conn = ConnectionFactory.getConnection()) {

			System.out.println("Connection closed? " + conn.isClosed());

			// users 件数
			System.out.println("users count = " + count(conn, "users"));

			// tasks 件数
			System.out.println("tasks count = " + count(conn, "tasks"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static int count(Connection conn, String tableName) throws SQLException {
		String sql = "SELECT COUNT(*) FROM " + tableName;

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			rs.next();
			return rs.getInt(1);
		}
	}
}
