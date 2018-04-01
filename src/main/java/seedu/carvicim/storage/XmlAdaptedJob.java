package seedu.carvicim.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.carvicim.commons.exceptions.IllegalValueException;
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
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.model.remark.RemarkList;

//@@author whenzei
/**
 * JAXB-friendly version of the Job
 */
public class XmlAdaptedJob {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Job's %s field is missing!";

    @XmlElement(required = true)
    private String jobNumber;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String vehicleNumber;
    @XmlElement(required = true)
    private String status;
    @XmlElement(required = true)
    private String date;

    @XmlElement(required = true)
    private List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
    @XmlElement(required = true)
    private List<XmlAdaptedRemark> remarks = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedJob.
     * This is the no-arg constructor that is required by JAXB
     */
    public XmlAdaptedJob() {}

    /**
     * Constructs an {@code XmlAdaptedJob} with the given job details.
     */
    public XmlAdaptedJob(String jobNumber, String name, String phone, String email, String vehicleNumber,
                         String status, String date, List<XmlAdaptedEmployee> assignedEmployees,
                         List<XmlAdaptedRemark> remarks) {

        this.jobNumber = jobNumber;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.vehicleNumber = vehicleNumber;
        this.status = status;
        this.date = date;
        if (assignedEmployees != null) {
            this.assignedEmployees = new ArrayList<>(assignedEmployees);
        }
        if (remarks != null) {
            this.remarks = new ArrayList<>(remarks);
        }
    }

    /**
     * Converts a given Job into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEmployee
     */
    public XmlAdaptedJob(Job source) {
        jobNumber = source.getJobNumber().value;
        name = source.getClient().getName().fullName;
        phone = source.getClient().getPhone().value;
        email = source.getClient().getEmail().value;
        status = source.getStatus().value;
        vehicleNumber = source.getVehicleNumber().value;
        date = source.getDate().value;
        for (Employee employee : source.getAssignedEmployees()) {
            assignedEmployees.add(new XmlAdaptedEmployee(employee));
        }
        for (Remark remark : source.getRemarks()) {
            remarks.add(new XmlAdaptedRemark(remark));
        }
    }

    /**
     * Converts this jaxb-friendly adapted job object into the model's Job object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted job
     */
    public Job toModelType() throws IllegalValueException {
        final List<Remark> jobRemarks = new ArrayList<>();
        final List<Employee> jobAssignedEmployees = new ArrayList<>();
        for (XmlAdaptedRemark remark : remarks) {
            jobRemarks.add(remark.toModelType());
        }
        for (XmlAdaptedEmployee assignedEmployee : assignedEmployees) {
            jobAssignedEmployees.add(assignedEmployee.toModelType());
        }

        if (this.jobNumber == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JobNumber.class.getSimpleName()));
        }
        final JobNumber jobNumber = new JobNumber(this.jobNumber);

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);
        final Person client = new Person(name, phone, email);

        if (this.vehicleNumber == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    VehicleNumber.class.getSimpleName()));
        }
        if (!VehicleNumber.isValidVehicleNumber(this.vehicleNumber)) {
            throw new IllegalValueException(VehicleNumber.MESSAGE_VEHICLE_ID_CONSTRAINTS);
        }
        final VehicleNumber vehicleNumber = new VehicleNumber(this.vehicleNumber);

        if (this.status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName()));
        }
        final Status status = new Status(this.status);

        if (this.date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }
        final Date date = new Date(this.date);


        final RemarkList remarks = new RemarkList(new HashSet<>(jobRemarks));
        final UniqueEmployeeList assignedEmployees = new UniqueEmployeeList();
        assignedEmployees.setEmployees(jobAssignedEmployees);

        return new Job(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedJob)) {
            return false;
        }

        XmlAdaptedJob otherJob = (XmlAdaptedJob) other;
        return Objects.equals(jobNumber, otherJob.jobNumber)
                && Objects.equals(name, otherJob.name)
                && Objects.equals(phone, otherJob.phone)
                && Objects.equals(email, otherJob.email)
                && Objects.equals(date, otherJob.date)
                && Objects.equals(vehicleNumber, otherJob.vehicleNumber)
                && Objects.equals(status, otherJob.status)
                && assignedEmployees.equals(otherJob.assignedEmployees)
                && remarks.equals(otherJob.remarks);
    }
}
