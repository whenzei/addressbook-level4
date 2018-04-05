package seedu.carvicim.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

public class EmailCommandTest {

    private Model model;

    @Before
    public void setUp() {
        this.model = new ModelManager(getTypicalCarvicim(), new UserPrefs());
    }

    @Test
    public void executeEmail() {
        EmailCommand emailCommand = new EmailCommand();
        CommandResult commandResult = emailCommand.executeUndoableCommand();
        assertEquals(EmailCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
    }

}
