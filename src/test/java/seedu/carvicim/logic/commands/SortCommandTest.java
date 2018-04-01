package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.testutil.TypicalEmployees.getCarvicimNonAlphabetically;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

//@@author richardson0694
public class SortCommandTest {
    private Model model;
    private Model expectedModel;
    private SortCommand sortCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getCarvicimNonAlphabetically(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());

        sortCommand = new SortCommand();
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void executeSuccess() {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
