package seedu.carvicim.storage.session;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;

//@@author yuhongherald
/**
 * Represents a field that spans one or more columns
 */
public class RowData implements ExcelColumnSpannable {
    private final int startIndex;
    private final int endIndex;

    public RowData(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public int getEndIndex() {
        return endIndex;
    }

    @Override
    public ArrayList<String> readData(Workbook workbook, int sheetNumber, int rowNumber)
            throws DataIndexOutOfBoundsException {
        if (sheetNumber < workbook.getFirstVisibleTab()
                || sheetNumber >= workbook.getNumberOfSheets() + workbook.getFirstVisibleTab()) {
            throw new DataIndexOutOfBoundsException("Sheets", workbook.getFirstVisibleTab(),
                    workbook.getNumberOfSheets() + workbook.getFirstVisibleTab(), sheetNumber);
        }
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        return readDataFromSheet(sheet, rowNumber);
    }

    @Override
    public ArrayList<String> readDataFromSheet(Sheet sheet, int rowNumber)
        throws DataIndexOutOfBoundsException {
        if (rowNumber < sheet.getFirstRowNum() || rowNumber > sheet.getLastRowNum()) {
            throw new DataIndexOutOfBoundsException("Rows", sheet.getFirstRowNum(), sheet.getLastRowNum(), rowNumber);
        }
        Row row = sheet.getRow(rowNumber);
        ArrayList<String> data = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        for (int i = startIndex; i <= endIndex; i++) {
            data.add(dataFormatter.formatCellValue(row.getCell(i)));
        }
        return data;
    }
}
