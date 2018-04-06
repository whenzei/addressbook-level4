package seedu.carvicim.storage;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.commons.util.FileUtil;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;

//@@author richardson0694
public class XmlArchiveJobStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlArchiveJobStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readArchiveJob_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readArchiveJob(null);
    }

    private java.util.Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws Exception {
        return new XmlArchiveJobStorage(filePath).readArchiveJob(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readArchiveJob("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readArchiveJob("NotXmlFormatCarvicim.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readArchiveJob_invalidJobCarvicim_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readArchiveJob("invalidJobCarvicim.xml");
    }

    @Test
    public void readArchiveJob_invalidAndValidJobCarvicim_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readArchiveJob("invalidAndValidJobCarvicim.xml");
    }

    @Test
    public void saveArchiveJob_nullArchiveJob_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveArchiveJob(null, "SomeFile.xml");
    }

    /**
     * Saves {@code carvicim} at the specified {@code filePath}.
     */
    private void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) {
        try {
            new XmlArchiveJobStorage(filePath).saveArchiveJob(carvicim, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveArchiveJob_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveArchiveJob(new Carvicim(), null);
    }

}
