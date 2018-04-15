package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.carvicim.commons.core.Messages.MESSAGE_NO_JOB_ENTRIES;

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
public class RejectAllCommandTest extends ImportCommandTestEnv {
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
        RejectAllCommand rejectCommand1 = prepareCommand(comment);
        RejectAllCommand rejectCommandCopy = prepareCommand(comment);
        RejectAllCommand rejectCommand2 = prepareCommand("");

        // same object -> returns true
        assertTrue(rejectCommand1.equals(rejectCommand1));

        // same values -> returns true
        assertTrue(rejectCommand1.equals(rejectCommandCopy));

        // different types -> returns false
        assertFalse(rejectCommand1.equals(1));

        // different comments -> return false
        assertFalse(rejectCommand1.equals(rejectCommand2));

        // null -> return false
        assertFalse(rejectCommand1.equals(null));
    }

    @Test
    public void execute_rejectWithoutComment_success() throws Exception {
        prepareInputFiles();
        RejectAllCommand command = prepareCommand("");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectAllWithComment_success() throws Exception {
        prepareInputFiles();
        RejectAllCommand command = prepareCommand(comment.toString());
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectAllWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        RejectAllCommand command = prepareCommand(comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns RejectCommand with {@code comments}, with default data
     */
    protected RejectAllCommand prepareCommand(String comments) throws Exception {
        JobNumber.initialize(1);
        RejectAllCommand command = new RejectAllCommand(comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
