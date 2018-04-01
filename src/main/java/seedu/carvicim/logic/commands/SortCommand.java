package seedu.carvicim.logic.commands;

import seedu.carvicim.model.Carvicim;

//@@author richardson0694
/**
 * Sorts all persons alphabetically by names in the carvicim book to the user.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays all persons in the carvicim book as a list in alphabetical order.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Sorted all persons";

    protected Carvicim carvicim;

    @Override
    public CommandResult execute() {
        model.sortPersonList();
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
