package seedu.carvicim.logic.commands;

import seedu.carvicim.commons.core.EventsCenter;
import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.ui.SetThemeRequestEvent;
import seedu.carvicim.logic.commands.exceptions.CommandException;

//@@author whenzei
/**
 * Changes the theme of the application
 */
public class ThemeCommand extends Command {
    public static final String COMMAND_WORD = "theme";

    public static final int NUMBER_OF_THEMES = 2;

    public static final String MESSAGE_THEME_CHANGE_SUCCESS = "Theme updated: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Applies selected theme\n"
            + "1. Teal theme\n"
            + "2. Dark theme\n"
            + "Parameters: INDEX (positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2";

    private final Index selectedIndex;

    public ThemeCommand(Index selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (selectedIndex.getZeroBased() >= NUMBER_OF_THEMES) {
            throw new CommandException(Messages.MESSAGE_INVALID_THEME_INDEX);
        }
        EventsCenter.getInstance().post(new SetThemeRequestEvent(selectedIndex));
        return new CommandResult(String.format(MESSAGE_THEME_CHANGE_SUCCESS, selectedIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ThemeCommand // instanceof handles nulls
                && this.selectedIndex.equals(((ThemeCommand) other).selectedIndex)); // state check

    }
}
