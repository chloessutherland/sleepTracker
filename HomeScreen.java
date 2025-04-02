import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class HomeScreen {

	private static final String readFromFileKey = "R";
	private static final String viewEntriesKey = "E";
	private static final String addEntryKey = "A";
	private static final String viewStatsKey = "S";
	private static final String saveKey = "F";

	public static void startHome() {
		// 1. Clear the screen
		Printer.clearScreen();
		// 2. Display the message and the options
		Printer.displayHome();
		// 3. Get the user's option selection
		getUserSelection();
	}

	private static void getUserSelection() {
		while(true) {
			String userSelection = UserInput.getRegularInput();
			userSelection = userSelection.toUpperCase();
			// Allow us to import the entries if we have a file to import from
			File objectFile = new File("Entries.ser");
			if(userSelection.equals(readFromFileKey) && Main.listOfEntries.getSize() == 0 && objectFile.isFile()) {
				try(FileInputStream fileInput = new FileInputStream("Entries.ser");
					ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			    ) {
					Main.listOfEntries = (LinkedList<Day>)objectInput.readObject();
					startHome();
					break;
				} catch(IOException e) {
					e.printStackTrace();
					break;
				} catch(ClassNotFoundException e) {
					Printer.displayError();
					break;
				}
			}
			// Allow view entries if we have any entries
			if(userSelection.equals(viewEntriesKey)
			        && Main.listOfEntries.getSize() > 0) {
				EntryScreen.startEntries(Main.listOfEntries.getFirst());
				break;
			}
			// Add entry
			if(userSelection.equals(addEntryKey)) {
				AddScreen.startAddEntry();
				break;
			}
			// Allow view stats if we have two or more entries
			if(userSelection.equals(viewStatsKey)
			        && Main.listOfEntries.getSize() > 1) {
				StatsScreen.startStats();
				break;
			}
			// Allow save if we have any entries
			if(userSelection.equals(saveKey) && Main.listOfEntries.getSize() >= 1) {
				saveToFile();
				break;
			}
			// User didn't enter a valid input
			Printer.displayBadSelection();
		}
	}

	private static void saveToFile() {
		try(FileOutputStream fileOutput = new FileOutputStream("Entries.ser");
			        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			   ) {
			objectOutput.writeObject(Main.listOfEntries);
		} catch(IOException e) {
			Printer.displayError();
		}
	}

	public static String getReadFromFileKey() {
		return readFromFileKey;
	}

	public static String getViewEntriesKey() {
		return viewEntriesKey;
	}

	public static String getAddEntryKey() {
		return addEntryKey;
	}

	public static String getViewStatsKey() {
		return viewStatsKey;
	}

	public static String getSaveKey() {
		return saveKey;
	}
}