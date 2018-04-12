/*package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.JobNumber;

public class EmailCommandTest {

    private Model model;

    @Before
    public void setUp() {
        this.model = new ModelManager(getTypicalCarvicim(), new UserPrefs());
    }

    @Test
    public void execute_sendEmail_successful() throws CommandException {
        EmailCommand emailCommand = new EmailCommand(new JobNumber("1"));
        CommandResult commandResult = emailCommand.execute();
        assertEquals(EmailCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
    }

    @Test
    public void execute_outOfBoundsJobNumber_failure() {
        String outOfBoundsJobNumber = Integer.toString(model.getFilteredJobList().size() + 1);

        try {
            EmailCommand emailCommand = new EmailCommand(new JobNumber(outOfBoundsJobNumber));
            emailCommand.execute();
        } catch (CommandException | NullPointerException e) {
            assertEquals(Messages.MESSAGE_INVALID_JOB_NUMBER, e.getMessage());
        }
    }

    @Test
    public void equals() {
        EmailCommand emailCommandJobOne = new EmailCommand(new JobNumber("1"));
        EmailCommand emailCommandJobTwo = new EmailCommand(new JobNumber("2"));

        assertTrue(emailCommandJobOne.equals(emailCommandJobOne));

        EmailCommand emailCommandJobOneCopy = new EmailCommand(new JobNumber("1"));
        assertTrue(emailCommandJobOne == emailCommandJobOneCopy);

        assertFalse(emailCommandJobOne.equals(1));

        assertFalse(emailCommandJobOne.equals(null));

        assertFalse(emailCommandJobOne.equals(emailCommandJobTwo));

    }

}*/
