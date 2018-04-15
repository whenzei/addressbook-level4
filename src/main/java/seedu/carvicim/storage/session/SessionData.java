package seedu.carvicim.storage.session;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.ss.usermodel.WorkbookFactory;

import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;
import seedu.carvicim.storage.session.exceptions.InvalidDataException;
import seedu.carvicim.storage.session.exceptions.UninitializedException;

//@@author yuhongherald
/**
 * A data structure used to keep track of job entries in an {@code ImportSession}
 */
public class SessionData {
    public static final String ERROR_MESSAGE_INVALID_FILEPATH = "Please check the path to your file.";
    public static final String ERROR_MESSAGE_READ_PERMISSION = "Please enable file read permission.";
    public static final String ERROR_MESSAGE_FILE_FORMAT = "Unable to read the format of file. "
            + "Please ensure the file is in .xls or .xlsx format";
    public static final String ERROR_MESSAGE_IO_EXCEPTION = "Unable to read file. Please close the file and try again.";
    public static final String ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST = "There are no unreviewed job entries left!";

    public static final String FILE_PATH_CHARACTER = "/";
    public static final String SAVEFILE_SUFFIX = "-comments";
    public static final String ERROR_MESSAGE_UNINITIALIZED = "There is no imported file to save!";
    public static final String XLS_SUFFIX = ".xls";
    public static final String XLSX_SUFFIX = ".xlsx";
    public static final String TEMPFILE_NAME = "comments.temp";
    public static final String TEMPWORKBOOKFILE_NAME = "workbook.temp";
    public static final String ERROR_MESSAGE_INVALID_JOB_INDEX = "Job index not found!";
    public static final String TIMESTAMP_REGEX = "yyyy.MM.dd.HH.mm.ss.SSS";
    public static final String CURRENT_DIRECTORY = ".";

    private static final Logger logger = LogsCenter.getLogger(SessionData.class);

    private final ArrayList<JobEntry> unreviewedJobEntries;
    private final ArrayList<JobEntry> reviewedJobEntries;
    private final ArrayList<SheetWithHeaderFields> sheets;

    private File importFile;
    private Workbook workbook; // write comments to column after last row, with approval status
    private File tempFile;
    private File saveFile;
    private File tempWorkbookFile;

    public SessionData() {
        unreviewedJobEntries = new ArrayList<>();
        reviewedJobEntries = new ArrayList<>();
        sheets = new ArrayList<>();
    }

    public SessionData(File importFile, File tempFile, File saveFile) {
        this.unreviewedJobEntries = new ArrayList<>();
        this.reviewedJobEntries = new ArrayList<>();
        this.sheets = new ArrayList<>();
        this.importFile = importFile;
        this.tempFile = tempFile;
        this.saveFile = saveFile;
        // workbook and sheets have to be rewritten to file on undo
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(TIMESTAMP_REGEX).format(new Date());
    }

    /**
     * Creates a copy of sessionData and returns it
     */
    public SessionData createCopy() throws CommandException {
        SessionData other;
        tempFile = new File(CURRENT_DIRECTORY + getTimeStamp() + TEMPFILE_NAME);
        try {
            logger.info("Attempting to save file: " + tempFile.getAbsolutePath());
            saveDataToFile(tempFile);
        } catch (IOException e) {
            logger.warning("IOException occured while saving file");
            throw new CommandException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (UninitializedException e) {
            tempFile = null; // no data to save
        }
        other = new SessionData(importFile, tempFile, saveFile);
        return other;
    }

    public boolean isInitialized() {
        return (workbook != null);
    }

    /*===================================================================
    savefile methods
    ===================================================================*/

    /**
     * Creates a file using relative filePath of {@code importFile}, then appending a SAVEFILE_SUFFIX
     */
    private File generateSaveFile() {
        requireNonNull(importFile);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(importFile.getParent());
        stringBuilder.append(FILE_PATH_CHARACTER);
        String fullFillName = importFile.getName();
        String fileName;
        String suffix = "";
        if (fullFillName.endsWith(XLS_SUFFIX)) {
            suffix = XLS_SUFFIX;
        } else if (fullFillName.endsWith(XLSX_SUFFIX)) {
            suffix = XLSX_SUFFIX;
        }
        fileName = fullFillName.substring(0, fullFillName.length() - suffix.length());

        stringBuilder.append(fileName);
        stringBuilder.append(SAVEFILE_SUFFIX);
        stringBuilder.append(suffix);
        return new File(stringBuilder.toString());
    }

    /*===================================================================
    Initialization methods
    ===================================================================*/

    /**
     * Attempts load file specified at {@code filePath} if there is no currently open file and
     * specified file exists, is readable and is an excel file
     */
    public void loadFile(String filePath) throws FileAccessException, FileFormatException, InvalidDataException {
        if (isInitialized()) {
            // this check has been removed, can import to overwrite current import session
        }
        logger.info("Attempting to load file: " + filePath);
        freeResources(); // from previous session
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileAccessException(ERROR_MESSAGE_INVALID_FILEPATH);
        } else if (!file.canRead()) {
            logger.warning("Cannot read file: " + filePath);
            throw new FileAccessException(ERROR_MESSAGE_READ_PERMISSION);
        }
        importFile = file;

        try {
            setWorkBook(file);
        } catch (InvalidFormatException | IllegalArgumentException e) {
            throw new FileFormatException(ERROR_MESSAGE_FILE_FORMAT);
        } catch (IOException e) {
            throw new FileFormatException(ERROR_MESSAGE_IO_EXCEPTION);
        }
        initializeSessionData();
    }

