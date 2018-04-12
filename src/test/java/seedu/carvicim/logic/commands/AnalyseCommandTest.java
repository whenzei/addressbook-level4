package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithJobs;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

//@@author richardson0694
public class AnalyseCommandTest {
    private Model model;
    private Model expectedModel;
    private AnalyseCommand analyseCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());

        analyseCommand = new AnalyseCommand();
        analyseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void executeSuccess() {
        String expectedMessage = analyseCommand.execute().feedbackToUser;
        assertCommandSuccess(analyseCommand, model, expectedMessage, expectedModel);
    }
}
