package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_JOB_NUMBER;

import java.util.Iterator;

import seedu.carvicim.commons.core.EventsCenter;
import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.ui.JumpToJobListRequestEvent;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;

/**
 * Selects a job indentified using it's displayed job number in carvicim
 */
public class SelectJobCommand extends Command {

    public static final String COMMAND_WORD = "selectj";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the job identified by the job number used in the last job listing.\n"
            + "Parameters: JOB_NUMBER (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_JOB_NUMBER + "1";

    public static final String MESSAGE_SELECT_JOB_SUCCESS = "Selected Job: %1$s";

    private final JobNumber jobNumber;

    private Index targetIndex;

    public SelectJobCommand(JobNumber jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Override
    public CommandResult execute() throws CommandException {
        targetIndex = findJobIndex();
        EventsCenter.getInstance().post(new JumpToJobListRequestEvent(targetIndex));
        return new  CommandResult(String.format(MESSAGE_SELECT_JOB_SUCCESS, jobNumber));

    }

    /**
     * Returns the index of the selected job in the job list
     */
    public Index getTargetIndex() {
        return targetIndex;
    }

    /**
     * Returns the index of the job from job list, that matches the job number provided.
     */
    public Index findJobIndex() throws CommandException {
        Iterator<Job> jobIterator = model.getFilteredJobList().iterator();
        int count = 0;
        while (jobIterator.hasNext()) {
            count++;
            Job currJob = jobIterator.next();
            if (currJob.getJobNumber().equals(this.jobNumber)) {
                return Index.fromOneBased(count);
            }
        }
        throw new CommandException(Messages.MESSAGE_INVALID_JOB_NUMBER);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectJobCommand // instanceof handles nulls
                && this.jobNumber.equals(((SelectJobCommand) other).jobNumber)); // state check
    }
}