    /**
     * Loads a workbook from a previous snapshot
     */
    public void loadTempWorkBook() throws FileAccessException, FileFormatException, InvalidDataException {
        if (tempFile == null) {
            return;
        }
        logger.info("Loading temp workbook from: " + tempFile.getAbsolutePath());
        if (saveFile.exists() && !saveFile.delete()) {
            logger.warning("Unable to overwrite save file: " + saveFile.getAbsolutePath());
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        }
        try {
            setWorkBook(tempFile);
        } catch (IOException e) {
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (InvalidFormatException | IllegalArgumentException e) {
            throw new FileFormatException(ERROR_MESSAGE_FILE_FORMAT);
        } finally {
            tempFile.delete();
            tempFile = null;
        }
        initializeSessionData();
    }

    /**
     * Attempts to create and set {@code Workbook} for a given {@code File}
     */
    private void setWorkBook(File file) throws IOException, InvalidFormatException, IllegalArgumentException {
        logger.info("Setting workbook from file: " + file);
        requireNonNull(file);
        saveFile = generateSaveFile();
        if (saveFile.exists()) {
            logger.info("Save file detected: " + saveFile);
            try {
                workbook = WorkbookFactory.create(saveFile);
            } catch (EmptyFileException e) {
                logger.warning("Failed to load save file: " + saveFile);
                saveFile.delete();
                saveFile = null;
                setWorkBook(file);
            }
        } else {
            workbook = WorkbookFactory.create(file);
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook = WorkbookFactory.create(saveFile);
        }
    }

    /**
     * Attempts to parse the column headers and retrieve job entries
     */
    public void initializeSessionData() throws InvalidDataException {
        SheetWithHeaderFields sheetWithHeaderFields;
        SheetParser sheetParser;
        Sheet sheet;
        logger.info("Setting session data");
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(workbook.getFirstVisibleTab() + i);
            sheetParser = new SheetParser(sheet);
            try {
                sheetWithHeaderFields = sheetParser.parseSheetWithHeaderField();
                addSheet(sheetWithHeaderFields);
            } catch (FileFormatException e) {
                logger.warning("Suppressed error while importing sheet " + i + " : " + e.getMessage());
                sheets.add(null);
            }
        }
    }

    public String saveDataToSaveFile() throws IOException, UninitializedException {
        return saveDataToFile(saveFile);
    }

