package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.carvicim.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.TAG_DESC_MECHANIC;
import static seedu.carvicim.logic.commands.CommandTestUtil.TAG_DESC_TECHNICIAN;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_TAG_MECHANIC;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_TAG_TECHNICIAN;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.carvicim.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.carvicim.testutil.TypicalEmployees.AMY;
import static seedu.carvicim.testutil.TypicalEmployees.BOB;
import static seedu.carvicim.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.EditCommand;
import seedu.carvicim.logic.commands.RedoCommand;
import seedu.carvicim.logic.commands.UndoCommand;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;
import seedu.carvicim.model.tag.Tag;
import seedu.carvicim.testutil.EmployeeBuilder;
import seedu.carvicim.testutil.PersonUtil;

public class EditCommandSystemTest extends CarvicimSystemTest {

    @Test
    public void edit() throws Exception {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_PERSON;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BOB + "  "
                + PHONE_DESC_BOB + " " + EMAIL_DESC_BOB + "  " + " " + TAG_DESC_MECHANIC + " ";
        Employee editedEmployee = new EmployeeBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_MECHANIC).build();
        assertCommandSuccess(command, index, editedEmployee);

        /* Case: undo editing the last employee in the list -> last employee restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last employee in the list -> last employee edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updatePerson(
                getModel().getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), editedEmployee);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a employee with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_TECHNICIAN + TAG_DESC_MECHANIC;
        assertCommandSuccess(command, index, BOB);

        /* Case: edit some fields -> edited */
        index = INDEX_FIRST_PERSON;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + TAG_DESC_TECHNICIAN;
        Employee employeeToEdit = getModel().getFilteredPersonList().get(index.getZeroBased());
        editedEmployee = new EmployeeBuilder(employeeToEdit).withTags(VALID_TAG_TECHNICIAN).build();
        assertCommandSuccess(command, index, editedEmployee);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_PERSON;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        editedEmployee = new EmployeeBuilder(employeeToEdit).withTags().build();
        assertCommandSuccess(command, index, editedEmployee);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered employee list, edit index within bounds of carvicim book and employee list -> edited */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_PERSON;
        assertTrue(index.getZeroBased() < getModel().getFilteredPersonList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        employeeToEdit = getModel().getFilteredPersonList().get(index.getZeroBased());
        editedEmployee = new EmployeeBuilder(employeeToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedEmployee);

        /* Case: filtered employee list, edit index within bounds of carvicim book but out of bounds of employee list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getCarvicim().getEmployeeList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* ------------------- Performing edit operation while a employee card is selected ------------------------ */

        /* Case: selects first card in the employee list, edit a employee -> edited, card selection
         * remains unchanged but browser url changes
         */
        showAllPersons();
        index = INDEX_FIRST_PERSON;
        selectPerson(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + TAG_DESC_TECHNICIAN;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new employee's name
        assertCommandSuccess(command, index, AMY, index);

        /* -------------------------------- Performing invalid edit operation ------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredPersonList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + INVALID_EMAIL_DESC, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: edit a employee with new values same as another employee's values -> rejected */
        executeCommand(PersonUtil.getAddCommand(BOB));
        assertTrue(getModel().getCarvicim().getEmployeeList().contains(BOB));
        index = INDEX_FIRST_PERSON;
        assertFalse(getModel().getFilteredPersonList().get(index.getZeroBased()).equals(BOB));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_TECHNICIAN + TAG_DESC_MECHANIC;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: edit a employee with new values same as another employee's values
         but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_MECHANIC;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Employee, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Employee, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Employee editedEmployee) {
        assertCommandSuccess(command, toEdit, editedEmployee, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the employee at index {@code toEdit} being
     * updated to values specified {@code editedEmployee}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Employee editedEmployee,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        try {
            expectedModel.updatePerson(
                    expectedModel.getFilteredPersonList().get(toEdit.getZeroBased()), editedEmployee);
            expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        } catch (DuplicateEmployeeException | EmployeeNotFoundException e) {
            throw new IllegalArgumentException(
                    "editedEmployee is a duplicate in expectedModel, or it isn't found in the model.");
        }

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedEmployee), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the command box has the error style.<br>
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
