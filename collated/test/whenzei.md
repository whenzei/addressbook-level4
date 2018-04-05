# whenzei
###### \java\seedu\carvicim\logic\commands\AddJobCommandTest.java
``` java
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
```
###### \java\seedu\carvicim\logic\commands\CloseJobCommandTest.java
``` java

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code CloseJobCommand}
 */
public class CloseJobCommandTest {
    private Model model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());

    @Test
    public void execute_closeJobFailure_jobNotFound() {
        CloseJobCommand closeJobCommand = prepareCommand(new JobNumber("999999"));
        assertCommandFailure(closeJobCommand, model, MESSAGE_JOB_NOT_FOUND);
    }

    @Test
    public void execute_closeJobSuccess_jobIsPresent() throws Exception {
        Job jobToClose = model.getFilteredJobList().get(INDEX_FIRST_JOB.getZeroBased());

        CloseJobCommand closeJobCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));

        String expectedMessage = String.format(CloseJobCommand.MESSAGE_CLOSE_JOB_SUCCESS, jobToClose);
        ModelManager expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.closeJob(jobToClose);

        assertCommandSuccess(closeJobCommand, model, expectedMessage, expectedModel);

    }

    /**
     * 1. Closes a {@code Job} from CarviciM
     * 2. Undo the closing of job
     * 3. Redo the closing of job
     */
    @Test
    public void executeUndoRedo_sameJobClosed() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CloseJobCommand closeJobCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));
        Model expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        Job jobToClose = model.getFilteredJobList().get(INDEX_FIRST_JOB.getZeroBased());

        // close -> closes the job number 1 which is the first job in the job list
        closeJobCommand.execute();
        undoRedoStack.push(closeJobCommand);

        // undo -> reverts Carvicim back to previous state
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.closeJob(jobToClose);
        assertNotEquals(jobToClose, model.getFilteredPersonList().get(INDEX_FIRST_JOB.getZeroBased()));
        // redo -> closes same Job of job number 1
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);

    }

    @Test
    public void equals() throws Exception {
        CloseJobCommand closeJobNumberOneCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));
        CloseJobCommand closeJobNumberTwoCommand = prepareCommand(new JobNumber(VALID_JOB_NUMBER_TWO));

        // same object -> returns ture
        assertTrue(closeJobNumberOneCommand.equals(closeJobNumberOneCommand));

        // same values -> returns true
        CloseJobCommand closeJobNumberOneCommandCopy = prepareCommand(new JobNumber(VALID_JOB_NUMBER_ONE));
        assertTrue(closeJobNumberOneCommand.equals(closeJobNumberOneCommandCopy));

        // one command preprocessed when previously equal -> returns false
        closeJobNumberOneCommandCopy.preprocessUndoableCommand();
        assertFalse(closeJobNumberOneCommand.equals(closeJobNumberOneCommandCopy));

        // null -> returns false
        assertFalse(closeJobNumberOneCommand.equals(null));

        // different job -> returns false
        assertFalse(closeJobNumberOneCommand.equals(closeJobNumberTwoCommand));

    }

    /**
     * Returns a {@code CloseJobCommand} with the parameter {@code jobNumber}.
     */
    public CloseJobCommand prepareCommand(JobNumber jobNumber) {
        CloseJobCommand closeJobCommand = new CloseJobCommand(jobNumber);
        closeJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return  closeJobCommand;
    }
}
```
###### \java\seedu\carvicim\logic\commands\ListJobCommandTest.java
``` java

import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithAssignedJobs;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListJobCommand.
 */
public class ListJobCommandTest {

    private Model model;
    private Model expectedModel;
    private ListJobCommand listJobCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());
        expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());

        listJobCommand = new ListJobCommand();
        listJobCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showSameList() {
        assertCommandSuccess(listJobCommand, model, ListJobCommand.MESSAGE_SUCCESS, expectedModel);
    }



}
```
###### \java\seedu\carvicim\logic\commands\RemarkCommandTest.java
``` java
public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalCarvicimWithAssignedJobs(), new UserPrefs());

    @Test
    public void equals() {
        RemarkCommand remarkCommand1 = prepareCommand("abc", "1");
        RemarkCommand remarkCommand2 = prepareCommand("def", "2");

        // same object -> returns true
        assertTrue(remarkCommand1.equals(remarkCommand1));

        // same values -> returns true
        RemarkCommand remarkCommandCopy = new RemarkCommand(new Remark("abc"), new JobNumber("1"));
        assertTrue(remarkCommand1.equals(remarkCommandCopy));

        // different types -> returns false
        assertFalse(remarkCommand1.equals(1));

        // different jobs -> return false
        assertFalse(remarkCommand1.equals(remarkCommand2));

        // null -> return false
        assertFalse(remarkCommand1.equals(null));
    }

    @Test
    public void execute_remarkSuccess() throws Exception {
        RemarkCommand remarkCommand = prepareCommand("abc", "1");
        // Get first job
        Job targetJob = model.getFilteredJobList().get(0);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_REMARK_SUCCESS, new Remark("abc"));
        ModelManager expectedModel = new ModelManager(model.getCarvicim(), new UserPrefs());
        expectedModel.addRemark(targetJob, new Remark("abc"));

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_remarkFailure() {
        RemarkCommand remarkCommand = prepareCommand("abc", "100");
        // Get first job
        Job targetJob = model.getFilteredJobList().get(0);

        assertCommandFailure(remarkCommand, model, MESSAGE_JOB_NOT_FOUND);
    }

    private RemarkCommand prepareCommand(String remark, String jobNumber) {
        RemarkCommand remarkCommand = new RemarkCommand(new Remark(remark), new JobNumber(jobNumber));
        remarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return remarkCommand;
    }
}
```
###### \java\seedu\carvicim\logic\commands\ThemeCommandTest.java
``` java
public class ThemeCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_setTheme_success() {
        assertExecutionSuccess(INDEX_FIRST_THEME);
    }

    @Test
    public void execute_setTheme_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(NUMBER_OF_THEMES + 1);
        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_THEME_INDEX);
    }

    /**
     * Executes a {@code ThemeCommand} with the given {@code index}, and checks that {@code SetThemeRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        ThemeCommand themeCommand = prepareCommand(index);

        try {
            CommandResult commandResult = themeCommand.execute();
            assertEquals(String.format(ThemeCommand.MESSAGE_THEME_CHANGE_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        SetThemeRequestEvent lastEvent = (SetThemeRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, lastEvent.getSelectedIndex());
    }

    /**
     * Executes a {@code ThemeCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        ThemeCommand themeCommand = prepareCommand(index);

        try {
            themeCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code ThemeCommand} with parameters {@code index}.
     */
    private ThemeCommand prepareCommand(Index index) {
        ThemeCommand themeCommand = new ThemeCommand(index);
        return themeCommand;
    }
}
```
###### \java\seedu\carvicim\logic\parser\AddJobCommandParserTest.java
``` java
public class AddJobCommandParserTest {

    private AddJobCommandParser parser = new AddJobCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedClient = new ClientBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).build();

        // one assigned employee
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE,
                new AddJobCommand(expectedClient, new VehicleNumber(VALID_VEHICLE_NUMBER_A),
                        generateOneValidEmployeeIndex()));

        // two assigned employees
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_TWO,
                new AddJobCommand(expectedClient, new VehicleNumber(VALID_VEHICLE_NUMBER_A),
                        generateTwoValidEmployeeIndices()));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);
        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB
                + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);

        // missing vehicle number prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB
                + VALID_VEHICLE_NUMBER_A + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);

        // missing assigned employee prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB
                + VALID_VEHICLE_NUMBER_A + VALID_ASSIGNED_EMPLOYEE_INDEX_A, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid vehicle number
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_VEHICLE_NUM_DESC + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));

        //invalid assigned employee indices
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + INVALID_ASSIGNED_EMPLOYEE_INDEX_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
    }

    /**
     * Generates an Arraylist of valid first assigned employee index
     */
    private ArrayList<Index> generateOneValidEmployeeIndex() {
        ArrayList<Index> indices = new ArrayList<Index>();
        indices.add(INDEX_FIRST_PERSON);
        return indices;
    }

    /**
     * Generates an Arraylist of valid first and second assigned employee index
     */
    private ArrayList<Index> generateTwoValidEmployeeIndices() {
        ArrayList<Index> indices = new ArrayList<Index>();
        indices.add(INDEX_FIRST_PERSON);
        indices.add(INDEX_SECOND_PERSON);
        return indices;
    }
}
```
###### \java\seedu\carvicim\logic\parser\CloseJobCommandParserTest.java
``` java
public class CloseJobCommandParserTest {

    private CloseJobCommandParser parser = new CloseJobCommandParser();

    @Test
    public void parse_validArgs_returnsCloseJobCommand() {
        assertParseSuccess(parser, JOB_NUMBER_DESC_A, new CloseJobCommand(new JobNumber(VALID_JOB_NUMBER_ONE)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, INVALID_JOB_NUMBER_DESC, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CloseJobCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\carvicim\logic\parser\RemarkCommandParserTest.java
``` java
public class RemarkCommandParserTest {
    private RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Remark expectedRemark = new Remark(VALID_REMARK);
        JobNumber expectedJobNumber = new JobNumber(VALID_JOB_NUMBER_ONE);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + JOB_NUMBER_DESC_A + REMARK_DESC,
                new RemarkCommand(expectedRemark, expectedJobNumber));

        // multiple job number - last job taken
        assertParseSuccess(parser, JOB_NUMBER_DESC_B + JOB_NUMBER_DESC_A + REMARK_DESC,
                new RemarkCommand(expectedRemark, expectedJobNumber));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);

        // missing job number prefix
        assertParseFailure(parser, VALID_JOB_NUMBER_ONE + REMARK_DESC, expectedMessage);

        // missing remark prefix
        assertParseFailure(parser, JOB_NUMBER_DESC_A + VALID_REMARK, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid job number
        assertParseFailure(parser, INVALID_JOB_NUMBER_DESC + REMARK_DESC, JobNumber.MESSAGE_JOB_NUMBER_CONSTRAINTS);

        // invalid remark
        assertParseFailure(parser, JOB_NUMBER_DESC_A + INVALID_REMARK_DESC, Remark.MESSAGE_REMARKS_CONSTRAINTS);
    }
}
```
###### \java\seedu\carvicim\logic\parser\ThemeCommandParserTest.java
``` java
/**
 * Test scope: similar to {@code DeleteEmployeeCommandParserTest}.
 * @see DeleteEmployeeCommandParserTest
 */

