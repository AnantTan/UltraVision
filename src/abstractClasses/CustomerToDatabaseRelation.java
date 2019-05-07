package abstractClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

public abstract class CustomerToDatabaseRelation extends DatabaseConncection {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private int count;

	protected void newUser(String name, String subscription) {

		connection = getConnection();
		try {
			// add new customer
			preparedStatement = connection.prepareStatement("INSERT INTO customer VALUES(?,?,?,0)");
			preparedStatement.setInt(1, 0);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, subscription);
			count = preparedStatement.executeUpdate();
			// if added successfully show a message
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "Customer added successfully");
				showUniqueNum();// give this id to customers. they need this to make and return rentals
				connection.close();// close the connection when not in use
			}
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	protected void showUniqueNum() {
		try {
			preparedStatement = connection
					.prepareStatement("SELECT unique_num FROM customer getLastRecord ORDER BY unique_num DESC LIMIT 1");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				JOptionPane.showMessageDialog(null, "Give this ID to customer =" + resultSet.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	protected Vector<String> populateCustomerComboBox(String freeOrRegular) {

		connection = getConnection();

		Vector<String> customer = new Vector<String>();
		try {
			// if free show the customer having points more than or = 100
			// if not show all the customers
			if (freeOrRegular.equals("free")) {
				preparedStatement = connection
						.prepareStatement("SELECT unique_num,name FROM customer WHERE points >= 100 ORDER BY name ASC");
			} else {
				preparedStatement = connection
						.prepareStatement("SELECT unique_num,name FROM customer ORDER BY name ASC");
			}
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				customer.add(resultSet.getString(1).concat(" ").concat(resultSet.getString(2)));
			}
			connection.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
		return customer;
	}

	protected String getPersonalDetails(String selectCustomer) {

		connection = getConnection();// get connection

		String details = null;
		try {
			// extract customers id and find details at that id
			String a[] = selectCustomer.split("-");
			preparedStatement = connection
					.prepareStatement("SELECT name,subscription FROM  customer WHERE unique_num=?");
			preparedStatement.setString(1, a[0]);// where unique_num = ?
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				details = rs.getString(1).concat(" ").concat(rs.getString(2));
			}
			connection.close();// close the connection
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
		return details;
	}

	protected void updatePersonalDetails(String customerNum, String name, String subscription) {

		connection = getConnection();// get connection

		String id = customerNum.substring(0, customerNum.indexOf(" "));// takes the id only
		try {
			// takes the new details and updates them
			PreparedStatement preparedStatement = connection
					.prepareStatement("UPDATE customer SET name=?,subscription=? WHERE unique_num=" + id + "");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, subscription);
			count = preparedStatement.executeUpdate();
			// if updated show a message
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "Customer updated successfully");
				connection.close();// close the connection
			}
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	protected String customerSubscription(String customer, String freeOrRegular) {

		connection = getConnection();

		String subscription = null;
		String a[] = customer.split("-");
		// if the customer is eligible for free show all the titles
		if (freeOrRegular.equals("free")) {
			return subscription = "premium";
		} else {// show according to the subscription
			try {
				preparedStatement = connection
						.prepareStatement("SELECT subscription FROM  customer WHERE unique_num=?");
				preparedStatement.setString(1, a[0]);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					subscription = resultSet.getString(1);
				}
				connection.close();//close the connection
			} catch (SQLException e) {
				System.out.println("Error " + e);
			}
		}
		return subscription;
	}

	protected Vector<String> getCustomerRentID() {
		
		Vector<String> details = new Vector<>();
		connection = getConnection();//get connection
		try {
			//show the customers that have rented something
			preparedStatement = connection.prepareStatement("SELECT DISTINCT rented_by FROM rent");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				details.add(resultSet.getString(1));
			}
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
		return details;
	}
}
