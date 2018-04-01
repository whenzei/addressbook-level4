package seedu.carvicim.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.commons.util.FileUtil;
import seedu.carvicim.model.ReadOnlyCarvicim;

//@@author richardson0694
/**
 * A class to access Archive data stored as an xml file on the hard disk.
 */
public class XmlArchiveJobStorage implements ArchiveJobStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlArchiveJobStorage.class);

    private String filePath;

    public XmlArchiveJobStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getArchiveJobFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCarvicim> readArchiveJob() throws DataConversionException, IOException {
        return readArchiveJob(filePath);
    }

    /**
     * Similar to {@link #readArchiveJob()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File archiveJobFile = new File(filePath);

        if (!archiveJobFile.exists()) {
            logger.info("ArchiveJob file "  + archiveJobFile + " not found");
            return Optional.empty();
        }

        XmlSerializableArchiveJob xmlArchiveJob = XmlFileStorage.loadDataFromArchiveFile(new File(filePath));
        try {
            return Optional.of(xmlArchiveJob.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + archiveJobFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveArchiveJob(ReadOnlyCarvicim carvicim) throws IOException {
        saveArchiveJob(carvicim, filePath);
    }

    /**
     * Similar to {@link #saveArchiveJob(ReadOnlyCarvicim)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
        requireNonNull(carvicim);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createEvenIfExist(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableArchiveJob(carvicim));
    }

}
