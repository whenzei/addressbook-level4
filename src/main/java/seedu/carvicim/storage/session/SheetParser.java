package seedu.carvicim.storage.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import seedu.carvicim.storage.session.exceptions.FileFormatException;

//@@author yuhongherald
/**
 * a
 */
public class SheetParser {
    public static final String INVALID_FIELD = "INVALID_FIELD";
    public static final String ERROR_MESSAGE_MISSING_FIELDS = "Missing header fields: ";
    public static final String ERROR_MESSAGE_DUPLICATE_FIELDS = "Duplicate header field: ";

    // Compulsory header fields
    public static final String CLIENT_NAME = "client name";
    public static final String CLIENT_PHONE = "client phone";
    public static final String CLIENT_EMAIL = "client email";
    public static final String VEHICLE_NUMBER = "vehicle number";
    public static final String EMPLOYEE_NAME = "employee name";
    public static final String EMPLOYEE_PHONE = "employee phone";
    public static final String EMPLOYEE_EMAIL = "employee email";

    // Comment header fields
    public static final String APPROVAL_STATUS = "approval status";
    public static final String COMMENTS = "comments";
    public static final int APPROVAL_STATUS_INDEX = 0;
    public static final int COMMENTS_INDEX = 1;

    // Optional header fields
    // public static final String JOB_NUMBER = "job number";
    // public static final String DATE = "date";
    public static final String STATUS = "status";
    public static final String REMARKS = "remarks";


    public static final String[] JOB_ENTRY_COMPULSORY_FIELDS = { // ignore case when reading headings
        CLIENT_NAME, CLIENT_PHONE, CLIENT_EMAIL, VEHICLE_NUMBER, EMPLOYEE_NAME, EMPLOYEE_PHONE, EMPLOYEE_EMAIL
    };
    public static final String[] JOB_ENTRY_OPTIONAL_FIELDS = { // ignore case when reading headings
        STATUS, REMARKS
    };
    public static final String MESSAGE_SEPARATOR = ", ";

    private final Sheet sheet;
    private final ArrayList<String> missingCompulsoryFields;
    private final ArrayList<String> missingOptionalFields;
    private final HashMap<String, RowData> compulsoryFields;
    private final HashMap<String, RowData> commentFields;
    private final HashMap<String, RowData> optionalFields;


    public SheetParser(Sheet sheet) {
        this.sheet = sheet;
        missingCompulsoryFields = new ArrayList<>(
                Arrays.asList(JOB_ENTRY_COMPULSORY_FIELDS));
        missingOptionalFields =  new ArrayList<>(
                Arrays.asList(JOB_ENTRY_OPTIONAL_FIELDS));
        compulsoryFields = new HashMap<>();
        commentFields = new HashMap<>();
        optionalFields = new HashMap<>();
    }

    /**
     * Reads the (@code Sheet) and converts it into (@code SheetWithHeaderFields)
     */
    public SheetWithHeaderFields parseSheetWithHeaderField() throws FileFormatException {
        parseFirstRow();
        if (!missingCompulsoryFields.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(ERROR_MESSAGE_MISSING_FIELDS);
            for (String field : missingCompulsoryFields) {
                stringBuilder.append(field);
                stringBuilder.append(MESSAGE_SEPARATOR);
            }
            throw new FileFormatException(stringBuilder.toString());
        }
        createCommentField(APPROVAL_STATUS, APPROVAL_STATUS_INDEX);
        createCommentField(COMMENTS, COMMENTS_INDEX);
        return new SheetWithHeaderFields(sheet, compulsoryFields, commentFields, optionalFields);
    }

    /**
     * Creates a new column with header (@code name) (@code offset) columns after last column.
     * @param offset
     */
    private void createCommentField(String name, int offset) {
        int index = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() + offset;
        commentFields.put(name, new RowData(index, index));
    }

    /**
     * Processes the header fields in the first row into (@code headerFields) and throws (@code FileFormatException)
     * if there are missing compulsory header fields
     */
    private void parseFirstRow() throws FileFormatException {
        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        DataFormatter dataFormatter = new DataFormatter();
        int lastFieldIndex = firstRow.getLastCellNum();
        String lastField = INVALID_FIELD;
        String currentField;
        // traverse the row from the back to assist detecting end of row
        for (int i = firstRow.getLastCellNum(); i >= firstRow.getFirstCellNum(); i--) {
            currentField = dataFormatter.formatCellValue(firstRow.getCell(i)).toLowerCase();
            if (currentField.equals(lastField)) {
                continue;
            }
            if (!isFieldPresent(currentField)) {
                lastField = INVALID_FIELD;
                lastFieldIndex = i - 1;
                continue;
            }
            addHeaderField(currentField, new RowData(i, lastFieldIndex));
            lastField = currentField;
            lastFieldIndex = i - 1;
        }
    }

    /**
     * Removes header field from (@code missingCompulsoryFields) or (@code missingOptionalFields) and
     * places it into (@code headerFields)
     */
    private void addHeaderField(String currentField, RowData rowData) throws FileFormatException {
        if (missingCompulsoryFields.contains(currentField)) {
            missingCompulsoryFields.remove(currentField);
            compulsoryFields.put(currentField, rowData);
        } else if (missingOptionalFields.contains(currentField)) {
            missingOptionalFields.remove(currentField);
            optionalFields.put(currentField, rowData);
        } else {
            throw new FileFormatException(ERROR_MESSAGE_DUPLICATE_FIELDS + currentField);
        }
    }

    /**
     * Checks if (@code field) is present in (@code fields), ignoring case
     */
    private boolean isFieldPresent(String field) {
        return missingCompulsoryFields.contains(field) || missingOptionalFields.contains(field);
    }
}
