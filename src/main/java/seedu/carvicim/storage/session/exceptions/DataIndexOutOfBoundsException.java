package seedu.carvicim.storage.session.exceptions;

//@@author yuhongherald
/**
 * Represents an error which occurs when trying to access data out of specified range.
 */
public class DataIndexOutOfBoundsException extends Exception {
    public static final String ERROR_MESSAGE = "%s expected index %d to %d, but got %d";

    public DataIndexOutOfBoundsException(String field, int lower, int upper, int actual) {
        super(String.format(ERROR_MESSAGE, field, lower, upper, actual));
    }
}
