package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.logic.commands.exceptions.CommandWordException;

//@@author yuhongherald
/**
 * Sets a command word to user preferred command word
 */
public class SetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "set";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a command word to user preference. "
            + "Parameters: CURRENT_COMMAND_WORD NEW_COMMAND_WORD\n"
            + "Example: " + COMMAND_WORD + " set st";

    public static final String MESSAGE_ALIAS_SUCCESS = "%s has been replaced with %s!";
    public static final String MESSAGE_DEFAULT_SUCCESS = "%s has been set as an alternative for %s!";
    public static final String MESSAGE_REMOVE_ALIAS_SUCCESS = "%s has been removed!";

    private final String currentWord;
    private final String newWord;

    /**
     * Creates an SetCommand to set the specified {@code CommandWords}
     */
    public SetCommand(String currentWord, String newWord) {
        this.currentWord = currentWord;
        this.newWord = newWord;
    }

    public String getMessageAliasSuccess() {
        return String.format(MESSAGE_ALIAS_SUCCESS, currentWord, newWord);
    }

    public String getMessageRemoveAliasSuccess() {
        return String.format(MESSAGE_REMOVE_ALIAS_SUCCESS, currentWord);
    }

    public String getMessageDefaultSuccess() {
        return String.format(MESSAGE_DEFAULT_SUCCESS, newWord, currentWord);
    }

    public String getMessageUsed() {
        return CommandWords.getMessageUsed(newWord);
    }

    public String getMessageUnused() {
        return CommandWords.getMessageUnused(currentWord);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.getCommandWords().setCommandWord(currentWord, newWord);
        } catch (CommandWordException e) {
            throw new CommandException(e.getMessage());
        }
        if (CommandWords.isDefaultCommandWord(currentWord)) {
            return new CommandResult(getMessageDefaultSuccess());
        } else if (CommandWords.isDefaultCommandWord(newWord)) {
            return new CommandResult(getMessageRemoveAliasSuccess());
        }
        return new CommandResult(getMessageAliasSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetCommand // instanceof handles nulls
                && currentWord.equals(((SetCommand) other).currentWord)
                && newWord.equals(((SetCommand) other).newWord));
    }
}
