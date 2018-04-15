package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.carvicim.storage.session.SessionData.ERROR_MESSAGE_FILE_FORMAT;

import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

//@@author yuhongherald
public class ImportCommandTest extends ImportCommandTestEnv {

    @Test
    public void equals() throws Exception {
        String filePath = "CS2103-testsheet.xlsx";
        String altFilePath = "CS2103-testsheet-corrupt.xlsx";
        ImportCommand importCommand1 = prepareCommand(filePath);
        ImportCommand importCommand1Copy = prepareCommand(filePath);
        ImportCommand importCommand2 = prepareCommand(altFilePath);

        // same object -> returns true
        assertTrue(importCommand1.equals(importCommand1));

        // same values -> returns true
        assertTrue(importCommand1.equals(importCommand1Copy));

        // different types -> returns false
        assertFalse(importCommand1.equals(1));

        // different filepath -> return false
        assertFalse(importCommand1.equals(importCommand2));

        // null -> return false
        assertFalse(importCommand1.equals(null));
    }

    @Test
    public void execute_importValidExcelFile_success() throws Exception {
        Model expectedModel = new ModelManager();
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(ERROR_INPUT_FILE, ERROR_IMPORTED_FILE, ERROR_OUTPUT_FILE);

        ImportCommand command = prepareCommand(inputPath);
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_importInvalidExcelFile_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(NON_EXCEL_FILE, NON_EXCEL_FILE, NON_EXCEL_OUTPUT_FILE);
        ImportCommand command = prepareCommand(inputPath);
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_FILE_FORMAT, e.getMessage());
        }
    }

    /**
     * Returns ImportCommand with {@code filePath}, with default data
     */
    protected ImportCommand prepareCommand(String filePath) throws Exception {
        JobNumber.initialize(1);
        ImportCommand command = new ImportCommand(filePath);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
