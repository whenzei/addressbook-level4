package seedu.carvicim.logic.commands;

import static seedu.carvicim.model.Model.PREDICATE_SHOW_ALL_PERSONS;

/**
 * Lists all persons in the carvicim book to the user.
 */
public class ListEmployeeCommand extends Command {

    public static final String COMMAND_WORD = "liste";

    public static final String MESSAGE_SUCCESS = "Listed all employees";


    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
