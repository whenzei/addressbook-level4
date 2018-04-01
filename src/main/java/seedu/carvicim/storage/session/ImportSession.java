package seedu.carvicim.storage.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;

//@@author yuhongherald
/**
 * Used to store data relevant to importing of (@code Job) from (@code inFile) and
 * exporting (@code Job) with commens to (@code outFile). Implements a Singleton design pattern.
 */
public class ImportSession {
    public static final String ERROR_MESSAGE_FILE_OPEN = "An excel file is already open.";
    public static final String ERROR_MESSAGE_INVALID_FILEPATH = "Please check the path to your file.";
    public static final String ERROR_MESSAGE_READ_PERMISSION = "Please enable file read permission.";
    public static final String ERROR_MESSAGE_FILE_FORMAT = "Unable to read the format of file. "
            + "Please ensure the file is in .xls or .xlsx format";
    public static final String ERROR_MESSAGE_IO_EXCEPTION = "Unable to read file. Please close the file and try again.";
    private static ImportSession session;

    private boolean initialized;
    private File inFile;
    private File tempFile;
    private Workbook workbook; // write comments to column after last row, with approval status
    private SessionData sessionData;
    private File outFile;

    private ImportSession() {
        initialized = false;
    }

    public static ImportSession getInstance() {
        if (session == null) {
            session = new ImportSession();
        }
        return session;
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    /**
     * Returns whether (@code ImportSession) has been initialized with an excel spreadsheet
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     *  Opens excel file specified by (@code filepath) and initializes (@code SessionData) to support import operations
     */
    public void initializeSession(String filePath) throws FileAccessException, FileFormatException {
        if (inFile != null) {
            throw new FileAccessException(ERROR_MESSAGE_FILE_OPEN);
        }
        File file = new File (filePath);
        if (!file.exists()) {
            throw new FileAccessException(ERROR_MESSAGE_INVALID_FILEPATH);
        } else if (!file.canRead()) {
            throw new FileFormatException(ERROR_MESSAGE_READ_PERMISSION);
        }
        try {
            workbook = createWorkBook(file);
        } catch (InvalidFormatException e) {
            throw new FileFormatException(ERROR_MESSAGE_FILE_FORMAT);
        } catch (IOException e) {
            throw new FileFormatException(ERROR_MESSAGE_IO_EXCEPTION);
        }
        // the file is good to go
        inFile = file;
        initializeSessionData();
        initialized = true;
    }

    /**
     * Attempts to parse the column headers and retrieve job entries
     */
    private void initializeSessionData() throws FileFormatException {
        SheetWithHeaderFields sheetWithHeaderFields;
        SheetParser sheetParser;
        Sheet sheet;
        sessionData = new SessionData();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(workbook.getFirstVisibleTab() + i);
            sheetParser = new SheetParser(sheet);
            sheetWithHeaderFields = sheetParser.parseSheetWithHeaderField();
            sessionData.addSheet(sheetWithHeaderFields);
        }
    }

    /**
     * Attempts to create a (@code Workbook) for a given (@code File)
     */
    private Workbook createWorkBook(File file) throws IOException, InvalidFormatException {
        tempFile = new File(file.getPath() + getTimeStamp() + file.getName());
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        Workbook workbook = WorkbookFactory.create(file);
        workbook.write(fileOutputStream);
        workbook.close();
        workbook = WorkbookFactory.create(tempFile);
        return workbook;
    }

    public void reviewAllRemainingJobEntries(boolean approve) throws DataIndexOutOfBoundsException {
        sessionData.reviewAllRemainingJobEntries(approve, "Imported with no comments.");
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    /**
     * Flushes feedback to (@code outFile) and releases resources. Currently not persistent.
     */
    public void closeSession() throws DataIndexOutOfBoundsException, IOException {
        if (!initialized) {
            return;
        }
        if (outFile == null) { // does not check if a file exists
            String timeStamp = getTimeStamp();
            outFile = new File(inFile.getPath() + timeStamp + inFile.getName());
        }
        FileOutputStream fileOut = new FileOutputStream(outFile);
        System.out.println(outFile.getName());
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        tempFile.deleteOnExit();
        freeResources();
    }

    /**
     * Releases resources associated with ImportSession by nulling field
     */
    private void freeResources() {
        workbook = null;
        sessionData = null;
        inFile = null;
        outFile = null;
    }

    /**
     * For localized testing purposes
     */
    public static void main(String[] args) {
        ImportSession importSession = getInstance();
        String path;
        try {
            path = new File(".").getCanonicalPath();
            System.out.println(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            importSession.initializeSession(
                    ".\\src\\test\\resources\\model.session.ImportSessionTest\\CS2103-testsheet.xlsx");
            importSession.reviewAllRemainingJobEntries(true);
            importSession.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
