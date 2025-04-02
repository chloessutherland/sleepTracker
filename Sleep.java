import java.time.*;
import java.io.Serializable;

public class Sleep implements Serializable {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private double duration;

	public Sleep(LocalDateTime startTime, LocalDateTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		duration = 0;
		setDuration();
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
		setDuration();
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
		setDuration();
	}

	public double getDuration() {
		return duration;
	}

	private void setDuration() {
		long durationInSeconds = Duration.between(startTime, endTime).getSeconds();
		// Convert durationInSeconds into hours
		duration = (durationInSeconds / 60.0) / 60.0;
	}

}