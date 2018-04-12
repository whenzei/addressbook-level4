package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Person;
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
