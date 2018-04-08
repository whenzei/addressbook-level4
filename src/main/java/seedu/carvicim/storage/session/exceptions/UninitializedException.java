package seedu.carvicim.storage.session.exceptions;

//@@author yuhongherald
/**
 * Represents an error when {@link seedu.carvicim.storage.session.SessionData} is not initialized.
 */
public class UninitializedException extends Exception {
    public UninitializedException(String message) {
        super(message);
    }
}
