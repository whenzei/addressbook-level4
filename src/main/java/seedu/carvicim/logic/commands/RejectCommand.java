package seedu.carvicim.logic.commands;

import static seedu.carvicim.commons.core.Messages.MESSAGE_NO_JOB_ENTRIES;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

//@@author yuhongherald

/**
 * Rejects an unreviewed job entry using job number, adding comment into remarksList
 */
public class RejectCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "reject";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Rejects job entry using job index. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d rejected!";

    private final int jobIndex;
    private final String comment;

    public RejectCommand(int jobIndex, String comment) {
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
        Job job = sessionData.reviewJobEntryUsingJobIndex(jobIndex, false, comment);

        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RejectCommand) // instanceof handles nulls
                && jobIndex == ((RejectCommand) other).jobIndex
                && comment.equals(((RejectCommand) other).comment);
    }

}
