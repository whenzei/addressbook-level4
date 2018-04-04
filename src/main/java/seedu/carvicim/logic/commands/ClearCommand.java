package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

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
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.resetData(new Carvicim(), new CommandWords());
        JobNumber.initialize("1");
        ImportSession.getInstance().setSessionData(new SessionData());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
