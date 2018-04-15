package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithJobs;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.JobNumber;

//@@author charmaineleehc
public class EmailCommandTest {

    private Model model;

    @Before
    public void setUp() {
        this.model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
    }

    @Test
    public void execute_outOfBoundsJobNumber_failure() {
        String outOfBoundsJobNumber = Integer.toString(model.getFilteredJobList().size() + 1);
        EmailCommand emailCommand = new EmailCommand(new JobNumber(outOfBoundsJobNumber));
        emailCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(emailCommand, model, Messages.MESSAGE_INVALID_JOB_NUMBER);
    }

    @Test
    public void equals() {
        EmailCommand emailCommandJobOne = new EmailCommand(new JobNumber("1"));
        EmailCommand emailCommandJobTwo = new EmailCommand(new JobNumber("2"));

        assertTrue(emailCommandJobOne.equals(emailCommandJobOne));

        EmailCommand emailCommandJobOneCopy = new EmailCommand(new JobNumber("1"));
        assertTrue(emailCommandJobOne.equals(emailCommandJobOneCopy));

        assertFalse(emailCommandJobOne.equals(1));

        assertFalse(emailCommandJobOne.equals(null));

        assertFalse(emailCommandJobOne.equals(emailCommandJobTwo));

    }

}
