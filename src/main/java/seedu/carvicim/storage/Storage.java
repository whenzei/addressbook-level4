package seedu.carvicim.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.carvicim.commons.events.model.CarvicimChangedEvent;
import seedu.carvicim.commons.events.storage.DataSavingExceptionEvent;
import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends CarvicimStorage, UserPrefsStorage, ArchiveJobStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getAddressBookFilePath();

    @Override
    Optional<ReadOnlyCarvicim> readCarvicim() throws DataConversionException, IOException;

    @Override
    void saveCarvicim(ReadOnlyCarvicim carvicm) throws IOException;

    @Override
    String getArchiveJobFilePath();

    @Override
    Optional<ReadOnlyCarvicim> readArchiveJob() throws DataConversionException, IOException;

    @Override
    void saveArchiveJob(ReadOnlyCarvicim carvicim) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleAddressBookChangedEvent(CarvicimChangedEvent abce);
}
