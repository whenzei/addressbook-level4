package seedu.carvicim.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.carvicim.model.job.exceptions.JobNotFoundException;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.UniqueEmployeeList;

//@@author whenzei
/**
 * A list of jobs that does not allow nulls
 */
public class JobList implements Iterable<Job> {

    private final ObservableList<Job> internalList = FXCollections.observableArrayList();
    private int jobCount = 0;
    private int ongoingCount = 0;
    private int closedCount = 0;
    private HashMap<Name, Integer> analyse = new HashMap<Name, Integer>();

    /**
     * Returns true if the list contains an equivalent employee as the given argument
     */
    public boolean contains(Job toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    public void setJobs(JobList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setJobs(List<Job> jobs) {
        requireAllNonNull(jobs);
        final JobList replacement = new JobList();
        for (final Job job : jobs) {
            replacement.add(job);
        }
        setJobs(replacement);
    }

    /**
     * Adds a job to the list
     *
     */
    public void add(Job toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent job from the list
     *
     * @throws JobNotFoundException if no such job could be found in the list
     */
    public boolean remove(Job toRemove) {
        requireNonNull(toRemove);
        final boolean jobFoundAndDeleted = internalList.remove(toRemove);
        return jobFoundAndDeleted;
    }

    /**
     * Replaces the {@targetJob} with the {@updateJob}
     *
     * @throws JobNotFoundException if no {@targetJob} exists
     */
    public void replace(Job targetJob, Job updatedJob) {
        requireAllNonNull(targetJob, updatedJob);
        int targetIndex = internalList.indexOf(targetJob);
        internalList.set(targetIndex, updatedJob);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Job> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    //@@author yuhongherald
    /**
     * Filters {@code jobList} for jobs assigned to {@code employee}.
     */
    public static Predicate<Job> filterByEmployee(ObservableList<Job> jobList, Employee employee) {
        Predicate<Job> predicate = new Predicate<Job>() {
            @Override
            public boolean test(Job job) {
                return job.getAssignedEmployees().contains(employee);
            }
        };
        return predicate;
    }

    //@@author richardson0694
    /**
     * Get the job list for the current month.
     */
    public JobList getCurrentMonthJobList() {
        JobList currentMonthList = new JobList();
        LocalDate localDate = LocalDate.now();
        int month = localDate.getMonth().getValue();
        Iterator<Job> iterator = internalList.iterator();
        while (iterator.hasNext()) {
            Job job = iterator.next();
            Date date = job.getDate();
            date = new Date(date.toString());
            if (date.getMonth() == month) {
                currentMonthList.add(job);
            }
        }
        return currentMonthList;
    }

    /**
     * Get the respective job counts for both ongoing and closed.
     */
    public JobList analyseJobStatusCount(JobList analyseList) {
        analyseList = analyseList.getCurrentMonthJobList();
        Status ongoing = new Status("ongoing");
        Iterator<Job> iterator = analyseList.iterator();
        while (iterator.hasNext()) {
            Job job = iterator.next();
            if (job.getStatus().equals(ongoing)) {
                analyseList.ongoingCount++;
            } else {
                analyseList.closedCount++;
            }
            analyseList.jobCount++;
        }
        return analyseList;
    }

    /**
     * Get the respective job counts for each employee.
     */
    public JobList analyseList(JobList analyseList, UniqueEmployeeList employeeList) {
        analyseList = analyseList.analyseJobStatusCount(analyseList);
        initEmployeeJobCount(employeeList);
        Iterator<Job> iteratorJob = analyseList.iterator();
        while (iteratorJob.hasNext()) {
            Job job = iteratorJob.next();
            updateEmployeeJobCount(job);
        }
        analyseList.analyse = analyse;
        return analyseList;
    }

    /**
     * Initialise the employee job count.
     */
    public void initEmployeeJobCount(UniqueEmployeeList employeeList) {
        Iterator<Employee> iteratorEmployee = employeeList.iterator();
        while (iteratorEmployee.hasNext()) {
            Employee employee = iteratorEmployee.next();
            analyse.put(employee.getName(), 0);
        }
    }

    /**
     * Update the employee job count.
     */
    public void updateEmployeeJobCount(Job job) {
        Iterator<Employee> iteratorEmployee = job.getAssignedEmployees().iterator();
        while (iteratorEmployee.hasNext()) {
            Employee employee = iteratorEmployee.next();
            int jobCount = analyse.get(employee.getName());
            analyse.put(employee.getName(), jobCount + 1);
        }
    }
    /**
     * Get the analyse result.
     */
    public String getAnalyseResult() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Number of Jobs: ")
                .append(jobCount)
                .append(" Number of Ongoing: ")
                .append(ongoingCount)
                .append(" Number of Closed: ")
                .append(closedCount)
                .append("\n");
        Set set = analyse.entrySet();
        builder.append(set);
        return builder.toString();
    }

    @Override
    public Iterator<Job> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof JobList // instanceof handles nulls
                && this.internalList.equals(((JobList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
