package abstractClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

public abstract class TitleToDatabaseRelation extends DatabaseConncection {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private int count;

	protected void addNewTitle(String type, String title, String artisit, int year, String format) {

		connection = getConnection();// get the connection from the database
		try {
			// add the customer to database
			preparedStatement = connection.prepareStatement("INSERT INTO inventory VALUES(?,?,?,?,?,?,'yes')");
			preparedStatement.setInt(1, 0);
			preparedStatement.setString(2, type);
			preparedStatement.setString(3, title);
			preparedStatement.setString(4, artisit);
			preparedStatement.setInt(5, year);
			preparedStatement.setString(6, format);
			count = preparedStatement.executeUpdate();
			// if update successful show some message
			if (count > 0) {
				JOptionPane.showMessageDialog(null, "Title added successfully");
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	protected Vector<String> searchTitles(String sql) {

		connection = getConnection();// get connection
		Vector<String> titleDetails = new Vector<String>();
		try {
			preparedStatement = connection.prepareStatement(sql);// get the query for search titles
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				titleDetails.add("(ID)-" + resultSet.getInt(1) + " (Title)-"
						+ resultSet.getString(2).concat(" (Artist)-" + resultSet.getString(3).concat(
								" (Year)-" + resultSet.getString(4).concat(" (Format)-" + resultSet.getString(5)))));
			}
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
		return titleDetails;
	}

	protected void makeRent(int customerID, int rentID, String freeOrRegular) {
		connection = getConnection();
		Date now = new Date();// to get rented date
		try {
			// update that a rent has been made
			preparedStatement = connection.prepareStatement("INSERT INTO rent VALUES(?,?,?,'no')");
			preparedStatement.setInt(1, customerID);
			preparedStatement.setInt(2, rentID);
			preparedStatement.setDate(3, new java.sql.Date(now.getTime()));// get and pass todays date
			count = preparedStatement.executeUpdate();
			// if update is successful
			// make the title not available
			// add the points to customer
			if (count > 0) {
				makeUnavailableOrAvailable("no", rentID);
				// if free mark it that no rent should be charged
				if (freeOrRegular.equals("free")) {
					thisIsFree(rentID);// mark it
				}
				checkAndAddPoints(customerID, freeOrRegular);
				JOptionPane.showMessageDialog(null, "Issued successfully");
				connection.close();
			}

		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	private void thisIsFree(int rentID) {
		// set it as free
		try {
			preparedStatement = connection.prepareStatement("UPDATE rent SET free = 'yes' WHERE rented = " + rentID);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	private void makeUnavailableOrAvailable(String yesOrNo, int rentID) {
		try {
			// make the title at this id either available or not
			preparedStatement = connection
					.prepareStatement("UPDATE inventory SET available = '" + yesOrNo + "' WHERE id = " + rentID);
			preparedStatement.executeUpdate();// execute this update to inventory
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	private void checkAndAddPoints(int customerID, String freeOrRegular) {
		try {
			int updatePoints = 0;
			// get the points where customer id is this
			preparedStatement = connection
					.prepareStatement("SELECT points FROM customer WHERE unique_num = '" + customerID + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				// add 10 points if normal rent
				// reduce 100 if free rental
				// else keep them
				if (resultSet.getInt(1) > 100 && freeOrRegular.equals("free")) {
					updatePoints = resultSet.getInt(1) - 100;// reduce 100 points
				} else
					updatePoints = resultSet.getInt(1) + 10;// add 10 points
			}
			// update customers's points
			preparedStatement = connection.prepareStatement(
					"UPDATE customer SET points = " + updatePoints + " WHERE unique_num = " + customerID);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	protected Vector<String> getReturnTitleID(String customerID) {

		connection = getConnection();// get connection
		Vector<String> details = new Vector<>();
		try {
			// get this customer id who has some rent already
			preparedStatement = connection
					.prepareStatement("SELECT rented,date FROM rent WHERE rented_by = '" + customerID + "'");
			ResultSet resultSet = preparedStatement.executeQuery();
			// get the rented title id and the date at which it was rented
			while (resultSet.next()) {
				details.add(resultSet.getString(1).concat(" (Date)-" + resultSet.getString(2)));
			}
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
		return details;
	}

	protected void returnRent(int customerID, int rentID) {

		connection = getConnection();// get connection
		long amountDue = 0;
		// get the rental date for this particular rent (rent ID)
		try {
			preparedStatement = connection.prepareStatement("SELECT date,free FROM rent WHERE rented = " + rentID);
			ResultSet resultSet = preparedStatement.executeQuery();
			// get the date and check the amount due
			if (resultSet.next()) {
				if (resultSet.getString(2).equals("yes")) {
					//it is free no money is to be charged
				} else {
					amountDue = getMoneyDue(resultSet.getDate(1));
				}
			}
			// remove the particular rent from the database
			preparedStatement = connection.prepareStatement("DELETE FROM rent WHERE rented = " + rentID);
			count = preparedStatement.executeUpdate();
			// make it available in the inventory as well
			if (count > 0) {
				makeUnavailableOrAvailable("yes", rentID);
				JOptionPane.showMessageDialog(null, "Amount due is = €" + amountDue);
			}
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}

	private long getMoneyDue(Date dateToConvert) {

		// get the date from the database and convert it local date to do the conversion
		LocalDate rentedDate = Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault())
				.toLocalDate();
		LocalDate returnDate = LocalDate.now();// return date to compare
		long numberOfDays = ChronoUnit.DAYS.between(rentedDate, returnDate);// find the days in between rent and return
		if (numberOfDays > 3) {
			return 6 + (numberOfDays - 3);// if more than 3 charge 6 + number of days over it as penalty
		} else if (numberOfDays == 0) {
			return 1;// if returned on the same day charge 1 euro
		} else
			return numberOfDays * 2;// if returned before 3 days or on third day charge days*2
	}
}
