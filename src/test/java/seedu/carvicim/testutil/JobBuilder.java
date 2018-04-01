package seedu.carvicim.testutil;

import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import javafx.collections.ObservableList;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.Status;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.remark.RemarkList;

//@@author whenzei
/**
 * A utility class to help with building Job objects.
 */
public class JobBuilder {
    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";

    private Person client;
    private VehicleNumber vehicleNumber;
    private UniqueEmployeeList assignedEmployees;
    private Status status;
    private Date date;
    private RemarkList remarks;
    private JobNumber jobNumber;

    public JobBuilder(ObservableList<Employee> employees) throws Exception {
        Name name = new Name(DEFAULT_NAME);
        Phone phone = new Phone(DEFAULT_PHONE);
        Email email = new Email(DEFAULT_EMAIL);
        JobNumber.initialize("0");

        client = new Person(name, phone, email);
        vehicleNumber = new VehicleNumber(VehicleNumber.DEFAULT_VEHICLE_NUMBER);
        status = new Status(Status.STATUS_ONGOING);
        date = new Date();
        jobNumber = new JobNumber();
        remarks = new RemarkList();
        assignedEmployees = new UniqueEmployeeList();
        assignedEmployees.add(employees.get(INDEX_FIRST_PERSON.getZeroBased()));
        assignedEmployees.add(employees.get(INDEX_SECOND_PERSON.getZeroBased()));
    }

    public Job build() {
        return new Job(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
    }
}
