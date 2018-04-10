package seedu.carvicim.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;

//@@author yuhongherald
public class ImportSessionTest {
    private static final String TEST_INPUT_FILE = "storage/session/ImportSessionTest/CS2103-testsheet.xlsx";
    private static final String TEST_RESULT_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-results.xlsx";
    private static final String TEST_OUTPUT_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-comments.xlsx";
    private static final String EMPTY_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-empty.xls";
    private static final String EMPTY_OUTPUT_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-empty-comments.xls";
    private static final String EMPTY_FILE_MESSAGE =
            "Missing header fields: client name, client phone, client email, "
            + "vehicle number, employee name, employee phone, employee email, ";
    private static final String NO_JOBS_MESSAGE = "Sheet 1 contains no valid job entries!";
    private String inputPath;
    private String outputPath;
    private String testPath;
    private String emptyPath;
    private String emptyOutputPath;

    @Before
    public void cleanUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        inputPath = classLoader.getResource(TEST_INPUT_FILE).getPath();
        testPath = classLoader.getResource(TEST_RESULT_FILE).getPath();
        emptyPath = classLoader.getResource(EMPTY_FILE).getPath();
        try {
            outputPath = classLoader.getResource(TEST_OUTPUT_FILE).getPath();
            deleteFile(outputPath);
        } catch (NullPointerException e) {
            ;
        }
        try {
            emptyOutputPath = classLoader.getResource(EMPTY_OUTPUT_FILE).getPath();
            deleteFile(emptyOutputPath);
        } catch (NullPointerException e) {
            ;
        }
    }

    @Test
    public void importTestFileWithErrorCorrection() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        ImportSession importSession = ImportSession.getInstance();

        File inputFile = new File(inputPath);
        importSession.initializeSession(inputFile.getPath());
        importSession.getSessionData().reviewAllRemainingJobEntries(true, "");
        String outputFilePath = importSession.closeSession();
        outputPath = classLoader.getResource(TEST_OUTPUT_FILE).getPath();
        File testFile = new File(testPath);
        File outputFile = new File(outputFilePath);
        File expectedOutputFile = new File(outputPath);
        assertEquals(expectedOutputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        importSession.getSessionData().freeResources();
        assertExcelFilesEquals(testFile, outputFile);
        try {
            importSession.initializeSession(inputPath);
        } catch (FileFormatException e) {
            assertEquals(NO_JOBS_MESSAGE, e.getMessage());
        } finally {
            deleteFile(outputFilePath);
            importSession.getSessionData().freeResources();
        }
    }

    @Test
    public void importTestFileEmpty() throws FileAccessException {
        ImportSession importSession = ImportSession.getInstance();

        try {
            importSession.initializeSession(emptyPath);
        } catch (FileFormatException e) {
            assertEquals(EMPTY_FILE_MESSAGE, e.getMessage());
        } finally {
            importSession.getSessionData().freeResources();
        }
    }

    /**
     * Asserts that 2 excel files are equal:
     * The have the same number of sheets
     * In each sheet, they have the same number of rows
     * In each row, they have the same number of columns
     * In each cell, they have the same content
     */
    private void assertExcelFilesEquals(File file1, File file2) throws IOException, InvalidFormatException {
        Workbook workbook1 = WorkbookFactory.create(file1);
        Workbook workbook2 = WorkbookFactory.create(file2);
        assertEquals(workbook1.getNumberOfSheets(), workbook2.getNumberOfSheets());
        Sheet sheet1;
        Sheet sheet2;
        for (int i = 0; i < workbook1.getNumberOfSheets(); i++) {
            sheet1 = workbook1.getSheetAt(workbook1.getFirstVisibleTab() + i);
            sheet2 = workbook2.getSheetAt(workbook1.getFirstVisibleTab() + i);
            assertSheetEquals(sheet1, sheet2);
        }
    }

    /**
     * Asserts that 2 sheets are equal:
     * In each sheet, they have the same number of rows
     * In each row, they have the same number of columns
     * In each cell, they have the same content
     */
    private void assertSheetEquals(Sheet sheet1, Sheet sheet2) {
        assertEquals(sheet1.getPhysicalNumberOfRows(), sheet2.getPhysicalNumberOfRows());
        Row row1;
        Row row2;
        for (int i = sheet1.getFirstRowNum(); i <= sheet1.getLastRowNum(); i++) {
            row1 = sheet1.getRow(i);
            row2 = sheet2.getRow(i);
        }
    }

    /**
     * Asserts that 2 rows are equal:
     * In each row, they have the same number of columns
     * In each cell, they have the same content
     */
    private void assertRowEquals(Row row1, Row row2) {
        assertEquals(row1.getPhysicalNumberOfCells(), row2.getPhysicalNumberOfCells());
        Cell cell1;
        Cell cell2;
        for (int i = row1.getFirstCellNum(); i <= row1.getLastCellNum(); i++) {
            cell1 = row1.getCell(i);
            cell2 = row2.getCell(i);
        }
    }

    /**
     * Asserts that 2 cells are equal:
     * In each cell, they have the same content
     */
    private void assertCellEquals(Cell cell1, Cell cell2) {
        DataFormatter dataFormatter = new DataFormatter();
        assertEquals(dataFormatter.formatCellValue(cell1), dataFormatter.formatCellValue(cell2));
    }

    /**
     * Deletes file at (@String filePath) if file exists
     */
    private void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
