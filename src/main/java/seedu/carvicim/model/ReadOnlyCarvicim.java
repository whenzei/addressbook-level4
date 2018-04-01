package seedu.carvicim.model;

import javafx.collections.ObservableList;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.tag.Tag;

/**
 * Unmodifiable view of an carvicim book
 */
public interface ReadOnlyCarvicim {

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
