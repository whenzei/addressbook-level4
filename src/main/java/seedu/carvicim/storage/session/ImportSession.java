package seedu.carvicim.storage.session;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;

import seedu.carvicim.storage.session.exceptions.InvalidDataException;
import seedu.carvicim.storage.session.exceptions.UninitializedException;

//@@author yuhongherald
/**
 * Used to store data relevant to importing of {@code Job} from {@code inFile} and
 * exporting {@code Job} with commens to {@code outFile}. Implements a Singleton design pattern.
 */
public class ImportSession {

    public static final String ERROR_MESSAGE_EXPORT =
            "Unable to export file. Please close the application and try again.";
    public static final String CURRENT_DIRECTORY = ".";
    public static final String TEMP_SUFFIX = ".temp";
    private static final Logger logger = LogsCenter.getLogger(ImportSession.class);

    private static ImportSession importSession;

    private SessionData sessionData;

    private ImportSession() {
        sessionData = new SessionData();
    }

    public static ImportSession getInstance() {
        if (importSession == null) {
            logger.info("New ImportSession instance initialized");
            importSession = new ImportSession();
        }
        return importSession;
    }

    /**
     * Attempts to clean all the temp files in working directory. Does not clean all the .temp files, just
     * keeps their total size constant after close.
     */
    public static void cleanCache() {
        File folder = new File(CURRENT_DIRECTORY);
        File[] files = folder.listFiles();
        boolean success;
        logger.info("Beginning importSession cleanup:");
        for (File file : files) {
            success = true;
            if (file.getName().endsWith(TEMP_SUFFIX)) {
                success = file.delete();
            }
            if (!success) {
                logger.warning(String.format("File %s cannot be deleted", file.getAbsolutePath()));
            }
        }
    }

    public void setSessionData(SessionData sessionData) throws CommandException {
        logger.info("Attempting to set sessionData:");
        requireNonNull(sessionData);
        try {
            this.sessionData.closeWorkBook();
            sessionData.loadTempWorkBook();
            this.sessionData = sessionData;
        } catch (FileAccessException | FileFormatException | InvalidDataException e) {
            logger.warning("Failed to set session data: " + e.getMessage());
            throw new CommandException(e.getMessage());
        }
    }

    /**
     *  Opens excel file specified by {@code filepath} and initializes {@code SessionData} to support import operations
     */
    public void initializeSession(String filePath) throws FileAccessException, FileFormatException,
            InvalidDataException {
        sessionData.loadFile(filePath);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    /**
     * Flushes feedback to {@code pathToOutfile} and releases resources.
     */
    public String closeSession() throws CommandException {
        logger.info("Attempting to close session:");
        try {
            return sessionData.saveDataToSaveFile();
        } catch (IOException e) {
            logger.warning("IOException occurred while closing session.");
            throw new CommandException(ERROR_MESSAGE_EXPORT);
        } catch (UninitializedException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
