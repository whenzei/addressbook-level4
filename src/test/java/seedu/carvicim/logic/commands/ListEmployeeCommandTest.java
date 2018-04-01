package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListEmployeeCommand.
 */
public class ListEmployeeCommandTest {

    private Model model;
    private Model expectedModel;
    private ListEmployeeCommand listEmployeeCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        listEmployeeCommand = new ListEmployeeCommand();
        listEmployeeCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(listEmployeeCommand, model, ListEmployeeCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(listEmployeeCommand, model, ListEmployeeCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
