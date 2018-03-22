package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobNumber;
import seedu.address.model.job.VehicleNumber;
import seedu.address.model.job.exceptions.JobNotFoundException;
import seedu.address.model.person.Employee;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicateEmployeeException;
import seedu.address.model.person.exceptions.EmployeeNotFoundException;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobBuilder;

//@@author whenzei
public class AddJobCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

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

        CommandResult commandResult = addJobCommand.execute();

        Job validJob = new JobBuilder(model.getFilteredPersonList()).build();

        assertEquals(String.format(AddJobCommand.MESSAGE_SUCCESS, validJob), commandResult.feedbackToUser);
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
        public void resetData(ReadOnlyAddressBook newData, CommandWords newCommandWords) {
            fail("This method should not be called.");
        }

        @Override
        public CommandWords getCommandWords() {
            fail("This method should never be called");
            return null;
        }

        @Override public String appendCommandKeyToMessage(String message) {
            fail("This method should never be called");
            return null;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
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
     * A Model stub that always throw a DuplicateEmployeeException when trying to add a employee.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {
        @Override
        public void addPerson(Employee employee) throws DuplicateEmployeeException {
            throw new DuplicateEmployeeException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }


    /**
     * A Model stub that always accept the employee being added.
     */
    private class ModelStubAcceptingJobAdded extends ModelStub {
        final ArrayList<Job> personsAdded = new ArrayList<>();

        @Override
        public void addJob(Job job) {
            requireNonNull(job);
            personsAdded.add(job);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
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
        JobNumber.initialize("0");
        AddJobCommand addJobCommand = new AddJobCommand(client, vehicleNumber, indices);
        addJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addJobCommand;
    }
}
