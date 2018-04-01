package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.UndoRedoStackUtil.prepareStack;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.logic.commands.CommandTestUtil.deleteFirstPerson;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

public class RedoCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();
    private static final UndoRedoStack EMPTY_STACK = new UndoRedoStack();

    private final Model model = new ModelManager(getTypicalCarvicim(), new UserPrefs());
    private final DeleteEmployeeCommand deleteEmployeeCommandOne = new DeleteEmployeeCommand(INDEX_FIRST_PERSON);
    private final DeleteEmployeeCommand deleteEmployeeCommandTwo = new DeleteEmployeeCommand(INDEX_SECOND_PERSON);

    @Before
    public void setUp() throws Exception {
        deleteEmployeeCommandOne.setData(model, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
        deleteEmployeeCommandTwo.setData(model, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
        deleteEmployeeCommandOne.preprocessUndoableCommand();
        deleteEmployeeCommandTwo.preprocessUndoableCommand();
    }

    @Test
    public void execute() {
        UndoRedoStack undoRedoStack = prepareStack(
                Collections.emptyList(), Arrays.asList(deleteEmployeeCommandTwo, deleteEmployeeCommandOne));
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, EMPTY_COMMAND_HISTORY, undoRedoStack);
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());

        // multiple COMMANDS in redoStack
        deleteFirstPerson(expectedModel);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // single command in redoStack
        deleteFirstPerson(expectedModel);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // no command in redoStack
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }
}
