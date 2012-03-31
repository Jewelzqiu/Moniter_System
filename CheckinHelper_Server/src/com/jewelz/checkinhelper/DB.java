package com.jewelz.checkinhelper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {

	static final String DB_NAME = "radlab";
	static final String TABLE_NAME = "radlab";
	static final String PASSWORD = "admin";
	static String url =
			"jdbc:mysql://localhost:3306/radlab?user=root&password=admin";
	Connection connection;
	Statement statement;

	public void checkIn(long militime, ArrayList<String> names) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = java.sql.DriverManager.getConnection(url);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String cdate = new Date(militime).toString();
		String time = String.format("%tR", militime);

		for (String name : names) {
			System.out.println("name: " + name);
			try {
				ResultSet result = statement
						.executeQuery("select * from radlab where cdate='"
								+ cdate + "' and name='" + name + "'");
				if (result.next()) {
					System.out.println(result.getString("cdate") + "\t"
							+ result.getString("name") + "\t"
							+ result.getString("intime") + "\t"
							+ result.getString("outtime") + "\n");

					statement.executeUpdate("update radlab set outtime='"
							+ time + "' where cdate='" + cdate + "' and name='"
							+ name + "'");
				} else {
					statement.execute("insert into radlab (cdate,name,intime)"
							+ " values('" + cdate + "','" + name + "','"
							+ time + "')");
				}

				result = statement.executeQuery("select * from radlab");
				System.out.println("Date\t\tName\tIn\tOut");
				while (result.next()) {
					System.out.println(result.getString("cdate") + "\t"
							+ result.getString("name") + "\t"
							+ result.getString("intime") + "\t"
							+ result.getString("outtime"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DB db = new DB();
		ArrayList<String> names = new ArrayList<String>();
		names.add("qiu");
		db.checkIn(System.currentTimeMillis(), names);
	}

}
