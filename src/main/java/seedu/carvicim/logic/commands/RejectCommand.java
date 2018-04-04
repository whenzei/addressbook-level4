package seedu.carvicim.logic.commands;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.InvalidDataException;

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
        ImportSession importSession = ImportSession.getInstance();
        if (importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("There are no job entries to review!");
        }
        try {
            importSession.getSessionData().reviewJobEntryUsingJobNumber(jobNumber, false, "");
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (InvalidDataException e) {
            throw new CommandException(e.getMessage());
        }
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
