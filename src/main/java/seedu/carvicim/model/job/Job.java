package seedu.carvicim.model.job;

import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.model.remark.RemarkList;

//@@author whenzei
/**
 * Represents a Job in the car servicing manager
 */
public class Job {
    protected final RemarkList remarks;

    private final Person client;
    private final VehicleNumber vehicleNumber;
    private final JobNumber jobNumber;
    private final Date date;
    private final Status status;

    private final UniqueEmployeeList assignedEmployees;

    public Job(Person client, VehicleNumber vehicleNumber, JobNumber jobNumber,
               Date date, UniqueEmployeeList assignedEmployees, Status status, RemarkList remarks) {

        requireAllNonNull(client, vehicleNumber, jobNumber, date, assignedEmployees, status);
        this.client = client;
        this.vehicleNumber = vehicleNumber;
        this.jobNumber = jobNumber;
        this.date = date;
        this.assignedEmployees = assignedEmployees;
        this.status = status;
        this.remarks = remarks;
    }

    public JobNumber getJobNumber() {
        return jobNumber;
    }

    public VehicleNumber getVehicleNumber() {
        return vehicleNumber;
    }

    public Person getClient() {
        return client;
    }

    public Date getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Employee> getAssignedEmployees() {
        return Collections.unmodifiableSet(assignedEmployees.toSet());
    }

    public ObservableList getAssignedEmployeesAsObservableList() {
        return assignedEmployees.asObservableList();
    }

    /**
     * Returns an arraylist of remarks
     */
    public ArrayList<Remark> getRemarks() {
        return remarks.getRemarks();
    }

    public void addRemark(Remark remark) {
        remarks.add(remark);
    }

    public boolean hasEmployee(Employee employee) {
        return assignedEmployees.contains(employee);
    }

    public int getEmployeeCount() {
        return assignedEmployees.size();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Job)) {
            return false;
        }

        Job otherJob = (Job) other;
        return otherJob.getClient().equals(this.getClient())
                && otherJob.getVehicleNumber().equals(this.getVehicleNumber())
                && otherJob.getJobNumber().equals(this.getJobNumber())
                && otherJob.getDate().equals(this.getDate())
                && otherJob.getAssignedEmployees().equals(this.getAssignedEmployees())
                && otherJob.getStatus().equals(this.getStatus())
                && otherJob.getRemarks().equals(this.getRemarks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, vehicleNumber, jobNumber, date,
                assignedEmployees, status, remarks);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nJob Number: ")
                .append(getJobNumber())
                .append("[" + getStatus() + "]")
                .append(" Start Date: ")
                .append(getDate())
                .append(" \nVehicle ID: ")
                .append(getVehicleNumber())
                .append(" Client: ")
                .append(getClient())
                .append(" \nRemarks: ");

        for (Remark remark : remarks) {
            builder.append("\n" + remark);
        }

        builder.append(" \nAssigned Employees:");
        for (Employee assignedEmployee : assignedEmployees) {
            builder.append("\n" + assignedEmployee);
        }

        return builder.toString();
    }
}
