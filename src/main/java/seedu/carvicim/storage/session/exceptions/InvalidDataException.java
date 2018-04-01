package seedu.carvicim.storage.session.exceptions;

//@@author yuhongherald
/**
 * Represents an error when data supplied to {@link seedu.carvicim.storage.session.SessionData} is in wrong format.
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
