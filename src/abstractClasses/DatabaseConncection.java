package abstractClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class DatabaseConncection {

	private Connection conn;
	private PreparedStatement preparedStatement;

	public DatabaseConncection() {

	}

	private Connection sqlConnect() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:MySql://127.0.0.1:3306/ultra_vision", "root", "");
			if (!conn.isClosed()) {
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No connection " + e.toString());
			System.exit(0);
		}
		return conn;
	}

	protected Connection getConnection() {
		sqlConnect();
		return conn;//return connection
	}

	protected void fillTable(String sql, Vector<String> heading, JTable table, DefaultTableModel model) {

		getConnection();
		try {
			preparedStatement = conn.prepareStatement(sql);//query to execute
			ResultSet rs = preparedStatement.executeQuery();// result set keeps the data that we get from the database
			Vector<Vector> data = new Vector<>();
			while (rs.next()) {
				Vector<String> row = new Vector<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)// metadata takes all the data present in
																			// the database and loop goes until the
																			// total columns
				{
					row.addElement(rs.getString(i));// adds the whole row one by one in the row vector
				}
				data.addElement(row);// adds the whole data of the row in the data vector
			}
			model.setDataVector(data, heading);// shows both data and heading both in the model by passing them in the model
			conn.close(); // close the database connection

		} catch (Exception e) {
			System.out.println("Error " + e);
		}
	}
}
