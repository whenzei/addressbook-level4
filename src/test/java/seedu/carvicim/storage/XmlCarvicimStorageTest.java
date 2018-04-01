package seedu.carvicim.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.carvicim.testutil.TypicalEmployees.ALICE;
import static seedu.carvicim.testutil.TypicalEmployees.HOON;
import static seedu.carvicim.testutil.TypicalEmployees.IDA;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.commons.util.FileUtil;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;

public class XmlCarvicimStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlCarvicimStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readCarvicim_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readCarvicim(null);
    }

    private java.util.Optional<ReadOnlyCarvicim> readCarvicim(String filePath) throws Exception {
        return new XmlCarvicimStorage(filePath).readCarvicim(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readCarvicim("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readCarvicim("NotXmlFormatCarvicim.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readCarvicim_invalidPersonCarvicim_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readCarvicim("invalidEmployeeCarvicim.xml");
    }

    @Test
    public void readCarvicim_invalidAndValidEmployeeCarvicim_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readCarvicim("invalidAndValidEmployeeCarvicim.xml");
    }

    @Test
    public void readAndSaveCarvicim_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempCarvicim.xml";
        Carvicim original = getTypicalCarvicim();
        XmlCarvicimStorage xmlCarvicimStorage = new XmlCarvicimStorage(filePath);

        //Save in new file and read back
        xmlCarvicimStorage.saveCarvicim(original, filePath);
        ReadOnlyCarvicim readBack = xmlCarvicimStorage.readCarvicim(filePath).get();
        assertEquals(original, new Carvicim(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addEmployee(HOON);
        original.removeEmployee(ALICE);
        xmlCarvicimStorage.saveCarvicim(original, filePath);
        readBack = xmlCarvicimStorage.readCarvicim(filePath).get();
        assertEquals(original, new Carvicim(readBack));

        //Save and read without specifying file path
        original.addEmployee(IDA);
        xmlCarvicimStorage.saveCarvicim(original); //file path not specified
        readBack = xmlCarvicimStorage.readCarvicim().get(); //file path not specified
        assertEquals(original, new Carvicim(readBack));

    }

    @Test
    public void saveCarvicim_nullCarvicim_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveCarvicim(null, "SomeFile.xml");
    }

    /**
     * Saves {@code carvicim} at the specified {@code filePath}.
     */
    private void saveCarvicim(ReadOnlyCarvicim carvicim, String filePath) {
        try {
            new XmlCarvicimStorage(filePath).saveCarvicim(carvicim, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveCarvicim_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveCarvicim(new Carvicim(), null);
    }


}
