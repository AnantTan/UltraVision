package machineGUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import enumerator.MediaFormat;
import enumerator.MediaType;
import enumerator.Subscriptions;
import mainFunctionalities.ActionOfButtons;
import mainFunctionalities.AddUpdateCustomer;
import mainFunctionalities.ManageTitles;

public class MachineUi {

	private static final JFrame frame = new JFrame("Ultra Vision");
	private ActionOfButtons actionOfButtons;
	private AddUpdateCustomer addUpdateCustomer;

	private JMenuBar menuBar = new JMenuBar();
	private JMenu titles = new JMenu("Titles");
	private JMenu customer = new JMenu("Customer");
	private JMenu rental = new JMenu("Rental");
	private JMenu date;

	private JMenuItem addnewCustomer = new JMenuItem("Add New Customer");
	private JMenuItem searchCustomer = new JMenuItem("Search Customer");
	private JMenuItem updateCustomer = new JMenuItem("Update Customer");

	private JMenuItem addNewTitle = new JMenuItem("Add New Title");
	private JMenuItem lookForTitle = new JMenuItem("Look For Title");

	private JMenuItem newRental = new JMenuItem("New Rental");
	private JMenuItem retunRental = new JMenuItem("Return Rental");
	private JMenuItem freeRental = new JMenuItem("Free Rental");

	private static JTextField artistField;
	private static JTextField nameField;

	private static JComboBox<Vector<String>> customerSeachBox;
	private static JComboBox<Vector<String>> titleSearchBox;
	private static JComboBox subscriptionBox;
	private static JComboBox mediaTypeBox;
	private static JComboBox<Vector<String>> yearOfReleaseBox;
	private static JComboBox mediaFormatBox;
	private static JComboBox<Vector<String>> priceBox;

	private JButton add = new JButton("Add");

	private static DefaultTableModel model;
	private static JTable table;

	private static JPanel panel;
	private static JPanel panelOne;

	private Vector<String> years = new Vector<>();

	private static int count;
	private static int i = 0, j = 0;
	private static String customerOrTitle, freeOrRegular;

	public MachineUi() {
		actionOfButtons = new ActionOfButtons();
	}

