package seedu.carvicim.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.commons.util.XmlUtil;

/**
 * Stores addressbook and archivejob data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given addressbook data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableCarvicim carvicim)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, carvicim);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    //@@author richardson0694
    /**
     * Saves the given archivejob data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableArchiveJob archiveJob)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, archiveJob);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    //@@author
    /**
     * Returns carvicim book in the file or an empty carvicim book
     */
    public static XmlSerializableCarvicim loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableCarvicim.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

    //@@author richardson0694
    /**
     * Returns archive job in the file or an empty carvicim book
     */
    public static XmlSerializableArchiveJob loadDataFromArchiveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableArchiveJob.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
