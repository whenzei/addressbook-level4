package seedu.carvicim.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_ASSIGNED_EMPLOYEE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_VEHICLE_NUMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.NameContainsKeywordsPredicate;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;
import seedu.carvicim.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing COMMANDS.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_TAG_MECHANIC = "mechanic";
    public static final String VALID_TAG_TECHNICIAN = "technician";
    public static final String VALID_VEHICLE_NUMBER_A = "SAT166A";
    public static final String VALID_VEHICLE_NUMBER_B = "ABC166Z";
    public static final String VALID_ASSIGNED_EMPLOYEE_INDEX_A = "1";
    public static final String VALID_ASSIGNED_EMPLOYEE_INDEX_B = "2";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String TAG_DESC_TECHNICIAN = " " + PREFIX_TAG + VALID_TAG_TECHNICIAN;
    public static final String TAG_DESC_MECHANIC = " " + PREFIX_TAG + VALID_TAG_MECHANIC;
    public static final String VEHICLE_NUMBER_DESC_ONE = " " + PREFIX_VEHICLE_NUMBER + VALID_VEHICLE_NUMBER_A;
    public static final String VEHICLE_NUMBER_DESC_TWO = " " + PREFIX_VEHICLE_NUMBER + VALID_VEHICLE_NUMBER_B;
    public static final String ASSIGNED_EMPLOYEE_INDEX_DESC_ONE = " " + PREFIX_ASSIGNED_EMPLOYEE
            + VALID_ASSIGNED_EMPLOYEE_INDEX_A;
    public static final String ASSIGNED_EMPLOYEE_INDEX_DESC_TWO = " " + PREFIX_ASSIGNED_EMPLOYEE
            + VALID_ASSIGNED_EMPLOYEE_INDEX_A + " " + PREFIX_ASSIGNED_EMPLOYEE + VALID_ASSIGNED_EMPLOYEE_INDEX_B;


    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "mechanic*"; // '*' not allowed in tags
    public static final String INVALID_VEHICLE_NUM_DESC = " " + PREFIX_VEHICLE_NUMBER; //empty string allowed
    public static final String INVALID_ASSIGNED_EMPLOYEE_INDEX_DESC = " " + PREFIX_ASSIGNED_EMPLOYEE + "-1";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_TECHNICIAN).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withTags(VALID_TAG_MECHANIC, VALID_TAG_TECHNICIAN).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the carvicim book and the filtered employee list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        Carvicim expectedAddressBook = new Carvicim(actualModel.getCarvicim());
        List<Employee> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedAddressBook, actualModel.getCarvicim());
            assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the employee at the given {@code targetIndex} in the
     * {@code model}'s carvicim book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Employee employee = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = employee.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Deletes the first employee in {@code model}'s filtered list from {@code model}'s carvicim book.
     */
    public static void deleteFirstPerson(Model model) {
        Employee firstEmployee = model.getFilteredPersonList().get(0);
        try {
            model.deletePerson(firstEmployee);
        } catch (EmployeeNotFoundException pnfe) {
            throw new AssertionError("Employee in filtered list must exist in model.", pnfe);
        }
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }
}
