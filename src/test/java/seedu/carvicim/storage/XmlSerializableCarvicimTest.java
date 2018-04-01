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

public class XmlSerializableCarvicimTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlSerializableCarvicimTest/");
    private static final File TYPICAL_EMPLOYEES_FILE = new File(TEST_DATA_FOLDER + "typicalEmployeesCarvicim.xml");
    private static final File INVALID_EMPLOYEE_FILE = new File(TEST_DATA_FOLDER + "invalidEmployeeCarvicim.xml");
    private static final File INVALID_TAG_FILE = new File(TEST_DATA_FOLDER + "invalidTagCarvicim.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalEmployeesFile_success() throws Exception {
        XmlSerializableCarvicim dataFromFile = XmlUtil.getDataFromFile(TYPICAL_EMPLOYEES_FILE,
                XmlSerializableCarvicim.class);
        Carvicim carvicimFromFile = dataFromFile.toModelType();
        Carvicim typicalEmployeesCarvicim = TypicalEmployees.getTypicalCarvicim();
        assertEquals(carvicimFromFile, typicalEmployeesCarvicim);
    }

    @Test
    public void toModelType_invalidEmployeeFile_throwsIllegalValueException() throws Exception {
        XmlSerializableCarvicim dataFromFile = XmlUtil.getDataFromFile(INVALID_EMPLOYEE_FILE,
                XmlSerializableCarvicim.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_invalidTagFile_throwsIllegalValueException() throws Exception {
        XmlSerializableCarvicim dataFromFile = XmlUtil.getDataFromFile(INVALID_TAG_FILE,
                XmlSerializableCarvicim.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }
}