	public void frontPage() {
		frame.setTitle("Ultra Vision");
		frame.setSize(650, 600);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);// terminate when closed
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);// Ui should come in the center

		customer.add(addnewCustomer);
		addnewCustomer.addActionListener(actionOfButtons);
		addnewCustomer.setActionCommand("newCustomer");
		customer.add(searchCustomer);
		searchCustomer.addActionListener(actionOfButtons);
		searchCustomer.setActionCommand("searchCustomer");
		customer.add(updateCustomer);
		updateCustomer.addActionListener(actionOfButtons);
		updateCustomer.setActionCommand("updateCustomer");

		titles.add(addNewTitle);
		addNewTitle.addActionListener(actionOfButtons);
		addNewTitle.setActionCommand("addNewTitle");
		titles.add(lookForTitle);
		lookForTitle.addActionListener(actionOfButtons);
		lookForTitle.setActionCommand("searchTitle");

		rental.add(newRental);
		newRental.addActionListener(actionOfButtons);
		newRental.setActionCommand("newRent");
		rental.add(freeRental);
		freeRental.addActionListener(actionOfButtons);
		freeRental.setActionCommand("freeRental");
		rental.add(retunRental);
		retunRental.addActionListener(actionOfButtons);
		retunRental.setActionCommand("returnRental");

		menuBar.add(customer);
		menuBar.add(rental);
		menuBar.add(titles);
		menuBar.add(Box.createHorizontalGlue());// sticking to the right
		date = new JMenu((String.valueOf(getTodaysDate())));// show current date
		menuBar.add(date);

		frame.setJMenuBar(menuBar);// set the menu bar
		frame.revalidate();
	}

	public void searchCustomer() {
		model = new DefaultTableModel();
		// make up the column headings for the JTable
		model.addColumn("Unique Number");
		model.addColumn("Name");
		model.addColumn("Subscription");
		model.addColumn("Points");
		table = new JTable(model);// model to the table

		nameField = new JTextField();
		nameField.addKeyListener(actionOfButtons);
		JButton refresh = new JButton("Refresh");
		setCustomerOrTitle("customer");// same text field and key listener is used to search titles, so to
										// differentiate
		refresh.addActionListener(actionOfButtons);
		refresh.setActionCommand("refreshCustomerTable");

		JPanel panel = new JPanel(new GridLayout(1, 1, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Person Details"));
		panel.add(new JScrollPane(table));// make the table scroll
		table.disable();// table should not be edited

		JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));// setting up rows,column and gaps
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		buttonPanel.add(refresh);
		buttonPanel.add(new JLabel());
		buttonPanel.add(nameField);

		frame.getContentPane().removeAll();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.getContentPane().revalidate();
	}

	public void newCustomer() {
		JPanel panel = new JPanel(new GridLayout(2, 2));
		JPanel buttonPanel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));// create border
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));// create border
		JLabel fullNameLabel = new JLabel("Full Name");
		JLabel subscriptionLabel = new JLabel("Subscription");
		nameField = new JTextField();
		artistField = new JTextField();// (patch) avoid null pointer at reset

		subscriptionBox = new JComboBox(Subscriptions.values());
		panel.add(fullNameLabel);
		panel.add(nameField);
		panel.add(subscriptionLabel);
		panel.add(subscriptionBox);
		buttonPanel.add(add);
		add.addActionListener(actionOfButtons);
		add.setActionCommand("AddCustomer");

		frame.getContentPane().removeAll();
		frame.getContentPane().add(panel, BorderLayout.NORTH);// allocate the panel at north of the frame
		frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);// allocate the panel at the center of the frame
		frame.getContentPane().revalidate();// refresh the content pane
	}

	public void updateCustomer() {
		JPanel boxPanel = new JPanel(new GridLayout(1, 1, 0, 0));
		boxPanel.setBorder(BorderFactory.createTitledBorder("Select Person"));
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.setBorder(BorderFactory.createTitledBorder("Update Details"));
		JPanel buttonPanel = new JPanel();
		JLabel name = new JLabel("Name");
		JLabel subscription = new JLabel("Subscription");
		JButton update = new JButton("Update");
		update.addActionListener(actionOfButtons);
		update.setActionCommand("updatePersonalDetails");
		addUpdateCustomer = new AddUpdateCustomer();
		customerSeachBox = new JComboBox(addUpdateCustomer.customerDetails(""));// get all the customer details
		customerSeachBox.addActionListener(actionOfButtons);
		customerSeachBox.setActionCommand("customerSelected");
		nameField = new JTextField();
		subscriptionBox = new JComboBox(Subscriptions.values());// store all the values from the enumerator class

		boxPanel.add(customerSeachBox);
		panel.add(name);
		panel.add(nameField);
		panel.add(subscription);
		panel.add(subscriptionBox);
		buttonPanel.add(update);

		frame.getContentPane().removeAll();
		frame.getContentPane().add(boxPanel, BorderLayout.NORTH);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.getContentPane().revalidate();
	}

	public void addTitle() {
		ManageTitles manageTitles;
		JPanel panel = new JPanel(new GridLayout(5, 5));
		JPanel buttonPanel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Set Details"));// creating a titled border

		JLabel type = new JLabel("Type");
		JLabel title = new JLabel("Title");
		JLabel artist = new JLabel("Artist");
		JLabel year = new JLabel("Year of release");
		JLabel format = new JLabel("Format");
		JButton add = new JButton("Add");
		add.addActionListener(actionOfButtons);
		add.setActionCommand("newTitle");
		mediaTypeBox = new JComboBox(MediaType.values());// get all the values from the enumerator class
		nameField = new JTextField();
		artistField = new JTextField();
		// if global variable loaded once do not load it again
		// do not go in manage titles class
		if (count == 0) {
			manageTitles = new ManageTitles();
			years = manageTitles.listOfYears();
			yearOfReleaseBox = new JComboBox(years);
			count++;
		} else {
			yearOfReleaseBox = new JComboBox(years);
		}
		mediaFormatBox = new JComboBox(MediaFormat.values());

		panel.add(type);
		panel.add(mediaTypeBox);
		panel.add(title);
		panel.add(nameField);
		panel.add(artist);
		panel.add(artistField);
		panel.add(year);
		panel.add(yearOfReleaseBox);
		panel.add(format);
		panel.add(mediaFormatBox);
		buttonPanel.add(add);

		frame.getContentPane().removeAll();// remove everything in the content pane
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
		frame.getContentPane().revalidate();// refresh the content pane
	}

	public void searchTitle() {
		model = new DefaultTableModel();
		// add column titles to the table
		model.addColumn("Type");
		model.addColumn("Title");
		model.addColumn("Artist");
		model.addColumn("Year");
		model.addColumn("Format");
		model.addColumn("Available");
		table = new JTable(model);// add the table to the model

		nameField = new JTextField();
		setCustomerOrTitle("title");
		nameField.addKeyListener(actionOfButtons);// same text field and key listener is used to search customer, so to
													// differentiate
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(actionOfButtons);
		refresh.setActionCommand("refreshTitleTable");

		JPanel panel = new JPanel(new GridLayout(1, 1, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Title Details"));
		panel.add(new JScrollPane(table));
		table.disable();// table should be non editable

		JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		buttonPanel.add(refresh);
		buttonPanel.add(new JLabel());
		buttonPanel.add(nameField);

		frame.getContentPane().removeAll();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.getContentPane().revalidate();
	}

	public void rental(String freeOrRegular) {
		JPanel panel = new JPanel(new GridLayout(1, 1, 10, 10));
		panel.setBorder(BorderFactory.createTitledBorder("Rent Title"));
		JLabel selectCustomer = new JLabel("Select Customer:");
		AddUpdateCustomer addUpdateCustomer = new AddUpdateCustomer();
		this.freeOrRegular = freeOrRegular;
		// check if the method is being called from free rental perspective
		if (this.freeOrRegular.equals("free")) {
			// if yes load only the customer eligible to make a free rent
			customerSeachBox = new JComboBox(addUpdateCustomer.customerDetails(freeOrRegular));
		} else if (this.freeOrRegular.equals("return")) {
			// if return load only the customers that have some rents
			customerSeachBox = new JComboBox(addUpdateCustomer.customerRentID());
		} else {
			// else to make the rent load all the customers
			customerSeachBox = new JComboBox(addUpdateCustomer.customerDetails(freeOrRegular));
		}
		customerSeachBox.addActionListener(actionOfButtons);
		customerSeachBox.setActionCommand("getTitles");

		panel.add(selectCustomer);
		panel.add(customerSeachBox);

		frame.getContentPane().removeAll();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.getContentPane().revalidate();
	}

	public void titles() {
		// need to update the combo box and I did not find a direct way therefore a
		// little bit of bad coding
		// just to make the window appear live
		if (i == 0) {
			if (j == 1) {
				panel.setVisible(false);// do not show the old panel
				j = 0;
			}
			i = 1;// setting up a flag
			ManageTitles manageTitles = new ManageTitles();
			if (freeOrRegular.equals("return")) {
				// if return load the titles rented by a particular customer
				titleSearchBox = new JComboBox(manageTitles.getReturnTitleDetails(getCustomer()));
				panelOne = new JPanel();
				JButton rent = new JButton("Return");
				rent.addActionListener(actionOfButtons);
				rent.setActionCommand("returnRent");
				panelOne.add(titleSearchBox);
				panelOne.add(rent);
			} else {
				// else get all the titles on subscription to make a rent
				titleSearchBox = new JComboBox(manageTitles.getTitle(getCustomer(), freeOrRegular));
				panelOne = new JPanel();
				JButton rent = new JButton("Rent");
				rent.addActionListener(actionOfButtons);
				rent.setActionCommand("rentTitle");
				panelOne.add(titleSearchBox);
				panelOne.add(rent);
			}
			frame.getContentPane().add(panelOne);
			frame.getContentPane().revalidate();
			panelOne.setVisible(true);
		} else
			refreshComboBox(freeOrRegular);
	}

	private void refreshComboBox(String freeOrRegular) {
		// no direct way to update the combo box
		// remove the old one
		// place the new one
		panelOne.setVisible(false);// make the current panel invisible
		j = 1;
		ManageTitles manageTitles = new ManageTitles();
		if (freeOrRegular.equals("return")) {
			// if return load the titles rented by a particular customer
			titleSearchBox = new JComboBox(manageTitles.getReturnTitleDetails(getCustomer()));
			panel = new JPanel();
			JButton rent = new JButton("Retrun");
			rent.addActionListener(actionOfButtons);
			rent.setActionCommand("returnRent");
			panel.add(titleSearchBox);
			panel.add(rent);
		} else {
			titleSearchBox = new JComboBox(manageTitles.getTitle(getCustomer(), freeOrRegular));
			panel = new JPanel();
			JButton rent = new JButton("Rent");
			rent.addActionListener(actionOfButtons);
			rent.setActionCommand("rentTitle");
			panel.add(titleSearchBox);
			panel.add(rent);
		}
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		i = 0;
		frame.getContentPane().revalidate();
	}

	public String getTodaysDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");// setting up the format for the date
		Date date = new Date();// get todays date
		return formatter.format(date);// set the date in the provided format
	}

	public String getNameField() {
		nameField.setText(nameField.getText().trim());// remove any spaces
		return nameField.getText().toString();
	}

	public void setNameField(String text) {
		nameField.setText(text);
	}

	public String getArtistField() {
		return artistField.getText().toString();
	}

	public String getCustomer() {
		return customerSeachBox.getSelectedItem().toString();
	}

	public void setSubscription(String subscription) {
		subscriptionBox.setSelectedItem(Subscriptions.valueOf(subscription.toUpperCase()));
	}

	public String getSubscription() {
		return subscriptionBox.getSelectedItem().toString();
	}

	public String getMediaType() {
		return mediaTypeBox.getSelectedItem().toString();
	}

	public int getYearOfRelease() {
		return Integer.parseInt(yearOfReleaseBox.getSelectedItem().toString());
	}

	public double getPrice() {
		return Double.parseDouble(priceBox.getSelectedItem().toString());
	}

	public String getMediaFormat() {
		return mediaFormatBox.getSelectedItem().toString();
	}

	public JTable getTable() {
		return table;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	private void setCustomerOrTitle(String customerOrTitle) {
		this.customerOrTitle = customerOrTitle;
	}

	public String getCustomerOrTitle() {
		return customerOrTitle;
	}

	public String getRentalDetails() {
		if(titleSearchBox.getItemCount()==0)
		{
			return "";//if combo box is blank anything return empty
		}
		else
		return titleSearchBox.getSelectedItem().toString();
	}

	public String getFreeOrRegular() {
		return freeOrRegular;
	}

	public void reset() {
		nameField.setText("");
		artistField.setText("");
	}
}
