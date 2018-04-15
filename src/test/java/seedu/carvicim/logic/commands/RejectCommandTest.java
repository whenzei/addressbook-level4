package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.carvicim.commons.core.Messages.MESSAGE_NO_JOB_ENTRIES;
import static seedu.carvicim.storage.session.SessionData.ERROR_MESSAGE_INVALID_JOB_INDEX;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

//@@author yuhongherald
public class RejectCommandTest extends ImportCommandTestEnv {
    private Remark comment;
    private ModelIgnoreJobDates expectedModel;
    @Before
    public void setup() throws Exception {
        comment = new Remark("comment");
        expectedModel = new ModelIgnoreJobDates();
    }

    @Test
    public void equals() throws Exception {
        String comment = "comment";
        RejectCommand rejectCommand1 = prepareCommand(1, comment);
        RejectCommand rejectCommandCopy = prepareCommand(1, comment);
        RejectCommand rejectCommand2 = prepareCommand(2, comment);
        RejectCommand rejectCommand3 = prepareCommand(1, "");

        // same object -> returns true
        assertTrue(rejectCommand1.equals(rejectCommand1));

        // same values -> returns true
        assertTrue(rejectCommand1.equals(rejectCommandCopy));

        // different types -> returns false
        assertFalse(rejectCommand1.equals(1));

        // different job index -> return false
        assertFalse(rejectCommand1.equals(rejectCommand2));

        // different comments -> return false
        assertFalse(rejectCommand1.equals(rejectCommand3));

        // null -> return false
        assertFalse(rejectCommand1.equals(null));
    }

    @Test
    public void execute_rejectWithoutComment_success() throws Exception {
        prepareInputFiles();
        RejectCommand command = prepareCommand(1, "");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectWithComment_success() throws Exception {
        prepareInputFiles();
        RejectCommand command = prepareCommand(1, comment.toString());
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectOutOfBounds_failure() throws Exception {
        prepareInputFiles();
        RejectCommand command = prepareCommand(3, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_INVALID_JOB_INDEX, e.getMessage());
        }
        commandCleanup(command);
    }

    @Test
    public void execute_rejectWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        RejectCommand command = prepareCommand(1, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns RejectCommand with {@code jobIndex} and {@code comments}, with default data
     */
    protected RejectCommand prepareCommand(int jobIndex, String comments) throws Exception {
        JobNumber.initialize(1);
        RejectCommand command = new RejectCommand(jobIndex, comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
