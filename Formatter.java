import java.time.*;
import java.time.format.*;
import java.util.Locale;

public class Formatter {

	private static final String formatForHeading = "EEEE, MMMM d, uuuu";
	private static final String formatForData = "MM/dd/uuuu hh:mm a";
	private static final String formatForDateOnly = "M.d.YYYY";
	private static final String formatForInput = "M/d/uuuu h:m a";
	private static final DateTimeFormatter formatterForHeading =
	    DateTimeFormatter.ofPattern(formatForHeading, Locale.ENGLISH);
	private static final DateTimeFormatter formatterForData =
	    DateTimeFormatter.ofPattern(formatForData, Locale.ENGLISH);
	private static final DateTimeFormatter formatterForDateOnly =
	    DateTimeFormatter.ofPattern(formatForDateOnly, Locale.ENGLISH);
	private static final DateTimeFormatter formatterForInput =
	    DateTimeFormatter.ofPattern(formatForInput, Locale.ENGLISH);

	public static String formatHeading(LocalDate header) {
		return formatterForHeading.format(header);
	}

	public static String formatData(LocalDateTime data) {
		return formatterForData.format(data);
	}

	public static LocalDateTime formatInput(String input) {
		return LocalDateTime.parse(input, formatterForInput);
	}
}