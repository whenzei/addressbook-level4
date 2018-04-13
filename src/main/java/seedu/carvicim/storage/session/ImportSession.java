package seedu.carvicim.storage.session;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;

import seedu.carvicim.storage.session.exceptions.UninitializedException;

//@@author yuhongherald
/**
 * Used to store data relevant to importing of (@code Job) from (@code inFile) and
 * exporting (@code Job) with commens to (@code outFile). Implements a Singleton design pattern.
 */
public class ImportSession {

    public static final String ERROR_MESSAGE_EXPORT =
            "Unable to export file. Please close the application and try again.";
    private static ImportSession importSession;

    private SessionData sessionData;

    private ImportSession() {
        sessionData = new SessionData();
    }

    public static ImportSession getInstance() {
        if (importSession == null) {
            importSession = new ImportSession();
        }
        return importSession;
    }

    /**
     * Attempts to clean all the temp files in working directory
     */
    public static void cleanCache() {
        File folder = new File(".");
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".temp")) {
                file.delete();
            }
        }
    }

    public void setSessionData(SessionData sessionData) throws CommandException {
        requireNonNull(sessionData);
        try {
            this.sessionData.closeWorkBook();
            sessionData.loadTempWorkBook();
            this.sessionData = sessionData;
        } catch (FileAccessException | FileFormatException e) {
            //this.sessionData.reloadFile();
            throw new CommandException(e.getMessage());
        }
    }

    /**
     *  Opens excel file specified by (@code filepath) and initializes (@code SessionData) to support import operations
     */
    public void initializeSession(String filePath) throws FileAccessException, FileFormatException {
        sessionData.loadFile(filePath);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    /**
     * Flushes feedback to (@return pathToOutfile) and releases resources. Currently not persistent.
     */
    public String closeSession() throws CommandException {
        try {
            return sessionData.saveDataToSaveFile();
        } catch (IOException e) {
            throw new CommandException(ERROR_MESSAGE_EXPORT);
        } catch (UninitializedException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
