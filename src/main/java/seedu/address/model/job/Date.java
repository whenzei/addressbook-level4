package seedu.address.model.job;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//@@author whenzei
/**
 * Represent the date of job creation in the servicing manager
 */
public class Date {
    private static final String DATE_FORMATTER_PATTERN = "MMM d yyy";

    public final String date;

    public Date() {
        date = generateDate();
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
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.date.equals(((Date) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
