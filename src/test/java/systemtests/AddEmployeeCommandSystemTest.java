package systemtests;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_TAG_TECHNICIAN;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.carvicim.testutil.TypicalEmployees.ALICE;
import static seedu.carvicim.testutil.TypicalEmployees.AMY;
import static seedu.carvicim.testutil.TypicalEmployees.BOB;
import static seedu.carvicim.testutil.TypicalEmployees.CARL;
import static seedu.carvicim.testutil.TypicalEmployees.HOON;
import static seedu.carvicim.testutil.TypicalEmployees.IDA;
import static seedu.carvicim.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.AddEmployeeCommand;
import seedu.carvicim.logic.commands.RedoCommand;
import seedu.carvicim.logic.commands.UndoCommand;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.tag.Tag;
import seedu.carvicim.testutil.EmployeeBuilder;
import seedu.carvicim.testutil.PersonUtil;

public class AddEmployeeCommandSystemTest extends CarvicimSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a employee without tags to a non-empty carvicim book, command with leading spaces and
         * trailing spaces -> added
         */
        Employee toAdd = AMY;
        String command = "   " + AddEmployeeCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + PHONE_DESC_AMY + " "
                + EMAIL_DESC_AMY + "   " + "   " + TAG_DESC_TECHNICIAN + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addPerson(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a employee with all fields same as another employee in the carvicim book except name -> added */
        toAdd = new EmployeeBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withTags(VALID_TAG_TECHNICIAN).build();
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + TAG_DESC_TECHNICIAN;
        assertCommandSuccess(command, toAdd);

        /* Case: add a employee with all fields same as another employee in the carvicim book except phone -> added */
        toAdd = new EmployeeBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
                .withTags(VALID_TAG_TECHNICIAN).build();
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_BOB
                + EMAIL_DESC_AMY + TAG_DESC_TECHNICIAN;
        assertCommandSuccess(command, toAdd);

        /* Case: add a employee with all fields same as another employee in the carvicim book except email -> added */
        toAdd = new EmployeeBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_BOB)
                .withTags(VALID_TAG_TECHNICIAN).build();
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_BOB + TAG_DESC_TECHNICIAN;
        assertCommandSuccess(command, toAdd);


        /* Case: add to empty carvicim book -> added */
        deleteAllPersons();
        assertCommandSuccess(ALICE);

        /* Case: add a employee with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddEmployeeCommand.COMMAND_WORD + TAG_DESC_TECHNICIAN + PHONE_DESC_BOB + NAME_DESC_BOB
                + TAG_DESC_MECHANIC + EMAIL_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /* Case: add a employee, missing tags -> added */
        assertCommandSuccess(HOON);

        /* ------------------------ Perform add operation on the shown filtered list ---------------------------- */

        /* Case: filters the employee list before adding -> added */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ----------------------- Perform add operation while a employee card is selected ------------------------- */

        /* Case: selects first card in the employee list, add a employee -> added, card selection remains unchanged */
        selectPerson(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* --------------------------------- Perform invalid add operations ------------------------------------- */

        /* Case: add a duplicate employee -> rejected */
        command = PersonUtil.getAddCommand(HOON);
        assertCommandFailure(command, AddEmployeeCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a duplicate employee except with different tags -> rejected */
        // "friends" is an existing tag used in the default model, see TypicalEmployees#ALICE
        // This test will fail if a new tag that is not in the model is used, see the bug documented in
        // Carvicim#addEmployee(Employee)
        command = PersonUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "mechanic";
        assertCommandFailure(command, AddEmployeeCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: missing name -> rejected */
        command = AddEmployeeCommand.COMMAND_WORD + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE));

        /* Case: missing phone -> rejected */
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_AMY + EMAIL_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE));

        /* Case: missing email -> rejected */
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + PersonUtil.getPersonDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddEmployeeCommand.COMMAND_WORD + INVALID_NAME_DESC + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_PHONE_DESC + EMAIL_DESC_AMY;
        assertCommandFailure(command, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + INVALID_EMAIL_DESC;
        assertCommandFailure(command, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddEmployeeCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddEmployeeCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddEmployeeCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Employee toAdd) {
        assertCommandSuccess(PersonUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Employee)}. Executes {@code command}
     * instead.
     * @see AddEmployeeCommandSystemTest#assertCommandSuccess(Employee)
     */
    private void assertCommandSuccess(String command, Employee toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addPerson(toAdd);
        } catch (DuplicateEmployeeException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddEmployeeCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Employee)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddEmployeeCommandSystemTest#assertCommandSuccess(String, Employee)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
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
