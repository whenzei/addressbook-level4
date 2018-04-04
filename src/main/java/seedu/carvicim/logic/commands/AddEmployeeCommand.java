package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;

/**
 * Adds a employee to the carvicim book.
 */
public class AddEmployeeCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "adde";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an employee to CarviciM. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Tom Dick "
            + PREFIX_PHONE + "98665432 "
            + PREFIX_EMAIL + "tomdicl@example.com "
            + PREFIX_TAG + "technician "
            + PREFIX_TAG + "mechanic";

    public static final String MESSAGE_SUCCESS = "New employee added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This employee already exists in CarviciM";

    private final Employee toAdd;

    /**
     * Creates an AddEmployeeCommand to add the specified {@code Employee}
     */
    public AddEmployeeCommand(Employee employee) {
        requireNonNull(employee);
        toAdd = employee;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addPerson(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateEmployeeException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddEmployeeCommand // instanceof handles nulls
                && toAdd.equals(((AddEmployeeCommand) other).toAdd));
    }
}
