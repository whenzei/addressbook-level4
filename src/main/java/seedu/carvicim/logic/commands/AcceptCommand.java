package seedu.carvicim.logic.commands;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

//@@author yuhongherald

/**
 * Accepts an unreviewed job entry using job number and adds into servicing manager
 */
public class AcceptCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "accept";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Accepts job entry using job number. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d accepted!";

    private int jobNumber;

    public AcceptCommand(int jobNumber) {
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
        Job job = sessionData.reviewJobEntryUsingJobNumber(jobNumber, true, "");
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
                || (other instanceof AcceptCommand); // instanceof handles nulls
    }

}
