package seedu.carvicim.storage.session.exceptions;

//@@author yuhongherald
/**
 * Represents an error when {@link seedu.carvicim.storage.session.SessionData} is not initialized.
 */
public class UnitializedException extends Exception {
    public UnitializedException(String message) {
        super(message);
    }
}
