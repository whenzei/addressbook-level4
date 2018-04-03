package seedu.carvicim.storage;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static seedu.carvicim.storage.XmlAdaptedJob.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.carvicim.testutil.TypicalEmployees.BENSON;
import static seedu.carvicim.testutil.TypicalEmployees.CARL;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.Status;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.remark.RemarkList;
import seedu.carvicim.testutil.Assert;
import seedu.carvicim.testutil.ClientBuilder;

//@@author whenzei
public class XmlAdaptedJobTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_VEHICLE_NUMBER = "";
    private static final String INVALID_DATE = "F3b A 2018";
    private static final String INVALID_JOB_NUMBER = "-1";

    private static final String VALID_NAME = new ClientBuilder().build().getName().fullName;
    private static final String VALID_PHONE = new ClientBuilder().build().getPhone().value;
    private static final String VALID_EMAIL = new ClientBuilder().build().getEmail().value;
    private static final String VALID_VEHICLE_NUMBER = "SHG123";
    private static final String VALID_DATE = "Feb 02 2018";
    private static final String VALID_JOB_NUMBER = "2";
    private static final String VALID_STATUS = "ongoing";

    @Test
    public void toModelType_validJobDetails_returnsJob() throws Exception {
        Person client = new Person(new Name(VALID_NAME), new Phone(VALID_PHONE), new Email(VALID_EMAIL));
        UniqueEmployeeList assignedEmployees = new UniqueEmployeeList();
        assignedEmployees.add(BENSON);
        assignedEmployees.add(CARL);

        Job testJob = new Job(client, new VehicleNumber(VALID_VEHICLE_NUMBER), new JobNumber(VALID_JOB_NUMBER),
                new Date(VALID_DATE), assignedEmployees, new Status(VALID_STATUS), new RemarkList());

        XmlAdaptedJob job = new XmlAdaptedJob(testJob);
        assertEquals(testJob, job.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_jobNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(INVALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = JobNumber.MESSAGE_JOB_NUMBER_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, INVALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Date.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidVehicleNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                INVALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = VehicleNumber.MESSAGE_VEHICLE_ID_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullVehicleNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, VehicleNumber.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullJobNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(null, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, JobNumber.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, null, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, null, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, null,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, null, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullStatus_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, null, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void equals() {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job1 = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        XmlAdaptedJob job2 = new XmlAdaptedJob(VALID_JOB_NUMBER + 1, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);

        // same object -> returns true
        assertTrue(job1.equals(job1));

        // same values -> returns true
        XmlAdaptedJob jobCopy = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        assertTrue(job1.equals(jobCopy));

        // different types -> returns false
        assertFalse(job1.equals(1));

        // null -> returns false
        assertFalse(job1.equals(null));

        // different jobs -> return false
        assertFalse(job1.equals(job2));

    }

    /**Generates a sample employe in the Xml form*/
    private XmlAdaptedEmployee generateSampleEmployee() {
        List<XmlAdaptedTag> bensonTags = new ArrayList<>();
        bensonTags.add(new XmlAdaptedTag("mechanic"));
        bensonTags.add(new XmlAdaptedTag("technician"));

        XmlAdaptedEmployee benson = new XmlAdaptedEmployee(BENSON.getName().fullName, BENSON.getPhone().value,
                BENSON.getEmail().value, bensonTags);

        return benson;
    }
}

