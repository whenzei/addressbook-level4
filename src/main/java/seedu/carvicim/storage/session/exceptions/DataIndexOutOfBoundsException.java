package seedu.carvicim.storage.session.exceptions;

//@@author yuhongherald
/**
 * Represents an error which occurs when trying to access data out of specified range.
 */
public class DataIndexOutOfBoundsException extends Exception {
    public static final String ERROR_MESSAGE = "Rows expected index %d to %d, but got %d";

    public DataIndexOutOfBoundsException(int lower, int upper, int actual) {
        super(String.format(ERROR_MESSAGE, lower, upper, actual));
    }
}
