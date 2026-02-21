package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/taskmanager" + "?useSSL=false"
			+ "&characterEncoding=utf8" + "&useUnicode=true" + "&serverTimezone=Asia/Tokyo";

	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = ""; // ← 空にした

	private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

	static {
		try {
			Class.forName(DB_DRIVER);
			System.out.println("[DB] MySQL Driver loaded");
		} catch (ClassNotFoundException e) {
			System.err.println("[DB] Driver not found");
			throw new RuntimeException(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		System.out.println("[DB] Connecting to MySQL...");

		Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

		System.out.println("[DB] Connection successful");
		System.out.println("[DB] url=" + conn.getMetaData().getURL());
		System.out.println("[DB] catalog=" + conn.getCatalog());

		return conn;
	}

}
