import java.util.ArrayList;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ListIterator;

public class StatsScreen {

	private static final String cancelKey = "C";
	private static final String homeKey = "H";

	public static void startStats() {
		// 1. Clear the screen
		Printer.clearScreen();
		// 2. Get the data we need
		long daysBetween = getDaysBetween();
		// 2. Display the message
		Printer.displayStatsStart(daysBetween);
		// 3. Get user input
		getUserInput(daysBetween);
	}
	
	private static long getDaysBetween() {
	    Day firstDay = Main.listOfEntries.getFirst().getData();
		Day lastDay = Main.listOfEntries.getLast().getData();
		long daysBetween = ChronoUnit.DAYS.between(firstDay.getLabel(),
		                   lastDay.getLabel());
		// Add 1 because between method is exclusive of lastDay
		daysBetween += 1;
		return daysBetween;
	}

	private static void getUserInput(long daysBetween) {
		while(true) {
			String userSelection = UserInput.getRegularInput();
			// If user selects to cancel
			if(userSelection.equals(cancelKey)) {
				HomeScreen.startHome();
				break;
			}
			// If user enters a number
			try {
			    int userSelectionInt = Integer.parseInt(userSelection);
			    // If user enters an appropriate number
			    if(userSelectionInt >= 2 && userSelectionInt <= daysBetween) {
			        displayStats(userSelectionInt);
				    break;
			    }
				Printer.displayBadSelection();
			// If user doesn't enter a number
			} catch(NumberFormatException exc) {
				Printer.displayNotAnInteger();
			}
		}
	}

	private static void displayStats(int numberOfDaysChosen) {
		// Clear the screen
		Printer.clearScreen();
		// Get the data we need
		ArrayList<Day> daysToConsider = new ArrayList<Day>();
		LinkedList<Day>.Node currentNode = Main.listOfEntries.getLast();
		LocalDate lastDay = currentNode.getData().getLabel();
		// Iterate through the list backwards
		while(currentNode != null) {
			LocalDate nextDay = currentNode.getData().getLabel();
			// Compare the next day with the last day
			long daysBetween = ChronoUnit.DAYS.between(nextDay, lastDay);
			// Add 1 because the between method is exclusive of the last day
			daysBetween += 1;
			/* If the days between is still less than the number of days to be
			looked at, add this day as a day to be considered in the stats */
			if(daysBetween <= numberOfDaysChosen) {
				daysToConsider.add(currentNode.getData());
			/* If the days between has exceeded the number of days to be 
			looked at, break */
			} else {
				break;
			}
			currentNode = currentNode.getPrevious();
		}
		// Get our stats
		getStats(daysToConsider);
		// Wait for the user to specify they'd like to return home
		while(true) {
			String userSelection = UserInput.getRegularInput();
			if(userSelection.equals(homeKey)) {
				HomeScreen.startHome();
				break;
			}
			Printer.displayBadSelection();
		}
	}

	public static void getStats(ArrayList<Day> daysToConsider) {
		ListIterator<Day> iterator = daysToConsider.listIterator(0);
		// Set totals to default values
		long minutesForStartTimeTotal = 0;
		long minutesForEndTimeTotal = 0;
		double durationTotal = 0;
		// Set minimums & maximums to default values
		Day firstDay = daysToConsider.getFirst();
		Sleep[] firstDaySleeps = firstDay.getSleeps();
		Sleep firstDayFirstSleep = firstDaySleeps[0];
		Sleep firstDayLastSleep = firstDaySleeps[firstDaySleeps.length - 1];
		int minimumStartTime = timeToMinutes(firstDayFirstSleep.getStartTime());
		int maximumStartTime = minimumStartTime;
		int minimumEndTime = timeToMinutes(firstDayLastSleep.getEndTime());
		int maximumEndTime = minimumEndTime;
		double minimumDuration = firstDay.getTotalDuration();
		double maximumDuration = minimumDuration;
		// Iterate through the list to check each day
		while(iterator.hasNext()) {
			Day nextDay = iterator.next();
			Sleep[] sleepsList = nextDay.getSleeps();
			Sleep firstSleep = sleepsList[0];
			Sleep lastSleep = sleepsList[sleepsList.length - 1];
			// Add to totals
			minutesForStartTimeTotal += timeToMinutes(firstSleep.getStartTime());
			minutesForEndTimeTotal += timeToMinutes(lastSleep.getEndTime());
			double currentDuration = nextDay.getTotalDuration();
			durationTotal += currentDuration;
			// Compare minimums & maximums
			int minutesForStartTime = timeToMinutes(firstSleep.getStartTime());
			if(minutesForStartTime < minimumStartTime) {
				minimumStartTime = minutesForStartTime;
			} else if(minutesForStartTime > maximumStartTime) {
				maximumStartTime = minutesForStartTime;
			}
			int minutesForEndTime = timeToMinutes(lastSleep.getEndTime());
			if(minutesForEndTime < minimumEndTime) {
				minimumEndTime = minutesForEndTime;
			} else if(minutesForEndTime > maximumEndTime) {
				maximumEndTime = minutesForEndTime;
			}
			if(currentDuration < minimumDuration) {
				minimumDuration = currentDuration;
			} else if(currentDuration > maximumDuration) {
				maximumDuration = currentDuration;
			}
		}
		// Calculate averages
		String startAvgString = minutesToHoursFormatted(minutesForStartTimeTotal
		                        / daysToConsider.size());
		String endAvgString = minutesToHoursFormatted(minutesForEndTimeTotal
		                      / daysToConsider.size());
		// Display averages
		Printer.displayOperation("Average", startAvgString, endAvgString);
		Printer.displayOperationDuration("Average", (durationTotal
		                                 / daysToConsider.size()));
		// Calculate & display medians
		getMedians(daysToConsider);
		// Display minimums & maximums
		Printer.displayOperation("Earliest",
		                         minutesToHoursFormatted(minimumStartTime),
		                         minutesToHoursFormatted(minimumEndTime));
		Printer.displayOperationDuration("Shortest", minimumDuration);
		Printer.displayOperation("Latest",
		                         minutesToHoursFormatted(maximumStartTime),
		                         minutesToHoursFormatted(maximumEndTime));
		Printer.displayOperationDuration("Longest", maximumDuration);
	}

