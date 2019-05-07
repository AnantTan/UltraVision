package mainFunctionalities;

import java.util.Calendar;
import java.util.Vector;

import abstractClasses.TitleToDatabaseRelation;
import interfaces.AddSubscriptionQueries;
import interfaces.TitleDetails;
import machineGUI.MachineUi;

public class ManageTitles extends TitleToDatabaseRelation implements TitleDetails, AddSubscriptionQueries {

	private static MachineUi machineUi;

	public ManageTitles() {

	}

	public Vector<String> listOfYears() {

		Vector<String> vector = new Vector<>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);// get the current year

		for (int i = currentYear; i >= 1930; i--)// will get the new year automatically
		{
			vector.add(String.valueOf(i));
		}
		return vector;
	}

	//get the following tilte details
	@Override
	public String titleType() {
		// TODO Auto-generated method stub
		machineUi = new MachineUi();
		return machineUi.getMediaType();
	}

	@Override
	public String titleName() {
		// TODO Auto-generated method stub
		return machineUi.getNameField();
	}

	@Override
	public String artist() {
		// TODO Auto-generated method stub
		return machineUi.getArtistField();
	}

	@Override
	public int yearOfRelease() {
		// TODO Auto-generated method stub
		return machineUi.getYearOfRelease();
	}

	@Override
	public String mediaFormat() {
		// TODO Auto-generated method stub
		return machineUi.getMediaFormat();
	}

	@Override
	public void newTitleDetails() {
		// TODO Auto-generated method stub
		addNewTitle(titleType(), titleName(), artist(), yearOfRelease(), mediaFormat());// add new title with these
																						// details
		machineUi.reset();// reset the boxes when title has been added
	}

	public void searchTitle() {
		machineUi = new MachineUi();
		// get the letters typed and show data according to it
		String sql = "SELECT title_type,title,artist,year_of_release,media_format,"
				+ "available FROM inventory WHERE title LIKE'" + machineUi.getNameField() + "%' ORDER BY title ASC";
		fillTable(sql, headings(), machineUi.getTable(), machineUi.getModel());// fill the table with these inputs
	}

	public void refreshTitleTable() {
		String sql = "SELECT title_type,title,artist,year_of_release,media_format,"
				+ "available FROM inventory ORDER BY title ASC";// order the names in ascending order
		machineUi = new MachineUi();
		fillTable(sql, headings(), machineUi.getTable(), machineUi.getModel());
	}

	public Vector<String> getTitle(String titleType, String freeOrRegular) {
		AddUpdateCustomer addUpdateCustomer = new AddUpdateCustomer();
		Vector<String> allTitleDetails = new Vector<String>();
		String subscription = addUpdateCustomer.getSubscription(titleType, freeOrRegular);
		// check the subscription
		switch (subscription) {
		// if premium
		case "premium":
			allTitleDetails = searchTitles(permium());
			break;
		// if video lover
		case "video_lover":
			allTitleDetails = searchTitles(videoLover());
			break;
		// if music lover
		case "music_lover":
			allTitleDetails = searchTitles(musicLover());
			break;
		// if tv lover
		case "box_set":
			allTitleDetails = searchTitles(tvLover());
			break;
		}
		return allTitleDetails;
	}

	public Vector<String> getReturnTitleDetails(String customerID) {
		return getReturnTitleID(customerID);// get the customers with this ID
	}

	private Vector<String> headings() {
		// add the heading for table
		Vector<String> heading = new Vector<>();
		heading.addElement("Type");
		heading.addElement("Title");
		heading.addElement("Artist");
		heading.addElement("Year");
		heading.addElement("Format");
		heading.addElement("Available");
		return heading;
	}

	@Override
	public String permium() {
		// TODO Auto-generated method stub
		// get all the premium customers
		String sql = "SELECT id,title,artist,year_of_release,media_format FROM inventory "
				+ "WHERE available = 'yes' ORDER BY title ASC";
		return sql;
	}

	@Override
	public String musicLover() {
		// TODO Auto-generated method stub
		// get all the music lover customers
		String sql = "SELECT id,title,artist,year_of_release,media_format FROM inventory "
				+ "WHERE available = 'yes' AND (title_type = 'music' OR title_type = 'concert')  ORDER BY title ASC";
		return sql;
	}

	@Override
	public String videoLover() {
		// TODO Auto-generated method stub
		// get all the video lover customers
		String sql = "SELECT id,title,artist,year_of_release,media_format FROM inventory "
				+ "WHERE title_type = 'video' AND available = 'yes'  ORDER BY title ASC";
		return sql;
	}

	@Override
	public String tvLover() {
		// TODO Auto-generated method stub
		// get all the tv lover customers
		String sql = "SELECT id,title,artist,year_of_release,media_format FROM inventory "
				+ "WHERE title_type = 'box_set' AND available = 'yes' ORDER BY title ASC";
		return sql;
	}
}