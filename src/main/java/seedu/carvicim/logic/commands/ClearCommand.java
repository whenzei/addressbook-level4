package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

/**
 * Clears the carvicim book.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "CarviciM has been cleared!";


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        model.resetData(new Carvicim(), new CommandWords());
        ImportSession.getInstance().setSessionData(new SessionData());
        if (model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        model.resetJobDisplayPanel();
        JobNumber.initialize("1");
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
