package seedu.carvicim.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.carvicim.commons.events.model.CarvicimChangedEvent;
import seedu.carvicim.commons.events.storage.DataSavingExceptionEvent;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlCarvicimStorage carvicimStorage = new XmlCarvicimStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        XmlArchiveJobStorage archiveJobStorage = new XmlArchiveJobStorage(getTempFilePath("cd"));
        storageManager = new StorageManager(carvicimStorage, userPrefsStorage, archiveJobStorage);
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void carvicimReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlCarvicimStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlCarvicimStorageTest} class.
         */
        Carvicim original = getTypicalCarvicim();
        storageManager.saveCarvicim(original);
        ReadOnlyCarvicim retrieved = storageManager.readCarvicim().get();
        assertEquals(original, new Carvicim(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void handleAddressBookChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlCarvicimStorageExceptionThrowingStub("dummy"),
                                             new JsonUserPrefsStorage("dummy"),
                new XmlArchiveJobStorageExceptionThrowingStub("dummy"));
        storage.handleAddressBookChangedEvent(new CarvicimChangedEvent(new Carvicim()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlCarvicimStorageExceptionThrowingStub extends XmlCarvicimStorage {

        public XmlCarvicimStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveCarvicim(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }

    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlArchiveJobStorageExceptionThrowingStub extends XmlArchiveJobStorage {

        public XmlArchiveJobStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }

}
