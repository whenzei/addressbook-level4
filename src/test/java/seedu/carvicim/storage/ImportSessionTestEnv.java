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

import seedu.carvicim.storage.session.ImportSession;

//@@author yuhongherald

/**
 * Used to test classes that manipulate sessionData in importSession
 */
public abstract class ImportSessionTestEnv {
    protected static final String RESOURCE_PATH = "storage/session/ImportSessionTest/";
    protected static final String ERROR_INPUT_FILE = "CS2103-testsheet.xlsx";
    protected static final String ERROR_RESULT_FILE = "CS2103-testsheet-results.xlsx";
    protected static final String ERROR_OUTPUT_FILE = "CS2103-testsheet-comments.xlsx";
    protected static final String ERROR_IMPORTED_FILE = "CS2103-testsheet-import.xlsx";
    protected static final String MULTIPLE_INPUT_FILE = "CS2103-testsheet-multiple.xlsx";
    protected static final String MULTIPLE_RESULT_FILE = "CS2103-testsheet-multiple-results.xlsx";
    protected static final String MULTIPLE_OUTPUT_FILE = "CS2103-testsheet-multiple-comments.xlsx";
    protected static final String CORRUPT_INPUT_FILE = "CS2103-testsheet-corrupt.xlsx";
    protected static final String CORRUPT_RESULT_FILE = "CS2103-testsheet-corrupt-results.xlsx";
    protected static final String CORRUPT_OUTPUT_FILE = "CS2103-testsheet-corrupt-comments.xlsx";
    protected static final String NON_EXCEL_FILE = "non-excel-file.xlsx";
    protected static final String NON_EXCEL_OUTPUT_FILE = "non-excel-file-comments.xlsx";
    protected static final String MISSING_HEADER_FIELD_FILE = "missing-header-field.xlsx";
    protected static final String MISSING_HEADER_FIELD_OUTPUT_FILE = "missing_header_field-comments.xlsx";
    protected String expectedOutputPath;
    protected String inputPath;
    protected String outputPath;
    protected String resultPath;
    protected String outputFilePath;

    protected File testFile;
    protected File outputFile;
    protected File expectedOutputFile;

    /**
     * Attempts to delete -comments file if found
     */
    protected void cleanup() throws IOException {
        try {
            deleteFile(outputPath);
        } catch (NullPointerException e) {
            ;
        }
    }

    /**
     * asserts output file from importAll of input file is the same as result file
     */
    protected void assertOutputResultFilesEqual() throws Exception {
        assertEquals(expectedOutputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        ImportSession.getInstance().getSessionData().freeResources();
        assertExcelFilesEquals(testFile, outputFile);
    }

    protected void setup(String inputPath, String resultPath, String outputPath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        this.inputPath = classLoader.getResource(RESOURCE_PATH + inputPath).getPath();
        this.resultPath = classLoader.getResource(RESOURCE_PATH + resultPath).getPath();
        this.expectedOutputPath = RESOURCE_PATH + outputPath;
        try {
            this.outputPath = classLoader.getResource(expectedOutputPath).getPath();
            deleteFile(this.outputPath);
        } catch (NullPointerException e) {
            ;
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
        workbook1.close();
        workbook2.close();
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
     * Deletes file at {@code filePath} if file exists
     */
    private void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
