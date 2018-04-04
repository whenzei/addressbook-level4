package seedu.carvicim.storage.session;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.ss.usermodel.WorkbookFactory;

import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;
import seedu.carvicim.storage.session.exceptions.InvalidDataException;
import seedu.carvicim.storage.session.exceptions.UnitializedException;

//@@author yuhongherald
/**
 * A data structure used to keep track of job entries in an (@code ImportSession)
 */
public class SessionData {
    public static final String ERROR_MESSAGE_FILE_OPEN = "An excel file is already open.";
    public static final String ERROR_MESSAGE_INVALID_FILEPATH = "Please check the path to your file.";
    public static final String ERROR_MESSAGE_READ_PERMISSION = "Please enable file read permission.";
    public static final String ERROR_MESSAGE_FILE_FORMAT = "Unable to read the format of file. "
            + "Please ensure the file is in .xls or .xlsx format";
    public static final String ERROR_MESSAGE_IO_EXCEPTION = "Unable to read file. Please close the file and try again.";
    public static final String ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST = "There are no unreviewed job entries left!";

    public static final String FILE_PATH_CHARACTER = "/";
    public static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss";
    public static final String SAVEFILE_SUFFIX = "";
    public static final String TEMPFILE_SUFFIX = "temp";
    public static final String ERROR_MESSAGE_UNITIALIZED = "There is no imported file to save!";

    private final ArrayList<JobEntry> jobEntries;
    private final ArrayList<JobEntry> unreviewedJobEntries;
    private final ArrayList<JobEntry> reviewedJobEntries;
    private final ArrayList<SheetWithHeaderFields> sheets;
    // will be using an ObservableList

    private File importFile;
    private File tempFile;
    private Workbook workbook; // write comments to column after last row, with approval status
    private File saveFile;


    public SessionData() {
        jobEntries = new ArrayList<>();
        unreviewedJobEntries = new ArrayList<>();
        reviewedJobEntries = new ArrayList<>();
        sheets = new ArrayList<>();
    }

    public SessionData(ArrayList<JobEntry> jobEntries, ArrayList<JobEntry> unreviewedJobEntries,
                       ArrayList<JobEntry> reviewedJobEntries,
                       ArrayList<SheetWithHeaderFields> sheets,
                       File importFile, File tempFile, Workbook workbook, File saveFile) {
        this.jobEntries = jobEntries;
        this.unreviewedJobEntries = unreviewedJobEntries;
        this.reviewedJobEntries = reviewedJobEntries;
        this.sheets = sheets;
        this.importFile = importFile;
        this.tempFile = tempFile;
        this.workbook = workbook;
        this.saveFile = saveFile;
    }

    /**
     * Creates a copy of sessionData and returns it
     */
    public SessionData createCopy() {
        SessionData other = new SessionData(new ArrayList<>(jobEntries), new ArrayList<>(unreviewedJobEntries),
                new ArrayList<>(reviewedJobEntries), new ArrayList<>(sheets), importFile,
                tempFile, workbook, saveFile);
        return other;
    }

    public boolean isInitialized() {
        return (workbook != null);
    }

    /*===================================================================
    savefile methods
    ===================================================================*/

    /**
     * Creates a file using relative filePath of (@code importFile), then appending a timestamp and
     * (@code appendedName) to make it unique
     */
    private File generateFile(String appendedName) {
        requireNonNull(importFile);
        String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(importFile.getParent());
        stringBuilder.append(FILE_PATH_CHARACTER);
        stringBuilder.append(timeStamp);
        stringBuilder.append(importFile.getName());
        stringBuilder.append(appendedName);
        return new File(stringBuilder.toString());
    }

    public String getSaveFilePath() {
        if (saveFile != null) {
            return saveFile.getPath();
        }
        saveFile = generateFile(SAVEFILE_SUFFIX);
        return saveFile.getPath();
    }

    /**
     * Sets the filePath using relative address from import file
     */
    public void setSaveFile(String filePath) {
        saveFile = new File(filePath);
    }

    /*===================================================================
    Initialization methods
    ===================================================================*/

    /**
     * Attempts load file specified at (@code filePath) if there is no currently open file and
     * specified file exists, is readable and is an excel file
     */
    public void loadFile(String filePath) throws FileAccessException, FileFormatException {
        if (isInitialized()) {
            throw new FileAccessException(ERROR_MESSAGE_FILE_OPEN);
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileAccessException(ERROR_MESSAGE_INVALID_FILEPATH);
        } else if (!file.canRead()) {
            throw new FileAccessException(ERROR_MESSAGE_READ_PERMISSION);
        }
        importFile = file;

        try {
            workbook = createWorkBook(file);
        } catch (InvalidFormatException e) {
            throw new FileFormatException(ERROR_MESSAGE_FILE_FORMAT);
        } catch (IOException e) {
            throw new FileFormatException(ERROR_MESSAGE_IO_EXCEPTION);
        }

        initializeSessionData();
    }