public class ThemeCommandParserTest {

    private ThemeCommandParser parser = new ThemeCommandParser();

    @Test
    public void parse_validArgs_returnsThemeCommand() {
        assertParseSuccess(parser, "1", new ThemeCommand(INDEX_FIRST_THEME));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, ThemeCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\carvicim\storage\XmlAdaptedJobTest.java
``` java
public class XmlAdaptedJobTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_VEHICLE_NUMBER = "";
    private static final String INVALID_DATE = "F3b A 2018";
    private static final String INVALID_JOB_NUMBER = "-1";

    private static final String VALID_NAME = new ClientBuilder().build().getName().fullName;
    private static final String VALID_PHONE = new ClientBuilder().build().getPhone().value;
    private static final String VALID_EMAIL = new ClientBuilder().build().getEmail().value;
    private static final String VALID_VEHICLE_NUMBER = "SHG123";
    private static final String VALID_DATE = "Feb 02 2018";
    private static final String VALID_JOB_NUMBER = "2";
    private static final String VALID_STATUS = "ongoing";

    @Test
    public void toModelType_validJobDetails_returnsJob() throws Exception {
        Person client = new Person(new Name(VALID_NAME), new Phone(VALID_PHONE), new Email(VALID_EMAIL));
        UniqueEmployeeList assignedEmployees = new UniqueEmployeeList();
        assignedEmployees.add(BENSON);
        assignedEmployees.add(CARL);

        Job testJob = new Job(client, new VehicleNumber(VALID_VEHICLE_NUMBER), new JobNumber(VALID_JOB_NUMBER),
                new Date(VALID_DATE), assignedEmployees, new Status(VALID_STATUS), new RemarkList());

        XmlAdaptedJob job = new XmlAdaptedJob(testJob);
        assertEquals(testJob, job.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_jobNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(INVALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = JobNumber.MESSAGE_JOB_NUMBER_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, INVALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Date.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidVehicleNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                INVALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = VehicleNumber.MESSAGE_VEHICLE_ID_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullVehicleNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, VehicleNumber.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullJobNumber_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(null, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, JobNumber.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, null, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, null, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, null,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, null, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void toModelType_nullStatus_throwsIllegalValueException() throws DuplicateEmployeeException {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, null, VALID_DATE, assignedEmployees, remarks);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, job::toModelType);
    }

    @Test
    public void equals() {
        XmlAdaptedEmployee sampleEmployee = generateSampleEmployee();
        List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
        assignedEmployees.add(sampleEmployee);

        List<XmlAdaptedRemark> remarks = new ArrayList<>();

        XmlAdaptedJob job1 = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        XmlAdaptedJob job2 = new XmlAdaptedJob(VALID_JOB_NUMBER + 1, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);

        // same object -> returns true
        assertTrue(job1.equals(job1));

        // same values -> returns true
        XmlAdaptedJob jobCopy = new XmlAdaptedJob(VALID_JOB_NUMBER, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_VEHICLE_NUMBER, VALID_STATUS, VALID_DATE, assignedEmployees, remarks);
        assertTrue(job1.equals(jobCopy));

        // different types -> returns false
        assertFalse(job1.equals(1));

        // null -> returns false
        assertFalse(job1.equals(null));

        // different jobs -> return false
        assertFalse(job1.equals(job2));

    }

    /**Generates a sample employe in the Xml form*/
    private XmlAdaptedEmployee generateSampleEmployee() {
        List<XmlAdaptedTag> bensonTags = new ArrayList<>();
        bensonTags.add(new XmlAdaptedTag("mechanic"));
        bensonTags.add(new XmlAdaptedTag("technician"));

        XmlAdaptedEmployee benson = new XmlAdaptedEmployee(BENSON.getName().fullName, BENSON.getPhone().value,
                BENSON.getEmail().value, bensonTags);

        return benson;
    }
}

```
###### \java\seedu\carvicim\testutil\ClientBuilder.java
``` java
/**
 * A utility class to help with building Client objects.
 */
public class ClientBuilder {
    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";

    private Name name;
    private Phone phone;
    private Email email;

    public ClientBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
    }
    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public ClientBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public ClientBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public ClientBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email);
    }
}
```
###### \java\seedu\carvicim\testutil\JobBuilder.java
``` java
/**
 * A utility class to help with building Job objects.
 */
public class JobBuilder {
    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";

    private Person client;
    private VehicleNumber vehicleNumber;
    private UniqueEmployeeList assignedEmployees;
    private Status status;
    private Date date;
    private RemarkList remarks;
    private JobNumber jobNumber;

    public JobBuilder(ObservableList<Employee> employees) throws Exception {
        Name name = new Name(DEFAULT_NAME);
        Phone phone = new Phone(DEFAULT_PHONE);
        Email email = new Email(DEFAULT_EMAIL);
        JobNumber.initialize("0");

        client = new Person(name, phone, email);
        vehicleNumber = new VehicleNumber(VehicleNumber.DEFAULT_VEHICLE_NUMBER);
        status = new Status(Status.STATUS_ONGOING);
        date = new Date();
        jobNumber = new JobNumber();
        remarks = new RemarkList();
        assignedEmployees = new UniqueEmployeeList();
        assignedEmployees.add(employees.get(INDEX_FIRST_PERSON.getZeroBased()));
        assignedEmployees.add(employees.get(INDEX_SECOND_PERSON.getZeroBased()));
    }

    public Job build() {
        return new Job(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
    }
}
```
