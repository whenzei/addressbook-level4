package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNED_EMPLOYEE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VEHICLE_NUMBER;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.job.Date;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobNumber;
import seedu.address.model.job.Status;
import seedu.address.model.job.VehicleNumber;
import seedu.address.model.person.Employee;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniqueEmployeeList;
import seedu.address.model.person.exceptions.DuplicateEmployeeException;
import seedu.address.model.remark.RemarkList;

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
            + PREFIX_ASSIGNED_EMPLOYEE + "ASSIGNED_EMPLOYEE_INDEX+\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_VEHICLE_NUMBER + "SHG123A "
            + PREFIX_ASSIGNED_EMPLOYEE + "3 "
            + PREFIX_ASSIGNED_EMPLOYEE + "6 ";

    public static final String MESSAGE_SUCCESS = "New job added: %1$s";

    private final Person client;
    private final VehicleNumber vehicleNumber;
    private final ArrayList<Index> targetIndices;
    private final UniqueEmployeeList assignedEmployees;

    private Job toAdd;

    /**
     * Creates an AddJobCommand to add the specified {@code Job}
     */
    public AddJobCommand(Person client, VehicleNumber vehicleNumber, ArrayList<Index> targetIndices) {
        requireAllNonNull(client, vehicleNumber, targetIndices);
        this.client = client;
        this.vehicleNumber = vehicleNumber;
        this.targetIndices = targetIndices;
        assignedEmployees = new UniqueEmployeeList();
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Employee> lastShownList = model.getFilteredPersonList();

        //Check for valid employee indices
        for(Index targetIndex : targetIndices) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
            }
        }

        try {
            for (Index targetIndex : targetIndices) {
                assignedEmployees.add(lastShownList.get(targetIndex.getZeroBased()));
            }
            toAdd = new Job(client, vehicleNumber, new JobNumber(), new Date(), assignedEmployees,
                    new Status(Status.STATUS_ONGOING), new RemarkList());
        } catch (DuplicateEmployeeException e) {
            throw new CommandException("Duplicate employee index");
        }

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
