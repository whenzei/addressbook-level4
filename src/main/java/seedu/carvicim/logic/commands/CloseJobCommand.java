package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_JOB_NUMBER;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.Status;
import seedu.carvicim.model.job.exceptions.JobNotFoundException;

//@@author whenzei
/**
 * Closes an ongoing job in Carvicim
 */
public class CloseJobCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "closej";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Closes the job specified by the job number.\n"
            + "Parameters: " + PREFIX_JOB_NUMBER + "JOB_NUMBER\n"
            + "Example: " + COMMAND_WORD + " j/22";

    public static final String MESSAGE_CLOSE_JOB_SUCCESS = "Closed Job: %1$s";

    private final JobNumber targetJobNumber;

    private Job target;
    private Job updatedJob;

    public CloseJobCommand(JobNumber targetJobNumber) {
        this.targetJobNumber = targetJobNumber;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(target);
        requireNonNull(updatedJob);
        try {
            model.closeJob(target, updatedJob);
        } catch (JobNotFoundException jnfe) {
            throw new AssertionError("The target job cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_CLOSE_JOB_SUCCESS, updatedJob));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Job> lastShownJobList = model.getFilteredJobList();
        Iterator<Job> jobIterator = lastShownJobList.iterator();

        while (jobIterator.hasNext()) {
            Job currJob = jobIterator.next();
            if (currJob.getJobNumber().equals(this.targetJobNumber)
                    && (currJob.getStatus().value).equals(Status.STATUS_ONGOING)) {
                target = currJob;
                updatedJob = createUpdatedJob(currJob);
                break;
            }
        }

        if (target == null) {
            throw new CommandException(Messages.MESSAGE_JOB_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CloseJobCommand // instanceof handles nulls
                && this.targetJobNumber.equals(((CloseJobCommand) other).targetJobNumber) // state check
                && Objects.equals(this.target, ((CloseJobCommand) other).target));
    }

    /**
     * Creates and returns a new {@code Job} with a closed status
     */
    public static Job createUpdatedJob(Job jobToEdit) {
        assert jobToEdit != null;

        return new Job(jobToEdit.getClient(), jobToEdit.getVehicleNumber(), jobToEdit.getJobNumber(),
                jobToEdit.getDate(), jobToEdit.getAssignedEmployees(),
                new Status(Status.STATUS_CLOSED), jobToEdit.getRemarkList());
    }
}
