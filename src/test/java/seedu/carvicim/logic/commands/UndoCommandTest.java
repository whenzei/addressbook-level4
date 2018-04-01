package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.UndoRedoStackUtil.prepareStack;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.logic.commands.CommandTestUtil.deleteFirstPerson;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

public class UndoCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();
    private static final UndoRedoStack EMPTY_STACK = new UndoRedoStack();

    private final Model model = new ModelManager(getTypicalCarvicim(), new UserPrefs());
    private final DeleteEmployeeCommand deleteEmployeeCommandOne = new DeleteEmployeeCommand(INDEX_FIRST_PERSON);
    private final DeleteEmployeeCommand deleteEmployeeCommandTwo = new DeleteEmployeeCommand(INDEX_FIRST_PERSON);

    @Before
    public void setUp() {
        deleteEmployeeCommandOne.setData(model, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
        deleteEmployeeCommandTwo.setData(model, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
    }

    @Test
    public void execute() throws Exception {
        UndoRedoStack undoRedoStack = prepareStack(
                Arrays.asList(deleteEmployeeCommandOne, deleteEmployeeCommandTwo), Collections.emptyList());
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, EMPTY_COMMAND_HISTORY, undoRedoStack);
        deleteEmployeeCommandOne.execute();
        deleteEmployeeCommandTwo.execute();

        // multiple COMMANDS in undoStack
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        deleteFirstPerson(expectedModel);
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // single command in undoStack
        expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // no command in undoStack
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
    }
}
