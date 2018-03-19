package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.job.Job;
import seedu.address.model.job.exceptions.JobNotFoundException;
import seedu.address.model.person.Employee;
import seedu.address.model.person.exceptions.DuplicateEmployeeException;
import seedu.address.model.person.exceptions.EmployeeNotFoundException;
import seedu.address.testutil.EmployeeBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded() {
            @Override
            public CommandWords getCommandWords() {
                return new CommandWords();
            }
        };
        Employee validEmployee = new EmployeeBuilder().build();

        CommandResult commandResult = getAddCommandForPerson(validEmployee, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEmployee), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validEmployee), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException() {
            @Override
            public CommandWords getCommandWords() {
                return new CommandWords();
            }
        };
        Employee validEmployee = new EmployeeBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validEmployee, modelStub).execute();
    }

    @Test
    public void equals() {
        Employee alice = new EmployeeBuilder().withName("Alice").build();
        Employee bob = new EmployeeBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different employee -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given employee.
     */
    private AddCommand getAddCommandForPerson(Employee employee, Model model) {
        AddCommand command = new AddCommand(employee);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
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
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Employee> personsAdded = new ArrayList<>();

        @Override
        public void addPerson(Employee employee) throws DuplicateEmployeeException {
            requireNonNull(employee);
            personsAdded.add(employee);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
