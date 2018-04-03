package seedu.carvicim.storage.session;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;
import seedu.carvicim.storage.session.exceptions.UnitializedException;

//@@author yuhongherald
/**
 * Used to store data relevant to importing of (@code Job) from (@code inFile) and
 * exporting (@code Job) with commens to (@code outFile). Implements a Singleton design pattern.
 */
public class ImportSession {

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

    public void setSessionData(SessionData sessionData) {
        requireNonNull(sessionData);
        this.sessionData = sessionData;
    }

    /**
     *  Opens excel file specified by (@code filepath) and initializes (@code SessionData) to support import operations
     */
    public void initializeSession(String filePath) throws FileAccessException, FileFormatException {
        sessionData.loadFile(filePath);
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
    public String closeSession() throws IOException, UnitializedException {
        return sessionData.saveData();
    }
}
