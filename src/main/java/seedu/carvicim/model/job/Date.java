package seedu.carvicim.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//@@author whenzei
/**
 * Represent the date of job creation in the servicing manager
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS = "Date should be of the format MMM DD YYYY";
    public static final String DATE_VALIDATION_REGEX = "\\w\\w\\w\\s(0[1-9]|[12][0-9]|3[01])\\s(19|20)\\d\\d";

    private static final String DATE_FORMATTER_PATTERN = "MMM dd yyyy";

    private static final String DATE_SPLIT_REGEX = " ";
    private static final int DATE_DATA_INDEX_DAY = 1;
    private static final int DATE_DATA_INDEX_MONTH = 0;
    private static final int DATE_DATA_INDEX_YEAR = 2;
    private static final String[] monthAbbreviation = {"Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public final String value;
    private int day;
    private int month;
    private int year;

    public Date() {
        value = generateDate();
    }

    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        String trimmedDate = date.trim();
        String[] splitAddress = trimmedDate.split(DATE_SPLIT_REGEX);
        this.day = Integer.parseInt(splitAddress[DATE_DATA_INDEX_DAY]);
        this.month = convertMonth(splitAddress[DATE_DATA_INDEX_MONTH]);
        this.year = Integer.parseInt(splitAddress[DATE_DATA_INDEX_YEAR]);
        this.value = date;
    }

    /**
     * Generates the string representation of the current date on the system
     */
    private String generateDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN);
        return localDate.format(formatter);
    }

    /**
     * Returns true if a given string is a valid date
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Convert month abbreviation to number
     */
    private int convertMonth(String month) {
        int i;
        for (i = 0; i < 12; i++) {
            if (month.equals(monthAbbreviation[i])) {
                break;
            }
        }
        return i + 1;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
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
