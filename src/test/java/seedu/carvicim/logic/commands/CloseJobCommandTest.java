package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static seedu.carvicim.commons.core.Messages.MESSAGE_JOB_NOT_FOUND;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_JOB_NUMBER_ONE;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_JOB_NUMBER_TWO;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.carvicim.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithAssignedJobs;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_JOB;

import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;

//@@author whenzei

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code CloseJobCommand}
 */
public class CloseJobCommandTest {
    private Model model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());

    @Test
    public void execute_closeJobFailure_jobNotFound() {
        CloseJobCommand closeJobCommand = prepareCommand(new JobNumber("999999"));
        assertCommandFailure(closeJobCommand, model, MESSAGE_JOB_NOT_FOUND);
    }

    @Test
    public void execute_closeJobSuccess_jobIsPresent() throws Exception {
        Job jobToClose = model.getFilteredJobList().get(INDEX_FIRST_JOB.getZeroBased());

        CloseJobCommand closeJobCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));

        String expectedMessage = String.format(CloseJobCommand.MESSAGE_CLOSE_JOB_SUCCESS, jobToClose);
        ModelManager expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.closeJob(jobToClose);

        assertCommandSuccess(closeJobCommand, model, expectedMessage, expectedModel);

    }

    /**
     * 1. Closes a {@code Job} from CarviciM
     * 2. Undo the closing of job
     * 3. Redo the closing of job
     */
    @Test
    public void executeUndoRedo_sameJobClosed() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CloseJobCommand closeJobCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));
        Model expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        Job jobToClose = model.getFilteredJobList().get(INDEX_FIRST_JOB.getZeroBased());

        // close -> closes the job number 1 which is the first job in the job list
        closeJobCommand.execute();
        undoRedoStack.push(closeJobCommand);

        // undo -> reverts Carvicim back to previous state
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.closeJob(jobToClose);
        assertNotEquals(jobToClose, model.getFilteredPersonList().get(INDEX_FIRST_JOB.getZeroBased()));
        // redo -> closes same Job of job number 1
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);

    }

    @Test
    public void equals() throws Exception {
        CloseJobCommand closeJobNumberOneCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));
        CloseJobCommand closeJobNumberTwoCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_TWO));

        // same object -> returns ture
        assertTrue(closeJobNumberOneCommand.equals(closeJobNumberOneCommand));

        // same values -> returns true
        CloseJobCommand closeJobNumberOneCommandCopy = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));
        assertTrue(closeJobNumberOneCommand.equals(closeJobNumberOneCommandCopy));

        // one command preprocessed when previously equal -> returns false
        closeJobNumberOneCommandCopy.preprocessUndoableCommand();
        assertFalse(closeJobNumberOneCommand.equals(closeJobNumberOneCommandCopy));

        // null -> returns false
        assertFalse(closeJobNumberOneCommand.equals(null));

        // different job -> returns false
        assertFalse(closeJobNumberOneCommand.equals(closeJobNumberTwoCommand));

    }

    /**
     * Returns a {@code CloseJobCommand} with the parameter {@code jobNumber}.
     */
    public CloseJobCommand prepareCommand(JobNumber jobNumber) {
        CloseJobCommand closeJobCommand = new CloseJobCommand(jobNumber);
        closeJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return  closeJobCommand;
    }
}
