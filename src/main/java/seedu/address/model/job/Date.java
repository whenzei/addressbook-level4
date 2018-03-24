package seedu.address.model.job;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//@@author whenzei
/**
 * Represent the date of job creation in the servicing manager
 */
public class Date {
    private static final String DATE_FORMATTER_PATTERN = "MMM d yyy";

    public final String value;

    public Date() {
        value = generateDate();
    }

    public Date(String date) {
        value = date;
    }

    /**
     * Generates the string representation of the current date on the system
     */
    private String generateDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN);
        return localDate.format(formatter);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.value.equals(((Date) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
