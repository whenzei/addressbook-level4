package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Employee;
import seedu.address.testutil.EmployeeBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddEmployeeCommand}.
 */
public class AddEmployeeCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() throws Exception {
        Employee validEmployee = new EmployeeBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validEmployee);

        assertCommandSuccess(prepareCommand(validEmployee, model), model,
                String.format(AddEmployeeCommand.MESSAGE_SUCCESS, validEmployee), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Employee employeeInList = model.getAddressBook().getEmployeeList().get(0);
        assertCommandFailure(prepareCommand(employeeInList, model), model, AddEmployeeCommand.MESSAGE_DUPLICATE_PERSON);
    }

    /**
     * Generates a new {@code AddEmployeeCommand} which upon execution, adds {@code employee} into the {@code model}.
     */
    private AddEmployeeCommand prepareCommand(Employee employee, Model model) {
        AddEmployeeCommand command = new AddEmployeeCommand(employee);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
