package seedu.carvicim.storage.session.exceptions;

//@@author yuhongherald
/**
 * Represents an error from attempting to read an excel file in {@link seedu.carvicim.storage.session.ImportSession}.
 */
public class FileFormatException extends Exception {
    public FileFormatException(String message) {
        super(message);
    }
}
