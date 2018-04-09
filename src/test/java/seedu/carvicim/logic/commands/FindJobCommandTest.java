package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.carvicim.commons.core.Messages.MESSAGE_JOBS_LISTED_OVERVIEW;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithAssignedJobs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobDetailsContainKeyWordsPredicate;

/**
 * Contains integration tests for {@code FindJobCommand}.
 */
public class FindJobCommandTest {
    private Model model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());

    @Test
    public void equals() {
        JobDetailsContainKeyWordsPredicate firstPredicate =
                 new JobDetailsContainKeyWordsPredicate(Collections.singletonList("first"));
        JobDetailsContainKeyWordsPredicate secondPredicate =
                 new JobDetailsContainKeyWordsPredicate(Collections.singletonList("second"));

        FindJobCommand findFirstCommand = new FindJobCommand(firstPredicate);
        FindJobCommand findSecondCommand = new FindJobCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindJobCommand findFirstCommandCopy = new FindJobCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different employee -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

    }

    @Test
    public void execute_zeroKeywords_noJobFound() {
        String expectedMessage = String.format(MESSAGE_JOBS_LISTED_OVERVIEW, 0);
        FindJobCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multipleJobsFound() {
        Job job1 = model.getFilteredJobList().get(0);
        Job job2 = model.getFilteredJobList().get(1);

        String expectedMessage = String.format(MESSAGE_JOBS_LISTED_OVERVIEW, 2);
        FindJobCommand command = prepareCommand("Apr Feb");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(job1, job2));
    }

    /**
     * Parses {@code userInput} into a {@code FindJobCommand}.
     */
    private FindJobCommand prepareCommand(String userInput) {
        FindJobCommand command =
                new FindJobCommand(new JobDetailsContainKeyWordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Job>} is equal to {@code expectedList}<br>
     *     - the {@code Carvicim} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindJobCommand command,
                                      String expectedMessage, List<Job> expectedList) {
        Carvicim expectedCarviciM = new Carvicim(model.getCarvicim());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredJobList());
        assertEquals(expectedCarviciM, model.getCarvicim());
    }
}
