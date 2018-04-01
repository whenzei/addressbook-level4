package seedu.carvicim.model;

import static org.junit.Assert.assertEquals;
import static seedu.carvicim.testutil.TypicalEmployees.ALICE;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.tag.Tag;

public class CarvicimTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Carvicim carvicim = new Carvicim();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), carvicim.getEmployeeList());
        assertEquals(Collections.emptyList(), carvicim.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        carvicim.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        Carvicim newData = getTypicalCarvicim();
        carvicim.resetData(newData);
        assertEquals(newData, carvicim);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Repeat ALICE twice
        List<Employee> newEmployees = Arrays.asList(ALICE, ALICE);
        List<Job> newJobs = new ArrayList<>();
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        List<Job> newArchiveJobs = new ArrayList<>();
        CarvicimStub newData = new CarvicimStub(newEmployees, newJobs, newTags, newArchiveJobs);

        thrown.expect(AssertionError.class);
        carvicim.resetData(newData);
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        carvicim.getEmployeeList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        carvicim.getTagList().remove(0);
    }

    /**
     * A stub ReadOnlyCarvicim whose employees and tags lists can violate interface constraints.
     */
    private static class CarvicimStub implements ReadOnlyCarvicim {
        private final ObservableList<Employee> employees = FXCollections.observableArrayList();
        private final ObservableList<Job> jobs = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();
        private final ObservableList<Job> archiveJobs = FXCollections.observableArrayList();

        CarvicimStub(Collection<Employee> employees, Collection<Job> jobs, Collection<? extends Tag> tags,
                     Collection<Job> archiveJobs) {
            this.employees.setAll(employees);
            this.jobs.setAll(jobs);
            this.tags.setAll(tags);
            this.archiveJobs.setAll(archiveJobs);
        }

        @Override
        public ObservableList<Employee> getEmployeeList() {
            return employees;
        }

        @Override
        public ObservableList<Job> getJobList() {
            return jobs;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }

        @Override
        public ObservableList<Job> getArchiveJobList() {
            return archiveJobs;
        }
    }

}
