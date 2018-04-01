package seedu.carvicim.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;

//@@author richardson0694
/**
 * Represents a storage for {@link Carvicim}.
 */
public interface ArchiveJobStorage {

    /**
     * Returns the file path of the data file.
     */
    String getArchiveJobFilePath();

    /**
     * Returns Carvicim data as a {@link ReadOnlyCarvicim}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyCarvicim> readArchiveJob() throws DataConversionException, IOException;

    /**
     * @see #getArchiveJobFilePath()
     */
    Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyCarvicim} to the storage.
     * @param carvicim cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveArchiveJob(ReadOnlyCarvicim carvicim) throws IOException;

    /**
     * @see #saveArchiveJob(ReadOnlyCarvicim)
     */
    void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) throws IOException;

}
