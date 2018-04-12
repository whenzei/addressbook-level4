package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithAssignedJobs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.ui.JumpToJobListRequestEvent;
import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (integration with the Model) for {@code SelectJobCommand}.
 */
public class SelectJobCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());
    }

    @Test
    public void execute_validJobNumberUnfilteredList_success() {
        assertExecutionSuccess(new JobNumber("1"));
        assertExecutionSuccess(new JobNumber("2"));
    }

    @Test
    public void execute_invalidJobNumberUnfilteredList_success() {
        assertExecutionFailure(new JobNumber("3"), Messages.MESSAGE_INVALID_JOB_NUMBER);
    }

    @Test
    public void equals() {
        SelectJobCommand selectJobNumberOneCommand = new SelectJobCommand(new JobNumber("1"));
        SelectJobCommand selectJobNumberTwoCommand = new SelectJobCommand(new JobNumber("2"));

        // same object -> returns true
        assertTrue(selectJobNumberOneCommand.equals(selectJobNumberOneCommand));

        // same values -> returns true
        SelectJobCommand selectJobNumberOneCommandCopy = new SelectJobCommand(new JobNumber("1"));
        assertTrue(selectJobNumberOneCommand.equals(selectJobNumberOneCommandCopy));

        // different types -> returns false
        assertFalse(selectJobNumberOneCommand.equals(1));

        // null -> returns false
        assertFalse(selectJobNumberOneCommand.equals(null));

        // different employee -> returns false
        assertFalse(selectJobNumberOneCommand.equals(selectJobNumberTwoCommand));

    }

    /**
     * Executes a {@code SelectJobCommand} with the given {@code jobNumber},
     * and checks that {@code JumpToJobListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(JobNumber jobNumber) {
        SelectJobCommand selectJobCommand = prepareCommand(jobNumber);

        try {
            CommandResult commandResult = selectJobCommand.execute();
            assertEquals(String.format(SelectJobCommand.MESSAGE_SELECT_JOB_SUCCESS, jobNumber),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToJobListRequestEvent lastEvent =
                (JumpToJobListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(selectJobCommand.getTargetIndex(), Index.fromZeroBased(lastEvent.targetIndex));
    }

    /**
     * Executes a {@code SelectJobCommand} with the given {@code jobNumber},
     * and checks that a {@code CommandException} is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(JobNumber jobNumber, String expectedMessage) {
        SelectJobCommand selectJobCommand = prepareCommand(jobNumber);

        try {
            selectJobCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectJobCommand} with parameters {@code jobNumber}.
     */
    private SelectJobCommand prepareCommand(JobNumber jobNumber) {
        SelectJobCommand selectJobCommand = new SelectJobCommand(jobNumber);
        selectJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return selectJobCommand;
    }
}
