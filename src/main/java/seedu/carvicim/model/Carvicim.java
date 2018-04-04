package seedu.carvicim.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.DateRange;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobList;
import seedu.carvicim.model.job.exceptions.JobNotFoundException;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.model.tag.Tag;
import seedu.carvicim.model.tag.UniqueTagList;

/**
 * Wraps all data at the carvicim-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class Carvicim implements ReadOnlyCarvicim {

    private final UniqueEmployeeList employees;
    private final UniqueTagList tags;
    private final JobList jobs;
    private JobList archiveJobs;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        employees = new UniqueEmployeeList();
        tags = new UniqueTagList();
        jobs = new JobList();
        archiveJobs = new JobList();
    }

    public Carvicim() {}

    /**
     * Creates an Carvicim using the Persons, Jobs and Tags in the {@code toBeCopied}
     */
    public Carvicim(ReadOnlyCarvicim toBeCopied) {
        this();
        // For initial testing, a random job will be created for each employee
        resetData(toBeCopied);
        //createRandomJobForEachEmployee();
    }

    //// list overwrite operations

    public void setEmployees(List<Employee> employees) throws DuplicateEmployeeException {
        this.employees.setEmployees(employees);
    }

    public void setJobs(List<Job> jobs) {
        this.jobs.setJobs(jobs);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code Carvicim} with {@code newData}.
     */
    public void resetData(ReadOnlyCarvicim newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Employee> syncedEmployeeList = newData.getEmployeeList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());
        List<Job> syncedJobList = newData.getJobList();
        setJobs(syncedJobList);

        try {
            setEmployees(syncedEmployeeList);
            setJobs(syncedJobList);
        } catch (DuplicateEmployeeException e) {
            throw new AssertionError("AddressBooks should not have duplicate employees");
        }
    }

    //// job-level operations

    //@@author whenzei
    /**
     * Adds a job to Carvicim.
     */
    public void addJob(Job job) {
        jobs.add(job);
    }

    /**
     * Removes {@code job} from this {@code Carvicim}.
     * @throws JobNotFoundException if the {@code job} is not in this {@code Carvicim}.
     */
    public boolean closeJob(Job job) throws JobNotFoundException {
        if (jobs.remove(job)) {
            return true;
        } else {
            throw new JobNotFoundException();
        }
    }


    /**
     * Adds a remark to a specified job in Carvicim
     */
    public void addRemark(Job job, Remark remark) {
        Iterator<Job> iterator = jobs.iterator();
        while (iterator.hasNext()) {
            Job currJob = iterator.next();
            if (currJob.equals(job)) {
                job.addRemark(remark);
                break;
            }
        }
    }

    //@@author richardson0694
    /**
     * Archives job entries in Carvicim.
     */
    public void archiveJob(DateRange dateRange) {
        archiveJobs = new JobList();
        Iterator<Job> iterator = jobs.iterator();
        while (iterator.hasNext()) {
            Job job = iterator.next();
            Date date = job.getDate();
            Date startDate = dateRange.getStartDate();
            Date endDate = dateRange.getEndDate();
            if (dateRange.compareTo(date, startDate) >= 0 && dateRange.compareTo(date, endDate) <= 0) {
                archiveJobs.add(job);
            }
        }
    }

    /**
     * Analyses job entries in Carvicim for this month.
     */
    public JobList analyseJob(JobList jobList) {
        return jobList.analyseList(jobs);
    }

    //// employee-level operations

    /**
     * Adds a employee to the carvicim book.
     * Also checks the new employee's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the employee to point to those in {@link #tags}.
     *
     * @throws DuplicateEmployeeException if an equivalent employee already exists.
     */
    public void addEmployee(Employee p) throws DuplicateEmployeeException {
        Employee employee = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any employee
        // in the employee list.
        employees.add(employee);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code Carvicim}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicateEmployeeException if updating the employee's details causes the employee to be equivalent to
     *      another existing person in the list.
     * @throws EmployeeNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Employee)
     */
    public void updateEmployee(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireNonNull(editedEmployee);

        Employee syncedEditedEmployee = syncWithMasterTagList(editedEmployee);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any employee
        // in the employee list.
        employees.setEmployee(target, syncedEditedEmployee);
    }

    /**
     *  Updates the master tag list to include tags in {@code employee} that are not in the list.
     *  @return a copy of this {@code employee} such that every tag in this employee points
     *  to a Tag object in the master list.
     */
    private Employee syncWithMasterTagList(Employee employee) {
        final UniqueTagList employeeTags = new UniqueTagList(employee.getTags());
        tags.mergeFrom(employeeTags);

        // Create map with values = tag object references in the master list
        // used for checking employee tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of employee tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        employeeTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Employee(employee.getName(), employee.getPhone(), employee.getEmail(),
                correctTagReferences);
    }

    /**
     * Removes {@code key} from this {@code Carvicim}.
     * @throws EmployeeNotFoundException if the {@code key} is not in this {@code Carvicim}.
     */
    public boolean removeEmployee(Employee key) throws EmployeeNotFoundException {
        if (employees.remove(key)) {
            return true;
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //@@author richardson0694
    /**
     * Sort all employees' name in list alphabetically.
     */
    public UniqueEmployeeList sortList() {
        employees.sortName(new Comparator<Employee>() {
            @Override
            public int compare(Employee employee1, Employee employee2) {
                return employee1.getName().toString().compareToIgnoreCase(employee2.getName().toString());
            }
        });
        return employees;
    }

    //// util methods
    //@@author
    @Override
    public String toString() {
        return employees.asObservableList().size() + " employees, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Employee> getEmployeeList() {
        return employees.asObservableList();
    }

    @Override
    public ObservableList<Job> getJobList() {
        return jobs.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public ObservableList<Job> getArchiveJobList() {
        return archiveJobs.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Carvicim // instanceof handles nulls
                && this.employees.equals(((Carvicim) other).employees)
                && this.jobs.equals(((Carvicim) other).jobs)
                && this.archiveJobs.equals(((Carvicim) other).archiveJobs)
                && this.tags.equalsOrderInsensitive(((Carvicim) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(employees, tags);
    }
}
