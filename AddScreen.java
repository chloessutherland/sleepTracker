import java.time.format.DateTimeParseException;
import java.time.*;

public class AddScreen {

	public static void startAddEntry() {
		// 1. Clear the screen
		Printer.clearScreen();
		// 2. Display the message
		Printer.displayAddEntry();
		// 3. Try to get a start time from the user
		LocalDateTime startTime = getUserDate();
		// 4. Display further message
		Printer.askForEndDate();
		// 5. Try to get an end time from the user
		LocalDateTime endTime = getUserDate();
		// 8. Go back to the entries with the new node that was added
		EntryScreen.startEntries(makeNewSleepNode(startTime, endTime));
	}

	public static LocalDateTime getUserDate() {
		while(true) {
			try {
				return UserInput.getDateTimeFromUser();
				/* If the user doesn't enter a date and time in the correct 
				format */
			} catch(DateTimeParseException badInputException) {
				Printer.displayBadFormat();
			}
		}
	}

	/* Add the sleep to either an existing node or a new node and return that
	node */
	public static LinkedList<Day>.Node addSleep(Sleep newSleep) {
		// Get the data we need
		LocalDateTime startTime = newSleep.getStartTime();
		LocalDate sleepDate = startTime.toLocalDate();
		String sleepTime = Formatter.formatData(startTime);
		// Check if the start time is AM or PM
		String amPM = sleepTime.substring(sleepTime.length() - 2);
		boolean isAM = amPM.equals("AM");
		/* If the start time is AM, ask which date we want to add to: current
		or day before */
		if(isAM) {
			sleepDate = checkWhichDayToAddTo(sleepDate);
		}
		// If this is the first node, simply add to front
		if(Main.listOfEntries.getSize() == 0) {
			Day newDay = new Day(sleepDate, newSleep);
			Main.listOfEntries.addToFront(newDay);
			return Main.listOfEntries.getFirst();
		}
		// Traverse through the linked list if this isn't the first node
		LinkedList<Day>.Node potentialNode = addToLinkedList(sleepDate,
		                                     newSleep);
		if(potentialNode != null) {
			return potentialNode;
		}
		// If we get here, no node was after so we add a new node to the back
		Day newDay = new Day(sleepDate, newSleep);
		Main.listOfEntries.addToBack(newDay);
		return Main.listOfEntries.getLast();
	}

// Check if we want to add to the current date or the previous date
	private static LocalDate checkWhichDayToAddTo(LocalDate sleepDate) {
		Printer.askWhichDayToAddTo(sleepDate);
		while(true) {
			String userSelection = UserInput.getRegularInput();
			// User selected current date, do nothing
			if(userSelection.equals("1")) {
				break;
			}
			// User selected previous date, change our date to day before
			if(userSelection.equals("2")) {
				sleepDate = sleepDate.minusDays(1);
				break;
			}
			// User didn't enter an appropriate response
			Printer.displayBadSelection();
		}
		return sleepDate;
	}

	// Traverse through the linked list to find where to add to
	private static LinkedList<Day>.Node addToLinkedList(LocalDate sleepDate,
	        Sleep newSleep) {
		LinkedList<Day>.Node currentNode = Main.listOfEntries.getFirst();
		for(int index = 0; index < Main.listOfEntries.getSize(); index++) {
			Day currentDay = currentNode.getData();
			LocalDate currentDate = currentDay.getLabel();
			// If we already have this day as a node, add the sleep to it
			if(currentDate.equals(sleepDate)) {
				currentDay.addSleep(newSleep);
				return currentNode;
			}
			// Else, place the new node in the correct spot
			if(currentDate.isAfter(sleepDate)) {
				Day newDay = new Day(sleepDate, newSleep);
				Main.listOfEntries.add(newDay, index);
				return Main.listOfEntries.nodeOfObject(newDay);
			}
			currentNode = currentNode.getNext();
		}
		return null;
	}

	// Add a new sleep and return the node it was added to
	private static LinkedList<Day>.Node makeNewSleepNode(LocalDateTime startTime, LocalDateTime endTime) {
		// Make a new sleep out of the inputs
		Sleep newSleep = new Sleep(startTime, endTime);
		// Add the sleep and return the associated node
		return addSleep(newSleep);
	}

}