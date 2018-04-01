package seedu.carvicim.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.carvicim.commons.core.ComponentManager;
import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.events.model.CarvicimChangedEvent;
import seedu.carvicim.commons.events.storage.DataSavingExceptionEvent;
import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.UserPrefs;

/**
 * Manages storage of Carvicim data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private CarvicimStorage carvicimStorage;
    private UserPrefsStorage userPrefsStorage;
    private ArchiveJobStorage archiveJobStorage;

    public StorageManager(CarvicimStorage carvicimStorage, UserPrefsStorage userPrefsStorage,
                          ArchiveJobStorage archiveJobStorage) {
        super();
        this.carvicimStorage = carvicimStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.archiveJobStorage = archiveJobStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public String getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ Carvicim methods ==============================

    @Override
    public String getAddressBookFilePath() {
        return carvicimStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyCarvicim> readCarvicim() throws DataConversionException, IOException {
        return readCarvicim(carvicimStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyCarvicim> readCarvicim(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return carvicimStorage.readCarvicim(filePath);
    }

    @Override
    public void saveCarvicim(ReadOnlyCarvicim carvicm) throws IOException {
        saveCarvicim(carvicm, carvicimStorage.getAddressBookFilePath());
    }

    @Override
    public void saveCarvicim(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        carvicimStorage.saveCarvicim(carvicim, filePath);
    }


    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(CarvicimChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveCarvicim(event.data);
            saveArchiveJob(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    //@@author richardson0694
    // ================ ArchiveJob methods ==============================

    @Override
    public String getArchiveJobFilePath() {
        return archiveJobStorage.getArchiveJobFilePath();
    }

    @Override
    public Optional<ReadOnlyCarvicim> readArchiveJob() throws DataConversionException, IOException {
        return readArchiveJob(archiveJobStorage.getArchiveJobFilePath());
    }

    @Override
    public Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return archiveJobStorage.readArchiveJob(filePath);
    }

    @Override
    public void saveArchiveJob(ReadOnlyCarvicim carvicim) throws IOException {
        saveArchiveJob(carvicim, archiveJobStorage.getArchiveJobFilePath());
    }

    @Override
    public void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        archiveJobStorage.saveArchiveJob(carvicim, filePath);
    }

}
