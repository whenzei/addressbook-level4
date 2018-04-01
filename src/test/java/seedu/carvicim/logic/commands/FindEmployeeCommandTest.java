package seedu.carvicim.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.carvicim.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.carvicim.testutil.TypicalEmployees.CARL;
import static seedu.carvicim.testutil.TypicalEmployees.ELLE;
import static seedu.carvicim.testutil.TypicalEmployees.FIONA;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

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
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindEmployeeCommand}.
 */
public class FindEmployeeCommandTest {
    private Model model = new ModelManager(getTypicalCarvicim(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindEmployeeCommand findFirstCommand = new FindEmployeeCommand(firstPredicate);
        FindEmployeeCommand findSecondCommand = new FindEmployeeCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindEmployeeCommand findFirstCommandCopy = new FindEmployeeCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different employee -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindEmployeeCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindEmployeeCommand command = prepareCommand("Kurz Elle Kunz");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));
    }

    /**
     * Parses {@code userInput} into a {@code FindEmploye
     * eCommand}.
     */
    private FindEmployeeCommand prepareCommand(String userInput) {
        FindEmployeeCommand command =
                new FindEmployeeCommand(new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Employee>} is equal to {@code expectedList}<br>
     *     - the {@code Carvicim} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindEmployeeCommand command,
                                      String expectedMessage, List<Employee> expectedList) {
        Carvicim expectedAddressBook = new Carvicim(model.getCarvicim());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getCarvicim());
    }
}
