import java.time.*;

public class EditScreen {

	private static final String editStartTimeKey = "S";
	private static final String editEndTimeKey = "E";
	private static final String cancelKey = "C";

	public static void startEdit(LinkedList<Day>.Node nodeToEdit) {
		// 1. Clear the screen
		Printer.clearScreen();
		// 2. Get the necessary data
		Day dayToEdit = nodeToEdit.getData();
		Sleep[] sleepsList = dayToEdit.getSleeps();
		/* If there is just one sleep, no need to ask the user which sleep to
		edit */
		if(sleepsList.length == 1) {
			editSleep(sleepsList[0], dayToEdit);
			// Go back home
			HomeScreen.startHome();
			return;
		}
		// 3. If there is more than one sleep, ask the user which sleep to edit
		askWhichSleepToEdit(sleepsList, dayToEdit);
	}

	private static void editSleep(Sleep sleepToEdit, Day dayOfSleep) {
		Printer.displayEditSleep(sleepToEdit);
        selection:
        while(true) {
            String userSelection = UserInput.getRegularInput();
            // If user selects to edit the start time
			switch (userSelection) {
				case editStartTimeKey:
					LocalDateTime oldStart = setNewStartTime(sleepToEdit, dayOfSleep);
                	/* true, needs to move, if the new start date doesn't
                	match the old start date */
					if (!(sleepToEdit.getStartTime().toLocalDate().equals(
						oldStart.toLocalDate()))) {
						// Remove the sleep and re-add it to a new or existing node
						dayOfSleep.removeSleep(sleepToEdit);
						AddScreen.addSleep(sleepToEdit);
						break selection;
					}
					Sleep[] sleepsList = dayOfSleep.getSleeps();
					// Check if we need to move the sleep within the sleeps list
					if (checkIfNeedToMoveWithinDay(sleepToEdit, sleepsList)) {
						// Remove the sleep and re-add it to the same node
						dayOfSleep.removeSleep(sleepToEdit);
						dayOfSleep.addSleep(sleepToEdit);
					}
					break selection;
				// If user selects to edit the end time
				case editEndTimeKey:
					setNewEndTime(sleepToEdit, dayOfSleep);
					break selection;
				// If user chose to cancel
				case cancelKey:
					break selection;
			}
			Printer.displayBadSelection();
        }
    }

	private static LocalDateTime setNewStartTime(Sleep sleepToEdit, Day dayOfSleep) {
		Printer.displayEditStart();
		LocalDateTime newStart = AddScreen.getUserDate();
		LocalDateTime oldStart = sleepToEdit.getStartTime();
		sleepToEdit.setStartTime(newStart);
		dayOfSleep.resetTotalDuration();
		return oldStart;
	}

	private static void setNewEndTime(Sleep sleepToEdit, Day dayOfSleep) {
		Printer.displayEditEnd();
		LocalDateTime newEnd = AddScreen.getUserDate();
		sleepToEdit.setEndTime(newEnd);
		dayOfSleep.resetTotalDuration();
	}

	// Returns true, needs to move, if the array has become unsorted
	public static boolean checkIfNeedToMoveWithinDay(Sleep editedSleep,
	        Sleep[] sleepsList) {
		// If there is only one Sleep in the list, no need to move it
		if(sleepsList.length <= 1) {
			return false;
		}
		// Get the index of the edited sleep within the list
		int indexOfSleep = -1;
		for(int index = 0; index < sleepsList.length - 1; index++) {
			if(sleepsList[index] == editedSleep) {
				indexOfSleep = index;
			}
		}
		LocalDateTime editedStartTime = editedSleep.getStartTime();
		/* If the sleep is at the beginning of the list, check only the sleep
		after */
		if(indexOfSleep == 0) {
			/* If the next sleep's start time is before the edited sleep, we
			need to move it */
			return(sleepsList[indexOfSleep + 1].getStartTime().isBefore(editedStartTime));
		}
		// If the sleep is at the end of the list, check only the sleep before
		if(indexOfSleep == sleepsList.length - 1) {
			/* If the previous sleep's start time is after the edited sleep, we
			need to move it */
			return(sleepsList[indexOfSleep - 1].getStartTime().isAfter(editedStartTime));
		}
		// Check the sleep before and after to make sure the list is sorted
        return sleepsList[indexOfSleep + 1].getStartTime().isBefore(editedStartTime)
                || sleepsList[indexOfSleep - 1].getStartTime().isAfter(editedStartTime);
		// Doesn't need to move if we get to here
    }

	public static void askWhichSleepToEdit(Sleep[] sleepsList, Day dayToEdit) {
		Printer.displayEdit(dayToEdit);
		// Get the user's input selection
		while(true) {
			String userSelection = UserInput.getRegularInput();
			// If user selects to cancel
			if(userSelection.equals(cancelKey)) {
				HomeScreen.startHome();
				break;
			}
			// If user selects a number
			try {
				int intSelection = Integer.parseInt(userSelection);
				// Edit the chosen sleep
				editSleep(sleepsList[intSelection], dayToEdit);
				HomeScreen.startHome();
				break;
			// If the user didn't enter a number
			} catch(NumberFormatException numException) {
				Printer.displayNotAnInteger();
			}
		}
	}

	public static String getEditStartTimeKey() {
		return editStartTimeKey;
	}

	public static String getEditEndTimeKey() {
		return editEndTimeKey;
	}

	public static String getCancelKey() {
		return cancelKey;
	}
}