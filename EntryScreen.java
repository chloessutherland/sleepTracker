public class EntryScreen {

	// The number of days we will skip if the user wants to and can
	private static final int daysToSkip = 7;
	private static boolean hasPrevious;
	private static boolean hasNext;
	private static boolean hasSkippablePrevious;
	private static boolean hasSkippableNext;
	private static final String previousKey = "<";
	private static final String nextKey = ">";
	private static final String skipPreviousKey = "<<";
	private static final String skipNextKey = ">>";
	private static final String editKey = "E";
	private static final String removeKey = "R";
	private static final String homeKey = "H";

	public static void startEntries(LinkedList<Day>.Node nodeToPull) {
		// 1. Clear the screen
		Printer.clearScreen();
		// 2. Display the message
		Printer.displayEntryScreen(nodeToPull.getData());
		// 3. Display the options
		displayOptions(nodeToPull);
		// 4. Get user's option selection
		getUserSelection(nodeToPull);
	}

	private static void displayOptions(LinkedList<Day>.Node nodeToPull) {
		Day dayToPull = nodeToPull.getData();
		// If we have a previous and a next
		hasPrevious = nodeToPull.getPrevious() != null;
		hasNext = nodeToPull.getNext() != null;
		// If there is a previous entry to skip to in large lists
		hasSkippablePrevious = (hasPrevious)
		                       && (Main.listOfEntries.getSize() > daysToSkip)
		                       && (Main.listOfEntries.indexOf(dayToPull) > daysToSkip);
		// If there is a next entry to skip to in large lists
		hasSkippableNext = (hasNext)
		                   && (Main.listOfEntries.getSize() > daysToSkip)
		                   && (Main.listOfEntries.indexOf(dayToPull) <
		                       (Main.listOfEntries.getSize() - daysToSkip));
		// If there is a previous entry, display the option to go back to it
		if(hasPrevious) {
			Printer.displayPreviousArrow();
		}
		/* If there is a previous entry to skip to in large lists, display the
		option to skip backward */
		if(hasSkippablePrevious) {
			Printer.displaySkipPreviousArrow();
		}
		// If there is a next entry, display the option to go to it
		if(hasNext) {
			Printer.displayNextArrow();
		}
		// If there is a next entry to skip to in large lists, display the option to skip forward
		if(hasSkippableNext) {
			Printer.displaySkipNextArrow();
		}
		// Display the other options
		Printer.displayOtherOptions();
	}

	private static void getUserSelection(LinkedList<Day>.Node nodeToPull) {
	    Day dayToPull = nodeToPull.getData();
	    Sleep[] sleepsToPull = dayToPull.getSleeps();
        label:
        while(true) {
            String userSelection = UserInput.getRegularInput();
            // User chose to go to the previous entry
            if(hasPrevious && userSelection.equals(previousKey)) {
                startEntries(nodeToPull.getPrevious());
                break;
            }
            // User chose to skip to previous
            if(hasSkippablePrevious && userSelection.equals(skipPreviousKey)) {
                startEntries(Main.listOfEntries.nodeOfIndex(
                                 Main.listOfEntries.indexOf(dayToPull) - daysToSkip));
            }
            // User chose to go to the next entry
            if(hasNext && userSelection.equals(nextKey)) {
                startEntries(nodeToPull.getNext());
                break;
            }
            // User chose to skip to next
            if(hasSkippableNext && userSelection.equals(skipNextKey)) {
                startEntries(Main.listOfEntries.nodeOfIndex(
                                 Main.listOfEntries.indexOf(dayToPull) + daysToSkip));
            }
			switch (userSelection) {
				// User chose to edit the entry
				case editKey:
					EditScreen.startEdit(nodeToPull);
					break label;
				// User chose to remove the entry
				case removeKey:
					LinkedList<Day>.Node previousNode = nodeToPull.getPrevious();
					// If we only have one Sleep, just remove it
					if (sleepsToPull.length == 1) {
						dayToPull.removeSleep(sleepsToPull[0]);
					// Else we need to ask the user which to delete
					} else {
						RemoveScreen.startRemove(nodeToPull);
					}
					// If there is a previous, go to that
					if (hasPrevious) {
						startEntries(previousNode);
					// Else go to the next node if there is one
					} else if (hasNext) {
						startEntries(nodeToPull.getNext());
					// Else go home because there are no entries to view
					} else {
						HomeScreen.startHome();
					}
					break label;
				// User chose to go home
				case homeKey:
					HomeScreen.startHome();
					break label;
				// User had invalid input
				default:
					Printer.displayBadSelection();
			}
        }
    }

	public static String getPreviousKey() {
		return previousKey;
	}

	public static String getNextKey() {
		return nextKey;
	}
	
	public static String getSkipPreviousKey() {
	    return skipPreviousKey;
	}
	
	public static String getSkipNextKey() {
	    return skipNextKey;
	}

	public static String getEditKey() {
		return editKey;
	}

	public static String getRemoveKey() {
		return removeKey;
	}

	public static String getHomeKey() {
		return homeKey;
	}

}