package seedu.carvicim.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.commons.util.FileUtil;
import seedu.carvicim.commons.util.XmlUtil;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.testutil.TypicalEmployees;

//@@author richardson0694
public class XmlSerializableArchiveJobTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlSerializableArchiveJobTest/");
    private static final File TYPICAL_JOBS_FILE = new File(TEST_DATA_FOLDER + "typicalJobsCarvicim.xml");
    private static final File INVALID_JOB_FILE = new File(TEST_DATA_FOLDER + "invalidJobCarvicim.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalJobsFile_success() throws Exception {
        XmlSerializableArchiveJob dataFromFile = XmlUtil.getDataFromFile(TYPICAL_JOBS_FILE,
               XmlSerializableArchiveJob.class);
        Carvicim carvicimFromFile = dataFromFile.toModelType();
        Carvicim typicalJobsCarvicim = TypicalEmployees.getTypicalCarvicimWithArchivedJob();
        assertEquals(carvicimFromFile, typicalJobsCarvicim);
    }

    @Test
    public void toModelType_invalidJobFile_throwsIllegalValueException() throws Exception {
        XmlSerializableArchiveJob dataFromFile = XmlUtil.getDataFromFile(INVALID_JOB_FILE,
                XmlSerializableArchiveJob.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

}
