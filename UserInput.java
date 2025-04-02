import java.time.*;
import java.util.Scanner;

public class UserInput {

	public static Scanner scanny = new Scanner(System.in);

	public static String getRegularInput() {
		String inputString = scanny.nextLine();
		return inputString.toUpperCase();
	}

	public static LocalDateTime getDateTimeFromUser() {
		String dateString = scanny.nextLine();
		return Formatter.formatInput(dateString.toUpperCase());
	}

	public static void closeScanner() {
		scanny.close();
	}
}