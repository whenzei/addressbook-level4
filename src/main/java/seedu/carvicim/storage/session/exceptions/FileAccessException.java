package seedu.carvicim.storage.session.exceptions;

//@@author yuhongherald
/**
 * Represents an error from attempting to read an excel file in {@link seedu.carvicim.storage.session.ImportSession}.
 */
public class FileAccessException extends Exception {
    public FileAccessException(String message) {
        super(message);
    }
}
