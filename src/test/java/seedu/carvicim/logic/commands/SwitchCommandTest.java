package seedu.carvicim.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;

//@@author yuhongherald
public class SwitchCommandTest {
    @Test
    public void execute_switch_success() throws CommandException {
        SwitchCommand command = prepareCommand();
        command.execute();
        Model expectedModel = new ModelManager();
        expectedModel.switchJobView();
        assertEquals(expectedModel.isViewingImportedJobs(), command.model.isViewingImportedJobs());
    }

    private SwitchCommand prepareCommand() {
        SwitchCommand command = new SwitchCommand();
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
