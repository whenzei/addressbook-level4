package seedu.carvicim.logic.commands;

import static seedu.carvicim.commons.core.Messages.MESSAGE_NO_JOB_ENTRIES;

import java.util.ArrayList;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

//@@author yuhongherald

/**
 * Accepts an unreviewed job entry using job number and adds into servicing manager, adding comment into remarksList
 */
public class AcceptCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "accept";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Accepts job entry using index. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d accepted!";

    private final int jobIndex;
    private final String comment;

    public AcceptCommand(int jobIndex, String comment) {
        this.jobIndex = jobIndex;
        this.comment = comment;
    }

    public String getMessageSuccess() {
        return String.format(MESSAGE_SUCCESS, jobIndex);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        SessionData sessionData = ImportSession.getInstance().getSessionData();
        if (sessionData.getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException(MESSAGE_NO_JOB_ENTRIES);
        }
        Job job = sessionData.reviewJobEntryUsingJobIndex(jobIndex, true, comment);
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(job);
        model.addJobsAndNewEmployees(jobs);

        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcceptCommand) // instanceof handles nulls
                && jobIndex == ((AcceptCommand) other).jobIndex
                && comment.equals(((AcceptCommand) other).comment);
    }

}
