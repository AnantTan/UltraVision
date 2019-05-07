package mainFunctionalities;

import abstractClasses.TitleToDatabaseRelation;
import interfaces.RentalDetails;
import machineGUI.MachineUi;

public class ManageRental extends TitleToDatabaseRelation implements RentalDetails {
	
	private MachineUi machineUi;
	private int customerID,rentID;
	
	public ManageRental()
	{
		
	}
	
	public void makeRent()
	{
		//get the details and make rent
		customerID= Integer.parseInt(customerDetails().substring(0, customerDetails().indexOf(" ")));
		rentID = Integer.parseInt(rentDetails().substring(rentDetails().indexOf("-")+1, rentDetails().indexOf(" ")));
		makeRent(customerID, rentID,freeOrRegular());
		machineUi = new MachineUi();
		machineUi.rental(freeOrRegular());//refresh the frame after rent has been made
	}
	
	public void returnRental()
	{
		//get the details and return the title
		customerID= Integer.parseInt(customerDetails());
		rentID = Integer.parseInt(rentDetails().substring(0, rentDetails().indexOf(" ")));
		returnRent(customerID,rentID);
		machineUi = new MachineUi();
		machineUi.rental(freeOrRegular());// refresh the frame after title has been returned
	}

	//get the details
	@Override
	public String customerDetails() {
		// TODO Auto-generated method stub
		machineUi = new MachineUi();
		return machineUi.getCustomer();
	}

	@Override
	public String rentDetails() {
		// TODO Auto-generated method stub
		return machineUi.getRentalDetails();
	}

	@Override
	public String freeOrRegular() {
		// TODO Auto-generated method stub
		return machineUi.getFreeOrRegular();
	}
}
