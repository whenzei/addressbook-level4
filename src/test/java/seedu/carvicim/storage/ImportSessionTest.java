package seedu.carvicim.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;
import seedu.carvicim.storage.session.SheetParser;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;
import seedu.carvicim.storage.session.exceptions.InvalidDataException;

public class ImportSessionTest extends ImportSessionTestEnv {
    private static final String EMPLOYEE_EMAIL_FIELD = "employee email, ";

    @Test
    public void import_testFileWithErrorCorrection_success() throws Exception {
        setup(ERROR_INPUT_FILE, ERROR_RESULT_FILE, ERROR_OUTPUT_FILE);
        importAll();
        assertOutputResultFilesEqual();
        cleanup();
    }

    @Test
    public void import_testFileWithMultipleSheets_success() throws Exception {
        setup(MULTIPLE_INPUT_FILE, MULTIPLE_RESULT_FILE, MULTIPLE_OUTPUT_FILE);
        importAll();
        assertOutputResultFilesEqual();
        cleanup();
    }

    @Test
    public void import_testFileWithCorruptEntry_success() throws Exception {
        setup(CORRUPT_INPUT_FILE, CORRUPT_RESULT_FILE, CORRUPT_OUTPUT_FILE);
        importAll();
        assertOutputResultFilesEqual();
        cleanup();
    }

    @Test
    public void import_missingEmployeeEmail_failure() throws Exception {
        setup(MISSING_HEADER_FIELD_FILE, MISSING_HEADER_FIELD_FILE, MISSING_HEADER_FIELD_OUTPUT_FILE);
        try {
            importAll();
        } catch (InvalidDataException e) {
            assertEquals(SheetParser.ERROR_MESSAGE_MISSING_FIELDS + EMPLOYEE_EMAIL_FIELD, e.getMessage());
        } finally {
            cleanup();
        }
    }

    @Test
    public void import_notExcelSheet_failure() throws Exception {
        setup(NON_EXCEL_FILE, NON_EXCEL_FILE, NON_EXCEL_OUTPUT_FILE);
        try {
            importAll();
        } catch (FileFormatException e) {
            assertEquals(SessionData.ERROR_MESSAGE_FILE_FORMAT, e.getMessage());
        } finally {
            cleanup();
        }
    }

    /**
     * Imports all job entries from excel file by accepting them
     */
    private void importAll() throws FileAccessException, FileFormatException, CommandException, InvalidDataException {
        ClassLoader classLoader = getClass().getClassLoader();
        ImportSession importSession = ImportSession.getInstance();
        File inputFile = new File(inputPath);
        importSession.initializeSession(inputFile.getPath());
        importSession.getSessionData().reviewAllRemainingJobEntries(true, "");
        outputFilePath = importSession.closeSession();
        outputPath = classLoader.getResource(expectedOutputPath).getPath();
        testFile = new File(resultPath);
        outputFile = new File(outputFilePath);
        expectedOutputFile = new File(outputPath);
    }

}
