package mainFunctionalities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import machineGUI.MachineUi;

public class ActionOfButtons implements ActionListener, KeyListener {

	private MachineUi machineUi;
	private AddUpdateCustomer addUpdateCustomer;
	private ManageTitles manageTitles;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("newCustomer")) {
			machineUi = new MachineUi();
			machineUi.newCustomer();
		} else if (e.getActionCommand().equals("AddCustomer")) {
			// check details before updating
			// if all good proceed otherwise stop
			machineUi = new MachineUi();
			if (!checkDetails(machineUi.getNameField())) {
				JOptionPane.showMessageDialog(null, "Name cannot be empty", "Missing Name", JOptionPane.ERROR_MESSAGE);
				return;// if error do not proceed
			} else {
				addUpdateCustomer = new AddUpdateCustomer();
				addUpdateCustomer.addNewCustomer();
			}
		} else if (e.getActionCommand().equals("searchCustomer")) {
			machineUi = new MachineUi();
			machineUi.searchCustomer();
		} else if (e.getActionCommand().equals("refreshCustomerTable")) {
			addUpdateCustomer = new AddUpdateCustomer();
			addUpdateCustomer.refreshCustomerTable();
		} else if (e.getActionCommand().equals("customerSelected")) {
			addUpdateCustomer = new AddUpdateCustomer();
			addUpdateCustomer.personalDetails();
		} else if (e.getActionCommand().equals("updateCustomer")) {
			machineUi = new MachineUi();
			machineUi.updateCustomer();
		} else if (e.getActionCommand().equals("updatePersonalDetails")) {
			// check details before updating
			// if all good proceed otherwise stop
			machineUi = new MachineUi();
			if (!checkDetails(machineUi.getNameField())) {
				JOptionPane.showMessageDialog(null, "Please check the name!", "Name not valid",
						JOptionPane.ERROR_MESSAGE);
				return;// if error do not proceed
			} else {
				addUpdateCustomer = new AddUpdateCustomer();
				addUpdateCustomer.updatePersonalDetails();
			}
		} else if (e.getActionCommand().equals("addNewTitle")) {
			machineUi = new MachineUi();
			machineUi.addTitle();
		} else if (e.getActionCommand().equals("newTitle")) {
			// check details before updating
			// if all good proceed otherwise stop
			machineUi = new MachineUi();
			if (!checkDetails(machineUi.getNameField(), machineUi.getArtistField())) {
				JOptionPane.showMessageDialog(null, "Please check the details!", "Invalid Details",
						JOptionPane.ERROR_MESSAGE);
				return;// if error do not proceed
			} else {
				manageTitles = new ManageTitles();
				manageTitles.newTitleDetails();
			}
		} else if (e.getActionCommand().equals("searchTitle")) {
			machineUi = new MachineUi();
			machineUi.searchTitle();
		} else if (e.getActionCommand().equals("refreshTitleTable")) {
			manageTitles = new ManageTitles();
			manageTitles.refreshTitleTable();
		} else if (e.getActionCommand().equals("newRent")) {
			machineUi = new MachineUi();
			machineUi.rental("");// proceed as regular rent using same methods
		} else if (e.getActionCommand().equals("getTitles")) {
			machineUi = new MachineUi();
			machineUi.titles();
		} else if (e.getActionCommand().equals("rentTitle")) {
			ManageRental manageRental = new ManageRental();
			machineUi = new MachineUi();
			//check the selection before making the rental
			if(machineUi.getRentalDetails().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Please select something", "Check details", JOptionPane.ERROR_MESSAGE);
			}
			else
			manageRental.makeRent();
		} else if (e.getActionCommand().equals("freeRental")) {
			machineUi = new MachineUi();
			machineUi.rental("free");// if it is a free rental proceed as free rental customer using same methods
		} else if (e.getActionCommand().equals("returnRental")) {
			machineUi = new MachineUi();
			machineUi.rental("return");// proceed as return procedure using same methods
		} else if (e.getActionCommand().equals("returnRent")) {
			ManageRental manageRental = new ManageRental();
			manageRental.returnRental();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		machineUi = new MachineUi();
		if (machineUi.getCustomerOrTitle().equals("customer"))// if user look for customers
		{
			addUpdateCustomer = new AddUpdateCustomer();
			addUpdateCustomer.searchCustomer();
		} else// look for titles
		{
			manageTitles = new ManageTitles();
			manageTitles.searchTitle();// look for titles
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	private boolean checkDetails(String name) {

		if (name.matches("[A-Z a-z]+"))// name can have only letters
		{
			return true;
		} else
			return false;
	}

	private boolean checkDetails(String name, String artist) {
		if (name.matches("[A-Za-z 0-9]+") && artist.matches("[A-Za-z 0-9]+"))// title can have letters and numbers
		{
			return true;
		} else
			return false;
	}
}
