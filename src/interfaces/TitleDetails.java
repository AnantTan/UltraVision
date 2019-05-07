package interfaces;

public interface TitleDetails {
	
	//details that all titles will have
	String titleType();
	String titleName();
	int yearOfRelease();
	String mediaFormat();
	String artist();
	void newTitleDetails();
}
