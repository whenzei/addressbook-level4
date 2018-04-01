package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;

/**
 * Deletes a employee identified using it's last displayed index from the carvicim book.
 */
public class DeleteEmployeeCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deletee";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the employee identified by the index number used in the last employee listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Employee: %1$s";

    private final Index targetIndex;

    private Employee employeeToDelete;

    public DeleteEmployeeCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(employeeToDelete);
        try {
            model.deletePerson(employeeToDelete);
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("The target employee cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, employeeToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Employee> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
        }

        employeeToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEmployeeCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteEmployeeCommand) other).targetIndex) // state check
                && Objects.equals(this.employeeToDelete, ((DeleteEmployeeCommand) other).employeeToDelete));
    }
}
