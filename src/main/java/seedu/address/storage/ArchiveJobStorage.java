package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;

//@@author richardson0694
/**
 * Represents a storage for {@link seedu.address.model.AddressBook}.
 */
public interface ArchiveJobStorage {

    /**
     * Returns the file path of the data file.
     */
    String getArchiveJobFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyAddressBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyAddressBook> readArchiveJob() throws DataConversionException, IOException;

    /**
     * @see #getArchiveJobFilePath()
     */
    Optional<ReadOnlyAddressBook> readArchiveJob(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyAddressBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveArchiveJob(ReadOnlyAddressBook addressBook) throws IOException;

    /**
     * @see #saveArchiveJob(ReadOnlyAddressBook)
     */
    void saveArchiveJob(ReadOnlyAddressBook addressBook, String filePath) throws IOException;

}
