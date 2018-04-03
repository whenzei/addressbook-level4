package seedu.carvicim.logic.commands;

//@@author whenzei

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
 * Contains integration tests (interaction with the Model) and unit tests for ListJobCommand.
 */
public class ListJobCommandTest {

    private Model model;
    private Model expectedModel;
    private ListJobCommand listJobCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());
        expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        listJobCommand = new ListJobCommand();
        listJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showSameList() {
        assertCommandSuccess(listJobCommand, model, ListJobCommand.MESSAGE_SUCCESS, expectedModel);
    }



}