    /**
     * Saves feedback to {@code file}
     */
    public String saveDataToFile(File file) throws IOException, UninitializedException {
        requireNonNull(file);
        if (!isInitialized()) {
            throw new UninitializedException(ERROR_MESSAGE_UNINITIALIZED);
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        String path = file.getAbsolutePath();
        workbook.write(fileOut);
        fileOut.close();
        return path;
    }

    /**
     * Releases resources associated with ImportSession by nulling field
     */
    public void freeResources() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                logger.warning("Failed to close workbook"); // can't close it, but application is already closing
            }
        }
        workbook = null;
        importFile = null;
        if (tempFile != null && !tempFile.delete()) {
            logger.warning("Failed to delete tempFile " + tempFile.getAbsolutePath());
        }
        tempFile = null;
        saveFile = null;
        unreviewedJobEntries.clear();
        reviewedJobEntries.clear();
        sheets.clear();
    }

    /**
     * Attempts to close {@code workBook} so that the file associated can be modified
     */
    public void closeWorkBook() throws FileAccessException {
        if (workbook == null) {
            return;
        }
        File newFile = new File(CURRENT_DIRECTORY + getTimeStamp() + TEMPWORKBOOKFILE_NAME);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (IOException e) {
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        } finally {
            try {
                workbook.close();
                newFile.delete();
            } catch (IOException e) {
                throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
            }
        }
    }

    /**
     * Adds job entries from {@code sheetWithHeaderFields} into {@code SessionData}
     */
    public void addSheet(SheetWithHeaderFields sheetWithHeaderFields) {
        Iterator<JobEntry> jobEntryIterator = sheetWithHeaderFields.iterator();
        JobEntry jobEntry;
        while ((jobEntry = jobEntryIterator.next()) != null) {
            unreviewedJobEntries.add(jobEntry);
        }
        sheets.add(sheetWithHeaderFields.getSheetIndex(), sheetWithHeaderFields);
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
    public List<Job> getReviewedJobEntries() { // marked for deletion
        return Collections.unmodifiableList(reviewedJobEntries);
    }

    /**
     * Reviews all remaining jobs using {@code reviewJobEntry}. Writes to {@code saveFile} when done.
     */
    public ArrayList<JobEntry> reviewAllRemainingJobEntries(boolean approved, String comments) throws CommandException {
        logger.info("Reviewing all remaining entries: ");
        ArrayList<JobEntry> reviewedEntries = new ArrayList<>();
        try {
            while (!getUnreviewedJobEntries().isEmpty()) {
                reviewedEntries.add(reviewJobEntry(0, approved, comments));
            }
        } catch (DataIndexOutOfBoundsException e) {
            logger.warning("Index went out of bounds before completion, should not happen: " + e.getMessage());
            throw new CommandException(e.getMessage());
        }

        try {
            saveDataToSaveFile();
        } catch (UninitializedException e) {
            unreviewJobEntries(reviewedEntries);
            throw new CommandException(e.getMessage());
        } catch (IOException e) {
            logger.warning("Unable to save to file: " + e.getMessage());
            unreviewJobEntries(reviewedEntries);
            throw new CommandException(ERROR_MESSAGE_IO_EXCEPTION);
        }
        for (JobEntry jobEntry: reviewedEntries) {
            jobEntry.confirmLastReview();
        }
        return reviewedEntries;
    }

    /**
     * Reviews a {@code JobEntry} specified by {@code listIndex}. Writes to {@code saveFile} when done.
     * @param jobIndex index of {@code JobEntry} in {@code unreviewedJobEntries}
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     * @return reviewed jobEntry
     */
    public JobEntry reviewJobEntryUsingJobIndex(int jobIndex, boolean approved, String comments)
            throws CommandException {
        logger.info("Reviewing job index " + jobIndex);
        if (unreviewedJobEntries.isEmpty()) {
            throw new CommandException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        }
        if (unreviewedJobEntries.size() < jobIndex || jobIndex <= 0) {
            throw new CommandException(ERROR_MESSAGE_INVALID_JOB_INDEX);
        }
        JobEntry entry = unreviewedJobEntries.get(Index.fromOneBased(jobIndex).getZeroBased());
        try {
            reviewJobEntry(Index.fromOneBased(jobIndex).getZeroBased(), approved, comments);
        } catch (DataIndexOutOfBoundsException e) {
            logger.warning("Index went out of bounds :" + e.getMessage());
            throw new CommandException(e.getMessage());
        }
        try {
            saveDataToSaveFile();
        } catch (IOException e) {
            logger.warning("Unable to save to file: " + e.getMessage());
            unreviewJobEntry(entry);
            throw new CommandException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (UninitializedException e) {
            unreviewJobEntry(entry);
            throw new CommandException(ERROR_MESSAGE_UNINITIALIZED);
        }
        entry.confirmLastReview();
        return entry;
    }

    /**
     * Reviews a {@code JobEntry} specified by {@code listIndex}
     * @param listIndex index of {@code JobEntry} in {@code unreviewedJobEntries}
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     * @return reviewed jobEntry
     */
    private JobEntry reviewJobEntry(int listIndex, boolean approved, String comments) throws
            DataIndexOutOfBoundsException {
        if (unreviewedJobEntries.isEmpty()) {
            throw new IllegalStateException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        } else if (listIndex < 0 || listIndex >= unreviewedJobEntries.size()) {
            throw new DataIndexOutOfBoundsException(0, unreviewedJobEntries.size(), listIndex);
        }

        JobEntry jobEntry = unreviewedJobEntries.get(listIndex);
        jobEntry.review(approved, comments);
        unreviewedJobEntries.remove(jobEntry);
        reviewedJobEntries.add(jobEntry);
        SheetWithHeaderFields sheet = sheets.get(jobEntry.getSheetNumber());
        sheet.commentJobEntry(jobEntry.getRowNumber(), jobEntry.getCommentsAsString());
        if (approved) {
            sheet.approveJobEntry(jobEntry.getRowNumber());
        } else {
            sheet.rejectJobEntry(jobEntry.getRowNumber());
        }
        return jobEntry;
    }

    private void unreviewJobEntries(ArrayList<JobEntry> jobEntries) {
        for (JobEntry jobEntry : jobEntries) {
            unreviewJobEntry(jobEntry);
        }
    }

    /**
     * Reverses the reviewing process of {@code jobEntry} in the event that it cannot be written to file
     */
    private void unreviewJobEntry(JobEntry jobEntry) {
        sheets.get(jobEntry.getSheetNumber()).unreviewJobEntry(jobEntry.getRowNumber());
        jobEntry.unreviewLastComment();
        reviewedJobEntries.remove(jobEntry);
        unreviewedJobEntries.add(jobEntry);
    }
}
