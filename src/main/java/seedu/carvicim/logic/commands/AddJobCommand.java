package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_ASSIGNED_EMPLOYEE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_VEHICLE_NUMBER;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.Status;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.remark.RemarkList;

//@@author whenzei
/**
 * Adds a job to Carvicim
 */
public class AddJobCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the Carvicim. "
            + "Parameters: "
            + PREFIX_NAME + "CLIENT_NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_VEHICLE_NUMBER + "VEHICLE_NUMBER "
            + PREFIX_ASSIGNED_EMPLOYEE + "ASSIGNED_EMPLOYEE_INDEX+ (must be a positive integer)\n"
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
        for (Index targetIndex : targetIndices) {
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
                && client.equals(((AddJobCommand) other).client)
                && vehicleNumber.equals(((AddJobCommand) other).vehicleNumber)
                && targetIndices.equals(((AddJobCommand) other).targetIndices)
                && assignedEmployees.equals(((AddJobCommand) other).assignedEmployees)
                && Objects.equals(this.toAdd, ((AddJobCommand) other).toAdd));
    }
}
