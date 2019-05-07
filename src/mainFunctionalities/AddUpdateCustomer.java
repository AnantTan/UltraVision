package mainFunctionalities;

import java.util.Vector;

import abstractClasses.CustomerToDatabaseRelation;
import machineGUI.MachineUi;

public class AddUpdateCustomer extends CustomerToDatabaseRelation {

	private MachineUi machineUi;

	public AddUpdateCustomer() {

	}

	public void addNewCustomer() {
		// add new customer
		machineUi = new MachineUi();
		newUser(machineUi.getNameField(), machineUi.getSubscription());
		machineUi.reset();// clears the text fields
	}

	public void searchCustomer() {
		// search for a customer
		machineUi = new MachineUi();
		String sql = "SELECT * FROM customer WHERE name LIKE'" + machineUi.getNameField() + "%' ORDER BY name ASC";// search
																													// using
																													// first
																													// letters
		fillTable(sql, headings(), machineUi.getTable(), machineUi.getModel());
	}

	public void refreshCustomerTable() {
		// refresh the whole table
		String sql = "SELECT * FROM customer ORDER BY name ASC";// order the names in alphabetical order
		machineUi = new MachineUi();
		fillTable(sql, headings(), machineUi.getTable(), machineUi.getModel());
	}

	public String getSubscription(String customer, String freeOrRegular) {
		return customerSubscription(customer, freeOrRegular);// gives out the customer subscription
	}

	private Vector<String> headings() {
		// heading for the table
		Vector<String> heading = new Vector<>();
		heading.addElement("Unique Number");
		heading.addElement("Name");
		heading.addElement("Subscription");
		heading.addElement("Points");
		return heading;
	}

	public Vector<String> customerDetails(String freeOrRegular) {
		return populateCustomerComboBox(freeOrRegular);//get or the customer details
	}

	public Vector<String> customerRentID() {
		return getCustomerRentID();//gives out the customer id
	}

	public void personalDetails() {
		
		machineUi = new MachineUi();
		String details = getPersonalDetails(machineUi.getCustomer());
		machineUi.setNameField(details.substring(0, details.lastIndexOf(" ")));//set the name in the combo box which is selected
		machineUi.setSubscription(details.substring(details.lastIndexOf(" ") + 1, details.length()));// sets the subscription in combo box if need to be updated
	}

	public void updatePersonalDetails() {
	
		machineUi = new MachineUi();
		updatePersonalDetails(machineUi.getCustomer(), machineUi.getNameField(), machineUi.getSubscription());//update the details
		machineUi.updateCustomer();// refresh the frame when updated
	}
}