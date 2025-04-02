import java.time.*;
import java.io.File;

public class Printer {

	private static final String readableFormat = "[MM/DD/YYYY H:SS am/pm]";
	private static final String selectAnOptionString = "Select an option.";

// Shared messages
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void displayBadSelection() {
		System.out.println("Invalid selection.");
	}

	public static void displayBadFormat() {
		System.out.println("Incorrect format.");
	}

	/* Displays Sleeps in a list with their start and end times, with their
	    index number */
	public static void displaySleepOptions(Sleep[] sleepsList) {
		for(int index = 0; index < sleepsList.length; index++) {
			System.out.println(index
			                   + ". Start time: "
			                   + Formatter.formatData(sleepsList[index].getStartTime())
			                   + ", End time: "
			                   + Formatter.formatData(sleepsList[index].getEndTime()));
		}
	}

	public static void displayNotAnInteger() {
		System.out.println("Input must be an integer or another valid option.");
	}

// Home screen messages
	public static void displayHome() {
		System.out.println("Welcome to the Sleep Tracker!");
		System.out.println(selectAnOptionString);
		int sizeOfList = Main.listOfEntries.getSize();
		// If the object file exists, display the option to read it
		File objectFile = new File("Entries.ser");
		if(Main.listOfEntries.getSize() == 0 && objectFile.isFile()) {
			System.out.println("- " + HomeScreen.getReadFromFileKey() +
			    ": Import saved entries from file");
		}
		// If the list has one or more entries, display the option to view them
		if(sizeOfList >= 1) {
			System.out.println("- "
			                   + HomeScreen.getViewEntriesKey()
			                   + ": View entries");
		}
		// Display the option to add an entry
		System.out.println("- " + HomeScreen.getAddEntryKey() + ": Add entry");
		// If the list has two or more entries, display the option to view stats
		if(sizeOfList >= 2) {
			System.out.println("- "
			                   + HomeScreen.getViewStatsKey()
			                   + ": View statistics");
		}
		// If the list has one or more entries, display the option to save to a file
		if(sizeOfList >= 1) {
			System.out.println("- "
			+ HomeScreen.getSaveKey()
			+ ": Save to file");
		}
	}

	public static void displayError() {
		System.out.println("Error with the file.");
	}

// Add screen messages
	public static void displayAddEntry() {
		System.out.println("Add an entry:");
		System.out.println("Enter the start date in the format "
		                   + readableFormat
		                   + ".");
	}

	public static void askForEndDate() {
		System.out.println("Enter the end date.");
	}

	public static void askWhichDayToAddTo(LocalDate date) {
		System.out.println("Please select a day to add this sleep to:");
		System.out.println("1. " + Formatter.formatHeading(date));
		System.out.println("2. " + Formatter.formatHeading(date.minusDays(1)));
	}

// Entry screen messages
	public static void displayEntryScreen(Day dayToDisplay) {
		// Print a heading containing the day label
		String header = Formatter.formatHeading(dayToDisplay.getLabel());
		System.out.println(header);
		String divider = "";
		// Make a divider the length of the header
		for(int character = 0; character <= header.length(); character++) {
			divider += "-";
		}
		System.out.println(divider);
		// Display all the sleeps and their information
		for(Sleep sleepToDisplay : dayToDisplay.getSleeps()) {
			System.out.println("Start time: "
			                   + Formatter.formatData(sleepToDisplay.getStartTime()));
			System.out.println("End time: "
			                   + Formatter.formatData(sleepToDisplay.getEndTime()));
			System.out.println(String.format("Duration: %.2f hours",
			                                 sleepToDisplay.getDuration()));
			System.out.println(divider);
		}
		// Display the total duration
		System.out.println(String.format("Total duration: %.2f hours",
		                                 dayToDisplay.getTotalDuration()));
        System.out.println();
	}

	public static void displayPreviousArrow() {
		System.out.print(EntryScreen.getPreviousKey() + "\t");
	}

	public static void displayNextArrow() {
		System.out.print(EntryScreen.getNextKey() + "\t");
	}

	public static void displaySkipPreviousArrow() {
		System.out.print(EntryScreen.getSkipPreviousKey() + "\t");
	}

	public static void displaySkipNextArrow() {
		System.out.print(EntryScreen.getSkipNextKey() + "\t");
	}

	// Display the other options: edit, remove, home
	public static void displayOtherOptions() {
		System.out.print(EntryScreen.getEditKey()
		                 + " - Edit\t"
		                 + EntryScreen.getRemoveKey()
		                 + " - Remove\t"
		                 + EntryScreen.getHomeKey()
		                 + " - Home\n");
	}

// Edit screen messages
	public static void displayEdit(Day dayToDisplay) {
		System.out.println("Editing " +
		                   Formatter.formatHeading(dayToDisplay.getLabel()));
		System.out.println(selectAnOptionString);
		displaySleepOptions(dayToDisplay.getSleeps());
		System.out.println("- " + EditScreen.getCancelKey() + ": Cancel");
	}

	public static void displayEditSleep(Sleep sleepToEdit) {
		String header = Formatter.formatData(sleepToEdit.getStartTime());
		System.out.println("Editing " + header + ":");
		System.out.println(selectAnOptionString);
		System.out.println("- "
		                   + EditScreen.getEditStartTimeKey()
		                   + ": Edit start time");
		System.out.println("- "
		                   + EditScreen.getEditEndTimeKey()
		                   + ": Edit end time");
		System.out.println("- " + EditScreen.getCancelKey() + ": Cancel");
	}

	public static void displayEditStart() {
		System.out.println("Enter new start time in the format "
		                   + readableFormat
		                   + ":");
	}

	public static void displayEditEnd() {
		System.out.println("Enter new end time in the format"
		                   + readableFormat
		                   + ":");
	}

// Stats screen messages
	public static void displayStatsStart(long daysBetween) {
		System.out.println("Statistics:");
		System.out.println("Enter a duration of time in days to view statistics for, or "
		                   + StatsScreen.getCancelKey()
		                   + " to cancel.");
		System.out.println("(Max: " + daysBetween + ")");
	}

	public static void displayOperation(String operationType, String sleepTime,
	                                    String wakeTime) {
		System.out.println(operationType + " time to go to sleep: " + sleepTime);
		System.out.println(operationType + " time to wake up: " + wakeTime);
	}

	public static void displayOperationDuration(String operationType,
	        double duration) {
		String durationString = String.format("%.2f", duration);
		System.out.println(operationType + " sleep length: " + durationString);
	}

// Remove Screen messages
	public static void displayRemove(Sleep[] sleepsList) {
		System.out.println(selectAnOptionString);
		displaySleepOptions(sleepsList);
		System.out.println("- " + RemoveScreen.getAllKey() + ": Remove all");
		System.out.println("- " + RemoveScreen.getCancelKey() + ": Cancel");
	}


}