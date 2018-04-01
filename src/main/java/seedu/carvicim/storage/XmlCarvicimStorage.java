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

/**
 * A class to access Carvicim data stored as an xml file on the hard disk.
 */
public class XmlCarvicimStorage implements CarvicimStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlCarvicimStorage.class);

    private String filePath;

    public XmlCarvicimStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCarvicim> readCarvicim() throws DataConversionException, IOException {
        return readCarvicim(filePath);
    }

    /**
     * Similar to {@link #readCarvicim()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyCarvicim> readCarvicim(String filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        File carvicimFile = new File(filePath);

        if (!carvicimFile.exists()) {
            logger.info("Carvicim file "  + carvicimFile + " not found");
            return Optional.empty();
        }

        XmlSerializableCarvicim xmlAddressBook = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlAddressBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + carvicimFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveCarvicim(ReadOnlyCarvicim carvicm) throws IOException {
        saveCarvicim(carvicm, filePath);
    }

    /**
     * Similar to {@link #saveCarvicim(ReadOnlyCarvicim)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveCarvicim(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
        requireNonNull(carvicim);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableCarvicim(carvicim));
    }

}
