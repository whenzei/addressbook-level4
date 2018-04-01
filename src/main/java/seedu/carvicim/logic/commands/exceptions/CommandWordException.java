package seedu.carvicim.logic.commands.exceptions;

//@@author yuhongherald
/**
 * Represents an error which occurs during execution of {@link seedu.carvicim.logic.commands.SetCommand}.
 */
public class CommandWordException extends Exception {
    public CommandWordException(String message) {
        super(message);
    }
}
