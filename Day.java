import java.time.*;
import java.util.ArrayList;
import java.io.Serializable;

public class Day implements Serializable {

	private ArrayList<Sleep> listOfSleeps = new ArrayList<Sleep>();
	private final LocalDate label;
	private double totalDuration;

	public Day(LocalDate label, Sleep firstSleep) {
		this.label = label;
		listOfSleeps.add(firstSleep);
		totalDuration = firstSleep.getDuration();
	}

	public void addSleep(Sleep sleepToAdd) {
		/* Iterate through the sleeps list to find where to add to to maintain
		sorted list */
		for(int i = 0; i < listOfSleeps.size(); i++) {
			Sleep currentSleep = listOfSleeps.get(i);
			if(currentSleep.getStartTime().isAfter(sleepToAdd.getStartTime())) {
				listOfSleeps.add(i, sleepToAdd);
				return;
			}
		}
		// Add to the end if we didn't find a suitable spot
		listOfSleeps.add(sleepToAdd);
		totalDuration += sleepToAdd.getDuration();
	}

	public void removeSleep(Sleep sleepToRemove) {
		listOfSleeps.remove(sleepToRemove);
		// Delete this day if there are no more sleeps
		if(listOfSleeps.isEmpty()) {
			Main.listOfEntries.remove(this);
		}
		totalDuration -= sleepToRemove.getDuration();
	}

	public void resetTotalDuration() {
		totalDuration = 0;
		for(Sleep sleep : listOfSleeps) {
			totalDuration += sleep.getDuration();
		}
	}

	public Sleep[] getSleeps() {
		return listOfSleeps.toArray(new Sleep[listOfSleeps.size()]);
	}

	public LocalDate getLabel() {
		return label;
	}

	public double getTotalDuration() {
		return totalDuration;
	}
}