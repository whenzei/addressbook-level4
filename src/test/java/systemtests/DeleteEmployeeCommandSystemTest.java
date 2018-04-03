package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX;
import static seedu.carvicim.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.carvicim.logic.commands.DeleteEmployeeCommand.MESSAGE_DELETE_PERSON_SUCCESS;
import static seedu.carvicim.testutil.TestUtil.getLastIndex;
import static seedu.carvicim.testutil.TestUtil.getMidIndex;
import static seedu.carvicim.testutil.TestUtil.getPerson;
import static seedu.carvicim.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.DeleteEmployeeCommand;
import seedu.carvicim.logic.commands.RedoCommand;
import seedu.carvicim.logic.commands.UndoCommand;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;

public class DeleteEmployeeCommandSystemTest extends CarvicimSystemTest {

    private static final String MESSAGE_INVALID_DELETE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteEmployeeCommand.MESSAGE_USAGE);

    @Test
    public void delete() {
        /* ---------------- Performing delete operation while an unfiltered list is being shown ------------------- */

        /* Case: delete the first employee in the list, command with leading spaces and trailing spaces -> deleted */
        Model expectedModel = getModel();
        String command = "     " + DeleteEmployeeCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_PERSON.getOneBased() + "       ";
        Employee deletedEmployee = removePerson(expectedModel, INDEX_FIRST_PERSON);
        String expectedResultMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedEmployee);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete the last employee in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeDeletingLast);
        assertCommandSuccess(lastPersonIndex);

        /* Case: undo deleting the last employee in the list -> last employee restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting the last employee in the list -> last employee deleted again */
        command = RedoCommand.COMMAND_WORD;
        removePerson(modelBeforeDeletingLast, lastPersonIndex);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete the middle employee in the list -> deleted */
        Index middlePersonIndex = getMidIndex(getModel());
        assertCommandSuccess(middlePersonIndex);

        /* ---------------- Performing delete operation while a filtered list is being shown -------------------- */

        /* Case: filtered employee list, delete index within bounds of carvicim book and employee list -> deleted */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index index = INDEX_FIRST_PERSON;
        assertTrue(index.getZeroBased() < getModel().getFilteredPersonList().size());
        assertCommandSuccess(index);

        /* Case: filtered employee list, delete index within bounds of carvicim but out of bounds of employee list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getCarvicim().getEmployeeList().size();
        command = DeleteEmployeeCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* ------------------- Performing delete operation while a employee card is selected ---------------------- */

        /* Case: delete the selected employee -> employee list panel selects the employee before the deleted employee */
        showAllPersons();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectPerson(selectedIndex);
        command = DeleteEmployeeCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        deletedEmployee = removePerson(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedEmployee);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* ------------------------------- Performing invalid delete operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteEmployeeCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteEmployeeCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getCarvicim().getEmployeeList().size() + 1);
        command = DeleteEmployeeCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteEmployeeCommand.COMMAND_WORD
                + " abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteEmployeeCommand.COMMAND_WORD
                + " 1 abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Employee} at the specified {@code index} in {@code model}'s carvicim book.
     * @return the removed employee
     */
    private Employee removePerson(Model model, Index index) {
        Employee targetEmployee = getPerson(model, index);
        try {
            model.deletePerson(targetEmployee);
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("targetEmployee is retrieved from model.");
        }
        return targetEmployee;
    }

    /**
     * Deletes the employee at {@code toDelete} by creating a default
     * {@code DeleteEmployeeCommand} using {@code toDelete} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see DeleteEmployeeCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Index toDelete) {
        Model expectedModel = getModel();
        Employee deletedEmployee = removePerson(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedEmployee);

        assertCommandSuccess(DeleteEmployeeCommand.COMMAND_WORD + " "
                + toDelete.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the selected card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)}
     * @see DeleteEmployeeCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
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
