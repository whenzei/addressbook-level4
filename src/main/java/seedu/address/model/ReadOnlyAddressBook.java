package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.job.Job;
import seedu.address.model.person.Employee;
import seedu.address.model.tag.Tag;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Employee> getEmployeeList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

    /**
     * Returns an unmodifiable view of the jobs list.
     */
    ObservableList<Job> getJobList();

    /**
     * Returns an unmodifiable view of the archive jobs list.
     */
    ObservableList<Job> getArchiveJobList();

}