    /**
     * Attempts to create a (@code Workbook) for a given (@code File)
     */
    private Workbook createWorkBook(File file) throws IOException, InvalidFormatException {
        tempFile = generateFile(TEMPFILE_SUFFIX);
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        Workbook workbook = WorkbookFactory.create(file);
        workbook.write(fileOutputStream);
        workbook.close();
        workbook = WorkbookFactory.create(tempFile);
        return workbook;
    }
    /**
     * Attempts to parse the column headers and retrieve job entries
     */
    public void initializeSessionData() throws FileFormatException {
        SheetWithHeaderFields sheetWithHeaderFields;
        SheetParser sheetParser;
        Sheet sheet;

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(workbook.getFirstVisibleTab() + i);
            sheetParser = new SheetParser(sheet);
            sheetWithHeaderFields = sheetParser.parseSheetWithHeaderField();
            addSheet(sheetWithHeaderFields);
        }
    }

    /**
     * Saves feedback to specified saveFile path
     */
    public String saveData() throws IOException, UnitializedException {
        if (!isInitialized()) {
            throw new UnitializedException(ERROR_MESSAGE_UNITIALIZED);
        }
        for (JobEntry jobEntry : jobEntries) {
            SheetWithHeaderFields sheet = sheets.get(jobEntry.getSheetNumber());
            sheet.commentJobEntry(jobEntry.getRowNumber(), jobEntry.getCommentsAsString());
            if (jobEntry.isApproved()) {
                sheet.approveJobEntry(jobEntry.getRowNumber());
            } else {
                sheet.rejectJobEntry(jobEntry.getRowNumber());
            }
        }

        if (saveFile == null) { // does not check if a file exists
            saveFile = generateFile(SAVEFILE_SUFFIX);
        }
        FileOutputStream fileOut = new FileOutputStream(saveFile);
        String path = saveFile.getAbsolutePath();
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        freeResources();
        return path;
    }
    /**
     * Releases resources associated with ImportSession by nulling field
     */
    public void freeResources() {
        workbook = null;
        importFile = null;
        if (tempFile != null) {
            tempFile.delete();
        }
        tempFile = null;
        saveFile = null;
        unreviewedJobEntries.clear();
        reviewedJobEntries.clear();
        sheets.clear();
    }

    /*===================================================================
    Job review methods
    ===================================================================*/
    /**
     * @return a copy of unreviewed jobs stored in this sheet
     */
    public List<Job> getUnreviewedJobEntries() {
        return Collections.unmodifiableList(unreviewedJobEntries);
    }

    /**
     * @return a copy of reviewed jobs stored in this sheet
     */
    public List<Job> getReviewedJobEntries() {
        return Collections.unmodifiableList(reviewedJobEntries);
    }

    /**
     * Adds job entries from (@code sheetWithHeaderFields) into (@code SessionData)
     */
    public void addSheet(SheetWithHeaderFields sheetWithHeaderFields) {
        Iterator<JobEntry> jobEntryIterator = sheetWithHeaderFields.iterator();
        JobEntry jobEntry;
        while (jobEntryIterator.hasNext()) {
            jobEntry = jobEntryIterator.next();
            jobEntries.add(jobEntry);
            unreviewedJobEntries.add(jobEntry);
        }
        sheets.add(sheetWithHeaderFields.getSheetIndex(), sheetWithHeaderFields);
    }

    /**
     * Reviews all remaining jobs using (@code reviewJobEntry)
     */
    public void reviewAllRemainingJobEntries(boolean approved, String comments) throws DataIndexOutOfBoundsException {
        while (!getUnreviewedJobEntries().isEmpty()) {
            reviewJobEntry(0, approved, comments);
        }
    }

    /**
     * Reviews a (@code JobEntry) specified by (@code listIndex)
     * @param jobNumber index of (@code JobEntry) in (@code unreviewedJobEntries)
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     */
    public void reviewJobEntryUsingJobNumber(int jobNumber, boolean approved, String comments)
            throws DataIndexOutOfBoundsException, InvalidDataException {
        if (unreviewedJobEntries.isEmpty()) {
            throw new IllegalStateException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        }
        JobEntry entry;
        for (int i = 0; i < unreviewedJobEntries.size(); i++) {
            entry = unreviewedJobEntries.get(i);
            if (entry.getJobNumber().asInteger() == jobNumber) {
                reviewJobEntry(i, approved, comments);
                return;
            }
        }
        throw new InvalidDataException("Job number not found!");
    }

    /**
     * Reviews a (@code JobEntry) specified by (@code listIndex)
     * @param listIndex index of (@code JobEntry) in (@code unreviewedJobEntries)
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     */
    public void reviewJobEntry(int listIndex, boolean approved, String comments) throws DataIndexOutOfBoundsException {
        if (unreviewedJobEntries.isEmpty()) {
            throw new IllegalStateException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        } else if (listIndex < 0 || listIndex >= unreviewedJobEntries.size()) {
            throw new DataIndexOutOfBoundsException("Rows", 0, unreviewedJobEntries.size(), listIndex);
        }

        JobEntry jobEntry = unreviewedJobEntries.get(listIndex);
        jobEntry.review(approved, comments);
        unreviewedJobEntries.remove(jobEntry);
        if (approved) {
            reviewedJobEntries.add(jobEntry);
        }
    }
}
