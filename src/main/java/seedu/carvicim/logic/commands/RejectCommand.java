package seedu.carvicim.logic.commands;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

//@@author yuhongherald

/**
 * Rejects an unreviewed job entry using job number
 */
public class RejectCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "reject";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Rejects job entry using job number. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d rejected!";

    private int jobNumber;

    public RejectCommand(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getMessageSuccess() {
        return String.format(MESSAGE_SUCCESS, jobNumber);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        SessionData sessionData = ImportSession.getInstance().getSessionData();
        if (sessionData.getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("There are no job entries to review!");
        }
        Job job = sessionData.reviewJobEntryUsingJobNumber(jobNumber, false, "");
        model.addJob(job);

        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RejectCommand); // instanceof handles nulls
    }

}
