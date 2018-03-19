package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNED_EMPLOYEE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VEHICLE_NUMBER;

import seedu.address.model.job.Job;

/**
 * Adds a job to CarviciM
 */
public class AddJobCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the CarviciM. "
            + "Parameters: "
            + PREFIX_NAME + "CLIENT_NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_VEHICLE_NUMBER + "VEHICLE_NUMBER "
            + PREFIX_ASSIGNED_EMPLOYEE + "ASSIGNED_EMPLOYEE+\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_VEHICLE_NUMBER + "SHG123A "
            + PREFIX_ASSIGNED_EMPLOYEE + "3 "
            + PREFIX_ASSIGNED_EMPLOYEE + "6 ";

    public static final String MESSAGE_SUCCESS = "New job added: %1$s";

    private final Job toAdd;

    /**
     * Creates an AddJobCommand to add the specified {@code Job}
     */
    public AddJobCommand(Job job) {
        requireNonNull(job);
        toAdd = job;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.addJob(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddJobCommand // instanceof handles nulls
                && toAdd.equals(((AddJobCommand) other).toAdd));
    }
}
