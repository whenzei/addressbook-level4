package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyAddressBook;

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
    public Optional<ReadOnlyAddressBook> readArchiveJob() throws DataConversionException, IOException {
        return readArchiveJob(filePath);
    }

    /**
     * Similar to {@link #readArchiveJob()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyAddressBook> readArchiveJob(String filePath) throws DataConversionException,
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
    public void saveArchiveJob(ReadOnlyAddressBook addressBook) throws IOException {
        saveArchiveJob(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveArchiveJob(ReadOnlyAddressBook)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveArchiveJob(ReadOnlyAddressBook addressBook, String filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createEvenIfExist(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableArchiveJob(addressBook));
    }

}
