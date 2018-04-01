package seedu.carvicim.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.carvicim.logic.commands.CommandTestUtil.deleteFirstPerson;
import static seedu.carvicim.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;

public class UndoableCommandTest {
    private final Model model = new ModelManager(getTypicalCarvicim(), new UserPrefs());
    private final DummyCommand dummyCommand = new DummyCommand(model);

    private Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());

    @Test
    public void executeUndo() throws Exception {
        dummyCommand.execute();
        deleteFirstPerson(expectedModel);
        assertEquals(expectedModel, model);

        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // undo() should cause the model's filtered list to show all persons
        dummyCommand.undo();
        expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // redo() should cause the model's filtered list to show all persons
        dummyCommand.redo();
        deleteFirstPerson(expectedModel);
        assertEquals(expectedModel, model);
    }

    /**
     * Deletes the first employee in the model's filtered list.
     */
    class DummyCommand extends UndoableCommand {
        DummyCommand(Model model) {
            this.model = model;
        }

        @Override
        public CommandResult executeUndoableCommand() throws CommandException {
            Employee employeeToDelete = model.getFilteredPersonList().get(0);
            try {
                model.deletePerson(employeeToDelete);
            } catch (EmployeeNotFoundException pnfe) {
                fail("Impossible: employeeToDelete was retrieved from model.");
            }
            return new CommandResult("");
        }
    }
}
