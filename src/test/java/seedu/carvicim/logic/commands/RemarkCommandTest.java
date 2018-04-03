package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static seedu.carvicim.commons.core.Messages.MESSAGE_JOB_NOT_FOUND;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithAssignedJobs;

import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.remark.Remark;

//@@author whenzei
public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());

    @Test
    public void equals() {
        RemarkCommand remarkCommand1 = prepareCommand("abc", "1");
        RemarkCommand remarkCommand2 = prepareCommand("def", "2");

        // same object -> returns true
        assertTrue(remarkCommand1.equals(remarkCommand1));

        // same values -> returns true
        RemarkCommand remarkCommandCopy = new RemarkCommand(new Remark("abc"), new JobNumber("1"));
        assertTrue(remarkCommand1.equals(remarkCommandCopy));

        // different types -> returns false
        assertFalse(remarkCommand1.equals(1));

        // different jobs -> return false
        assertFalse(remarkCommand1.equals(remarkCommand2));

        // null -> return false
        assertFalse(remarkCommand1.equals(null));
    }

    @Test
    public void execute_remarkSuccess() throws Exception {
        RemarkCommand remarkCommand = prepareCommand("abc", "1");
        // Get first job
        Job targetJob = model.getFilteredJobList().get(0);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_REMARK_SUCCESS, new Remark("abc"));
        ModelManager expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.addRemark(targetJob, new Remark("abc"));

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_remarkFailure() {
        RemarkCommand remarkCommand = prepareCommand("abc", "100");
        // Get first job
        Job targetJob = model.getFilteredJobList().get(0);

        assertCommandFailure(remarkCommand, model, MESSAGE_JOB_NOT_FOUND);
    }

    private RemarkCommand prepareCommand(String remark, String jobNumber) {
        RemarkCommand remarkCommand = new RemarkCommand(new Remark(remark), new JobNumber(jobNumber));
        remarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return remarkCommand;
    }
}
