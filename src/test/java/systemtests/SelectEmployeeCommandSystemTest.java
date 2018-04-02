package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX;
import static seedu.carvicim.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.carvicim.logic.commands.SelectEmployeeCommand.MESSAGE_SELECT_PERSON_SUCCESS;
import static seedu.carvicim.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalEmployees;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.RedoCommand;
import seedu.carvicim.logic.commands.SelectEmployeeCommand;
import seedu.carvicim.logic.commands.UndoCommand;
import seedu.carvicim.model.Model;

public class SelectEmployeeCommandSystemTest extends CarvicimSystemTest {
    @Test
    public void select() {
        /* ------------------------ Perform select operations on the shown unfiltered list -------------------------- */

        /* Case: select the first card in the employee list, command with leading spaces and trailing spaces
         * -> selected
         */
        String command = "   " + SelectEmployeeCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + "   ";
        assertCommandSuccess(command, INDEX_FIRST_PERSON);

        /* Case: select the last card in the employee list -> selected */
        Index personCount = Index.fromOneBased(getTypicalEmployees().size());
        command = SelectEmployeeCommand.COMMAND_WORD + " " + personCount.getOneBased();
        assertCommandSuccess(command, personCount);

        /* Case: undo previous selection -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo selecting last card in the list -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: select the middle card in the employee list -> selected */
        Index middleIndex = Index.fromOneBased(personCount.getOneBased() / 2);
        command = SelectEmployeeCommand.COMMAND_WORD + " " + middleIndex.getOneBased();
        assertCommandSuccess(command, middleIndex);

        /* Case: select the current selected card -> selected */
        assertCommandSuccess(command, middleIndex);

        /* ------------------------ Perform select operations on the shown filtered list ---------------------------- */

        /* Case: filtered employee list, select index within bounds of carvicim book but out of bounds of employee list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getCarvicim().getEmployeeList().size();
        assertCommandFailure(SelectEmployeeCommand.COMMAND_WORD + " " + invalidIndex,
                MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: filtered employee list, select index within bounds of carvicim book and employee list -> selected */
        Index validIndex = Index.fromOneBased(1);
        assertTrue(validIndex.getZeroBased() < getModel().getFilteredPersonList().size());
        command = SelectEmployeeCommand.COMMAND_WORD + " " + validIndex.getOneBased();
        assertCommandSuccess(command, validIndex);

        /* ----------------------------------- Perform invalid select operations ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(SelectEmployeeCommand.COMMAND_WORD + " " + 0,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEmployeeCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(SelectEmployeeCommand.COMMAND_WORD + " " + -1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEmployeeCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredPersonList().size() + 1;
        assertCommandFailure(SelectEmployeeCommand.COMMAND_WORD + " " + invalidIndex,
                MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(SelectEmployeeCommand.COMMAND_WORD + " abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEmployeeCommand.MESSAGE_USAGE));

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(SelectEmployeeCommand.COMMAND_WORD + " 1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEmployeeCommand.MESSAGE_USAGE));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeLeCte 1", MESSAGE_UNKNOWN_COMMAND);

        /* Case: select from empty carvicim book -> rejected */
        deleteAllPersons();
        assertCommandFailure(SelectEmployeeCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased(),
                MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected employee.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(
                MESSAGE_SELECT_PERSON_SUCCESS, expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getPersonListPanel().getSelectedCardIndex();

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpectedError(command, expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
