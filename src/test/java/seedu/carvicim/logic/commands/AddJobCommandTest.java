package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.DateRange;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobList;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.job.exceptions.JobNotFoundException;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.testutil.ClientBuilder;
import seedu.carvicim.testutil.JobBuilder;

//@@author whenzei
public class AddJobCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalCarvicim(), new UserPrefs());

    @Test
    public void constructor_nullAddJobFields_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddJobCommand(null, null, null);
    }

    @Test
    public void execute_jobAcceptedByModel_addSuccessful() throws Exception {
        Person client = new ClientBuilder().build();
        ArrayList<Index> indices = generateValidEmployeeIndices();
        AddJobCommand addJobCommand = prepareCommand(client,
                new VehicleNumber(VehicleNumber.DEFAULT_VEHICLE_NUMBER), indices);
        JobNumber.initialize(0);

        CommandResult commandResult = addJobCommand.execute();

        Job validJob = new JobBuilder(model.getFilteredPersonList()).build();

        assertEquals(String.format(AddJobCommand.MESSAGE_SUCCESS, validJob), commandResult.feedbackToUser);
    }

    @Test
    public void equals() throws Exception {
        Person aliceClient = new ClientBuilder().withName("Alice").build();
        Person bobClient = new ClientBuilder().withName("Bob").build();

        AddJobCommand addJobWithClientAliceCommand = prepareCommand(aliceClient,
                new VehicleNumber(VehicleNumber.DEFAULT_VEHICLE_NUMBER), generateValidEmployeeIndices());
        AddJobCommand addJobWithClientBobCommand = prepareCommand(bobClient,
                new VehicleNumber(VehicleNumber.DEFAULT_VEHICLE_NUMBER), generateValidEmployeeIndices());

        // same object -> returns true
        assertTrue(addJobWithClientAliceCommand.equals(addJobWithClientAliceCommand));

        // same values -> returns true
        JobNumber.initialize(0);
        AddJobCommand addJobWithClientAliceCommandCopy = new AddJobCommand(aliceClient,
                new VehicleNumber(VehicleNumber.DEFAULT_VEHICLE_NUMBER), generateValidEmployeeIndices());
        assertTrue(addJobWithClientAliceCommand.equals(addJobWithClientAliceCommandCopy));

        // different job -> returns false
        assertFalse(addJobWithClientAliceCommand.equals(addJobWithClientBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Employee employee) throws DuplicateEmployeeException {
            fail("This method should not be called.");
        }

        @Override
        public void addJobs(List<Job> job) {
            fail("This method should not be called.");
        }

        @Override
        public void addMissingEmployees(Set<Employee> employees) {
            fail("This method should not be called.");
        }

        @Override public boolean isViewingImportedJobs() {
            fail("This method should not be called.");
            return false;
        }

        @Override public void switchJobView() {
            fail("This method should not be called.");
        }

        @Override public void resetJobView() {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyCarvicim newData, CommandWords newCommandWords) {
            fail("This method should not be called.");
        }

        @Override
        public CommandWords getCommandWords() {
            fail("This method should never be called");
            return null;
        }

        @Override
        public void initJobNumber() {
            fail("This method should never be called");
        }

        @Override public String appendCommandKeyToMessage(String message) {
            fail("This method should never be called");
            return null;
        }

        @Override
        public ReadOnlyCarvicim getCarvicim() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addJob(Job job) {
            fail("This method should not be called.");
        }

        @Override
        public void closeJob(Job target) throws JobNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void addRemark(Job job, Remark remark) {
            fail("This method should not be called");
        }

        @Override
        public void archiveJob(DateRange dateRange) {
            fail("This method should not be called");
        }

        @Override
        public JobList analyseJob(JobList jobList) {
            fail("This method should not be called");
            return null;
        }

        @Override
        public void deletePerson(Employee target) throws EmployeeNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortPersonList() {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Employee target, Employee editedEmployee)
                throws DuplicateEmployeeException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Employee> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Employee> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Job> getFilteredJobList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredJobList(Predicate<Job> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * Generates an Arraylist of valid assigned employee index
     */
    private ArrayList<Index> generateValidEmployeeIndices() {
        ArrayList<Index> indices = new ArrayList<Index>();
        indices.add(INDEX_FIRST_PERSON);
        indices.add(INDEX_SECOND_PERSON);
        return indices;
    }

    /**
     * Returns a {@code AddJobCommand} with the client, vehicleNumber and indices.
     * @param client
     * @param vehicleNumber
     * @param indices
     */
    private AddJobCommand prepareCommand(Person client, VehicleNumber vehicleNumber, ArrayList<Index> indices) {
        AddJobCommand addJobCommand = new AddJobCommand(client, vehicleNumber, indices);
        addJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addJobCommand;
    }
}