	private static void getMedians(ArrayList<Day> daysToConsider) {
		int medianIndex = (daysToConsider.size() / 2) - 1; 
		    // Minus 1 because list starts at 0
		/* If the list is divisible by 2, we need to get totals of the two 
		medians then divide by 2 */
		if(daysToConsider.size() % 2 == 0) {
			Day day1 = daysToConsider.get(medianIndex);
			Day day2 = daysToConsider.get(medianIndex + 1);
			Sleep[] day1Sleeps = day1.getSleeps();
			Sleep[] day2Sleeps = day2.getSleeps();
			// Start time total
			int startMinutesTotal = timeToMinutes(day1Sleeps[0].getStartTime());
			startMinutesTotal += timeToMinutes(day2Sleeps[0].getStartTime());
			// End time total
			int endMinutesTotal = timeToMinutes(
			                          day1Sleeps[day1Sleeps.length - 1].getEndTime());
			endMinutesTotal += timeToMinutes(
			                       day2Sleeps[day2Sleeps.length - 1].getEndTime());
			// Duration total
			double durationTotal = day1.getTotalDuration();
			durationTotal += day2.getTotalDuration();
			// Print it out
			Printer.displayOperation("Median",
			                         minutesToHoursFormatted(startMinutesTotal / 2),
			                         minutesToHoursFormatted(endMinutesTotal / 2));
			Printer.displayOperationDuration("Median", (durationTotal / 2));
		// If the list is not divisible by 2, just get the middle item
		} else {
			Day medianDay = daysToConsider.get(medianIndex + 1); 
			    // Plus 1 due to rounding
			Sleep[] medianDaySleeps = medianDay.getSleeps();
			Printer.displayOperation("Median",
			                         minutesToHoursFormatted(timeToMinutes(
			                                     medianDaySleeps[0].getStartTime())),
			                         minutesToHoursFormatted(timeToMinutes(
			                                     medianDaySleeps[medianDaySleeps.length - 1].getEndTime())));
			Printer.displayOperationDuration("Median",
			                                 medianDay.getTotalDuration());
		}
	}

	// Takes total minutes and converts it into a formatted timestamp string
	private static String minutesToHoursFormatted(long minutesTotal) {
		long hour = minutesTotal / 60;
		long minute = minutesTotal % 60;
		boolean isPM = hour > 12;
		boolean isMidnightOrNoon = hour % 12 == 0;
		String timeStamp = "";
		if(isMidnightOrNoon) {
			timeStamp += "12";
		} else {
			timeStamp += hour % 12;
		}
		timeStamp += ":" + String.format("%02d", minute);
		if(isPM) {
			timeStamp += " PM";
		} else {
			timeStamp += " AM";
		}
		return timeStamp;
	}

	// Converts a LocalDateTime to minutes
	private static int timeToMinutes(LocalDateTime time) {
		int minutesTotal = time.getHour() * 60;
		minutesTotal += time.getMinute();
		return minutesTotal;
	}

	public static String getCancelKey() {
		return cancelKey;
	}

	public static String getHomeKey() {
		return homeKey;
	}
}