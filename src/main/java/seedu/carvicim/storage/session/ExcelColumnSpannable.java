package seedu.carvicim.storage.session;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;

//@@author yuhongherald
/**
 * For fields that resides in one or more columns
 */
public interface ExcelColumnSpannable {
    public int getStartIndex();
    public int getEndIndex();
    public ArrayList<String> readData(Workbook workbook, int sheetNumber, int rowNumber)
            throws DataIndexOutOfBoundsException;
    public ArrayList<String> readDataFromSheet(Sheet sheet, int rowNumber) throws DataIndexOutOfBoundsException;
}
