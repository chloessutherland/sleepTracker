public class RemoveScreen {

	private static final String allKey = "A";
	private static final String cancelKey = "C";

	public static void startRemove(LinkedList<Day>.Node nodeToPull) {
		// 1. Clear the screen
		Printer.clearScreen();
		// 2. Get the data we need
		Day dayToPull = nodeToPull.getData();
		Sleep[] sleepsList = dayToPull.getSleeps();
		// 3. Display the message
		Printer.displayRemove(sleepsList);
		// 4. Get the user's selection
		getUserSelection(nodeToPull, sleepsList);

	}

	private static void getUserSelection(LinkedList<Day>.Node nodeToPull,
	                                     Sleep[] sleepsList) {
		Day dayToPull = nodeToPull.getData();
		while(true) {
			String userSelection = UserInput.getRegularInput();
			if(userSelection.equals(allKey)) {
				break;
			}
			if(userSelection.equals(cancelKey)) {
				EntryScreen.startEntries(nodeToPull);
				break;
			}
			try {
				int intSelection = Integer.parseInt(userSelection);
				dayToPull.removeSleep(sleepsList[intSelection]);
				break;
			} catch(NumberFormatException exc) {
				Printer.displayNotAnInteger();
			}
		}
	}

	public static String getAllKey() {
		return allKey;
	}

	public static String getCancelKey() {
		return cancelKey;
	}
}