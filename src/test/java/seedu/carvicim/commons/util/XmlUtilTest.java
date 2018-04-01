package seedu.carvicim.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.carvicim.model.Carvicim;
import seedu.carvicim.storage.XmlAdaptedEmployee;
import seedu.carvicim.storage.XmlAdaptedTag;
import seedu.carvicim.storage.XmlSerializableCarvicim;
import seedu.carvicim.testutil.CarvicimBuilder;
import seedu.carvicim.testutil.EmployeeBuilder;
import seedu.carvicim.testutil.TestUtil;

public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "validCarvicm.xml");
    private static final File MISSING_EMPLOYEE_FIELD_FILE =
            new File(TEST_DATA_FOLDER + "missingEmployeeField.xml");
    private static final File INVALID_EMPLOYEE_FIELD_FILE =
            new File(TEST_DATA_FOLDER + "invalidEmployeeField.xml");
    private static final File VALID_EMPLOYEE_FILE = new File(TEST_DATA_FOLDER + "validEmployee.xml");
    private static final File TEMP_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempCarvicim.xml"));

    private static final String INVALID_PHONE = "9482asf424";

    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_PHONE = "9482424";
    private static final String VALID_EMAIL = "hans@example";
    private static final List<XmlAdaptedTag> VALID_TAGS =
            Collections.singletonList(new XmlAdaptedTag("mechanic"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, Carvicim.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, Carvicim.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, Carvicim.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        Carvicim dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableCarvicim.class).toModelType();
        assertEquals(9, dataFromFile.getEmployeeList().size());
        assertEquals(0, dataFromFile.getTagList().size());
    }

    @Test
    public void xmlAdaptedEmployeeFromFile_fileWithMissingEmployeeField_validResult() throws Exception {
        XmlAdaptedEmployee actualEmployee = XmlUtil.getDataFromFile(
                MISSING_EMPLOYEE_FIELD_FILE, XmlAdaptedEmployeeWithRootElement.class);
        XmlAdaptedEmployee expectedEmployee = new XmlAdaptedEmployee(
                null, VALID_PHONE, VALID_EMAIL, VALID_TAGS);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    public void xmlAdaptedEmployeeFromFile_fileWithInvalidEmployeeField_validResult() throws Exception {
        XmlAdaptedEmployee actualEmployee = XmlUtil.getDataFromFile(
                INVALID_EMPLOYEE_FIELD_FILE, XmlAdaptedEmployeeWithRootElement.class);
        XmlAdaptedEmployee expectedEmployee = new XmlAdaptedEmployee(
                VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_TAGS);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    public void xmlAdaptedEmployeeFromFile_fileWithValidEmployee_validResult() throws Exception {
        XmlAdaptedEmployee actualEmployee = XmlUtil.getDataFromFile(
                VALID_EMPLOYEE_FILE, XmlAdaptedEmployeeWithRootElement.class);
        XmlAdaptedEmployee expectedEmployee = new XmlAdaptedEmployee(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_TAGS);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new Carvicim());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new Carvicim());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        TEMP_FILE.createNewFile();
        XmlSerializableCarvicim dataToWrite = new XmlSerializableCarvicim(new Carvicim());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableCarvicim dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableCarvicim.class);
        assertEquals(dataToWrite, dataFromFile);

        CarvicimBuilder builder = new CarvicimBuilder(new Carvicim());
        dataToWrite = new XmlSerializableCarvicim(
                builder.withEmployee(new EmployeeBuilder().build()).withTag("Friends").build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableCarvicim.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data
     * to {@code XmlAdaptedEmployee}
     * objects.
     */
    @XmlRootElement(name = "employee")
    private static class XmlAdaptedEmployeeWithRootElement extends XmlAdaptedEmployee {}
}
