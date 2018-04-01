package seedu.carvicim.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;

/**
 * Represents a storage for {@link Carvicim}.
 */
public interface CarvicimStorage {

    /**
     * Returns the file path of the data file.
     */
    String getAddressBookFilePath();

    /**
     * Returns Carvicim data as a {@link ReadOnlyCarvicim}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyCarvicim> readCarvicim() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyCarvicim> readCarvicim(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyCarvicim} to the storage.
     * @param carvicm cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCarvicim(ReadOnlyCarvicim carvicm) throws IOException;

    /**
     * @see #saveCarvicim(ReadOnlyCarvicim)
     */
    void saveCarvicim(ReadOnlyCarvicim carvicim, String filePath) throws IOException;

}
