package seedu.carvicim.logic.commands;

import java.util.List;

import seedu.carvicim.commons.core.EventsCenter;
import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.ui.JumpToEmployeeListRequestEvent;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.person.Employee;

/**
 * Selects a employee identified using it's last displayed index from the carvicim book.
 */
public class SelectEmployeeCommand extends Command {

    public static final String COMMAND_WORD = "selecte";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the employee identified by the index number used in the last employee listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Employee: %1$s";

    private final Index targetIndex;

    public SelectEmployeeCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<Employee> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToEmployeeListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectEmployeeCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectEmployeeCommand) other).targetIndex)); // state check
    }
}
