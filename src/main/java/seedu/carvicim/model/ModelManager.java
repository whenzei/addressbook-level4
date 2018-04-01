package seedu.carvicim.model;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.carvicim.commons.core.ComponentManager;
import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.events.model.CarvicimChangedEvent;
import seedu.carvicim.logic.commands.CommandWords;
import seedu.carvicim.model.job.DateRange;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.exceptions.JobNotFoundException;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;

/**
 * Represents the in-memory model of the carvicim book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private static final String ONE_AS_STRING = "1";

    private final Carvicim carvicim;
    private final FilteredList<Employee> filteredEmployees;
    private final FilteredList<Job> filteredJobs;
    private final CommandWords commandWords;

    /**
     * Initializes a ModelManager with the given carvicim and userPrefs.
     */
    public ModelManager(ReadOnlyCarvicim carvicim, UserPrefs userPrefs) {
        super();
        requireAllNonNull(carvicim, userPrefs);

        logger.fine("Initializing with carvicim book: " + carvicim + " and user prefs " + userPrefs);

        this.carvicim = new Carvicim(carvicim);
        filteredEmployees = new FilteredList<>(this.carvicim.getEmployeeList());
        filteredJobs = new FilteredList<>(this.carvicim.getJobList());
        this.commandWords = userPrefs.getCommandWords();
    }

    public ModelManager() {
        this(new Carvicim(), new UserPrefs());
    }

    //@@author whenzei
    /**
     * Initializes the running job number based on the past job numbers.
     */
    @Override
    public void initJobNumber() {
        if (filteredJobs.isEmpty()) {
            JobNumber.initialize(ONE_AS_STRING);
            return;
        }
        int largest = filteredJobs.get(0).getJobNumber().asInteger();
        for (Job job : filteredJobs) {
            if (job.getJobNumber().asInteger() > largest) {
                largest = job.getJobNumber().asInteger();
            }
        }
        JobNumber.initialize(largest + 1);
    }

    @Override
    public void resetData(ReadOnlyCarvicim newData, CommandWords newCommandWords) {
        carvicim.resetData(newData);
        commandWords.resetData(newCommandWords);
        indicateAddressBookChanged();
    }

    @Override
    public CommandWords getCommandWords() {
        return commandWords;
    }

    @Override
    public String appendCommandKeyToMessage(String message) {
        StringBuilder builder = new StringBuilder(message);
        builder.append("\n");
        builder.append(commandWords.toString());
        return builder.toString();
    }

    @Override
    public ReadOnlyCarvicim getCarvicim() {
        return carvicim;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new CarvicimChangedEvent(carvicim));
    }

    @Override
    public synchronized void addJob(Job job) {
        carvicim.addJob(job);
        updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void closeJob(Job target) throws JobNotFoundException {
    }

    @Override
    public synchronized void archiveJob(DateRange dateRange) {
        carvicim.archiveJob(dateRange);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void deletePerson(Employee target) throws EmployeeNotFoundException {
        carvicim.removeEmployee(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Employee employee) throws DuplicateEmployeeException {
        carvicim.addEmployee(employee);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void addJobs(List<Job> jobs) {
        for (Job job : jobs) {
            addMissingEmployees(job.getAssignedEmployees());
            addJob(job);
        }
    }

    @Override
    public void addMissingEmployees(Set<Employee> employees) {
        Iterator<Employee> employeeIterator = employees.iterator();
        while (employeeIterator.hasNext()) {
            try {
                addPerson(employeeIterator.next());
            } catch (DuplicateEmployeeException e) {
                // discard the result
            }
        }
    }

    @Override
    public void updatePerson(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireAllNonNull(target, editedEmployee);

        carvicim.updateEmployee(target, editedEmployee);
        indicateAddressBookChanged();
    }

    @Override
    public void sortPersonList() {
        carvicim.sortList();
        indicateAddressBookChanged();
    }

    //=========== Filtered Employee List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Employee} backed by the internal list of
     * {@code carvicim}
     */
    @Override
    public ObservableList<Employee> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredEmployees);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Employee> predicate) {
        requireNonNull(predicate);
        filteredEmployees.setPredicate(predicate);
    }

    //=========== Filtered Job List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the internal list of
     * {@code carvicim}
     */
    @Override
    public ObservableList<Job> getFilteredJobList() {
        return FXCollections.unmodifiableObservableList(filteredJobs);
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        filteredJobs.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return carvicim.equals(other.carvicim)
                && filteredEmployees.equals(other.filteredEmployees)
                && filteredJobs.equals(other.filteredJobs)
                && commandWords.equals(other.getCommandWords());
    }

}
