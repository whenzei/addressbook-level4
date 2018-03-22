package seedu.address.testutil;

import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import javafx.collections.ObservableList;
import seedu.address.model.job.Date;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobNumber;
import seedu.address.model.job.Status;
import seedu.address.model.job.VehicleNumber;
import seedu.address.model.person.Email;
import seedu.address.model.person.Employee;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UniqueEmployeeList;
import seedu.address.model.remark.RemarkList;

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

    public JobBuilder(ObservableList<Employee> employees) throws Exception{
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
