package seedu.carvicim.logic.commands;

import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.testutil.EmployeeBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddEmployeeCommand}.
 */
public class AddEmployeeCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicim(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() throws Exception {
        Employee validEmployee = new EmployeeBuilder().build();

        Model expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.addPerson(validEmployee);

        assertCommandSuccess(prepareCommand(validEmployee, model), model,
                String.format(AddEmployeeCommand.MESSAGE_SUCCESS, validEmployee), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Employee employeeInList = model.getCarvicim().getEmployeeList().get(0);
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
