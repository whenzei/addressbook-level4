package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.logic.commands.CommandWords;
import seedu.address.model.job.Job;
import seedu.address.model.job.exceptions.JobNotFoundException;
import seedu.address.model.person.Employee;
import seedu.address.model.person.exceptions.DuplicateEmployeeException;
import seedu.address.model.person.exceptions.EmployeeNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Employee> filteredEmployees;
    private final FilteredList<Job> filteredJobs;
    private final CommandWords commandWords;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredEmployees = new FilteredList<>(this.addressBook.getEmployeeList());
        filteredJobs = new FilteredList<>(this.addressBook.getJobList());
        this.commandWords = userPrefs.getCommandWords();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData, CommandWords newCommandWords) {
        addressBook.resetData(newData);
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
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void addJob(Job job) {
        addressBook.addJob(job);
    }

    @Override
    public synchronized void closeJob(Job target) throws JobNotFoundException {
    }

    @Override
    public synchronized void deletePerson(Employee target) throws EmployeeNotFoundException {
        addressBook.removeEmployee(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Employee employee) throws DuplicateEmployeeException {
        addressBook.addEmployee(employee);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireAllNonNull(target, editedEmployee);

        addressBook.updateEmployee(target, editedEmployee);
        indicateAddressBookChanged();
    }

    @Override
    public void sortPersonList() {
        addressBook.sortList();
        indicateAddressBookChanged();
    }

    //=========== Filtered Employee List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Employee} backed by the internal list of
     * {@code addressBook}
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
     * {@code addressBook}
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
        return addressBook.equals(other.addressBook)
                && filteredEmployees.equals(other.filteredEmployees)
                && filteredJobs.equals(other.filteredJobs)
                && commandWords.equals(other.getCommandWords());
    }

}
