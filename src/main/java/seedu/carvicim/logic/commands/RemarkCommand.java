package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_JOB_NUMBER;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import seedu.carvicim.commons.core.EventsCenter;
import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.events.ui.JobDisplayPanelUpdateRequestEvent;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.exceptions.JobNotFoundException;
import seedu.carvicim.model.remark.Remark;

//@@author whenzei
/**
 * Adds a remark to a job in Carvicim
 */
public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a remark to the a job.\n"
            + "Parameters: " + PREFIX_JOB_NUMBER + "JOB_NUMBER " + PREFIX_REMARK + "REMARK\n"
            + "Example: " + COMMAND_WORD + " j/1" + " r/hellooooo";

    public static final String MESSAGE_REMARK_SUCCESS = "Remark added:  %1$s";

    private final JobNumber jobNumber;
    private final Remark remark;

    private Job target;

    /**
     * Creates a RemarkCommand to add the specified {@code Remark}
     */
    public RemarkCommand(Remark remark, JobNumber jobNumber) {
        requireAllNonNull(remark, jobNumber);
        this.remark = remark;
        this.jobNumber = jobNumber;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(target);
        try {
            model.addRemark(target, remark);
        } catch (JobNotFoundException jnfe) {
            throw new AssertionError("The target job cannot be missing");
        }
        EventsCenter.getInstance().post(new JobDisplayPanelUpdateRequestEvent(target));
        return new CommandResult(String.format(MESSAGE_REMARK_SUCCESS, remark));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Job> lastShownJobList = model.getFilteredJobList();
        Iterator<Job> jobIterator = lastShownJobList.iterator();

        while (jobIterator.hasNext()) {
            Job currentJob = jobIterator.next();
            if (currentJob.getJobNumber().equals(jobNumber)) {
                target = currentJob;
                break;
            }
        }

        if (target == null) {
            throw new CommandException(Messages.MESSAGE_JOB_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RemarkCommand
                && this.jobNumber.equals(((RemarkCommand) other).jobNumber)
                && this.remark.equals(((RemarkCommand) other).remark)
                && Objects.equals(this.target, ((RemarkCommand) other).target));
    }
}
