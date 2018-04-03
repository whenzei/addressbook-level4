package seedu.carvicim.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.carvicim.commons.core.Messages.MESSAGE_EMPLOYEE_IS_ASSIGNED;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.carvicim.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.carvicim.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithAssignedJobs;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.person.Employee;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteEmployeeCommand}.
 */
public class DeleteEmployeeCommandTest {

    private Model model = new ModelManager(getTypicalCarvicim(), new UserPrefs());

    @Test
    public void execute_deleteFailure_employeeIsAssignedToJob() throws Exception {
        model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());

        Employee employeeToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(deleteEmployeeCommand, model, MESSAGE_EMPLOYEE_IS_ASSIGNED);

    }

    @Test
    public void execute_deleteSuccess_employeeNotAssignedToJob() throws Exception {
        model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());

        Employee employeeToDelete = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(INDEX_THIRD_PERSON);

        String expectedMessage = String.format(DeleteEmployeeCommand.MESSAGE_DELETE_PERSON_SUCCESS, employeeToDelete);
        ModelManager expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.deletePerson(employeeToDelete);

        assertCommandSuccess(deleteEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Employee employeeToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteEmployeeCommand.MESSAGE_DELETE_PERSON_SUCCESS, employeeToDelete);

        ModelManager expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.deletePerson(employeeToDelete);

        assertCommandSuccess(deleteEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteEmployeeCommand, model, Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Employee employeeToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteEmployeeCommand.MESSAGE_DELETE_PERSON_SUCCESS, employeeToDelete);

        Model expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.deletePerson(employeeToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteEmployeeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of carvicim book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getCarvicim().getEmployeeList().size());

        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteEmployeeCommand, model, Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Employee employeeToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        // delete -> first employee deleted
        deleteEmployeeCommand.execute();
        undoRedoStack.push(deleteEmployeeCommand);

        // undo -> reverts addressbook back to previous state and filtered employee list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first employee deleted again
        expectedModel.deletePerson(employeeToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteEmployeeCommand not pushed into undoRedoStack
        assertCommandFailure(deleteEmployeeCommand, model, Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);

        // no COMMANDS in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Employee} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted employee in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the employee object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteEmployeeCommand deleteEmployeeCommand = prepareCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Employee employeeToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // delete -> deletes second employee in unfiltered employee list / first employee in filtered employee list
        deleteEmployeeCommand.execute();
        undoRedoStack.push(deleteEmployeeCommand);

        // undo -> reverts addressbook back to previous state and filtered employee list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deletePerson(employeeToDelete);
        assertNotEquals(employeeToDelete, model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));
        // redo -> deletes same second employee in unfiltered employee list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        DeleteEmployeeCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_PERSON);
        DeleteEmployeeCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteEmployeeCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different employee -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteEmployeeCommand} with the parameter {@code index}.
     */
    private DeleteEmployeeCommand prepareCommand(Index index) {
        DeleteEmployeeCommand deleteEmployeeCommand = new DeleteEmployeeCommand(index);
        deleteEmployeeCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteEmployeeCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
