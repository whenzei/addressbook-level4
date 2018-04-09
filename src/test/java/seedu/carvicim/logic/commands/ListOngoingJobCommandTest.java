package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithAssignedJobs;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

/**
 * Contains integration tests and unit tests for ListOngoingJobCommand
 */
public class ListOngoingJobCommandTest {
    private Model model;
    private Model expectedModel;
    private ListOngoingJobCommand listOngoingJobCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());
        expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        listOngoingJobCommand = new ListOngoingJobCommand();
        listOngoingJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(listOngoingJobCommand, model, ListOngoingJobCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
