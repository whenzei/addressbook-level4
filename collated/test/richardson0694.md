# richardson0694
###### \java\seedu\carvicim\logic\commands\AnalyseCommandTest.java
``` java
public class AnalyseCommandTest {
    private Model model;
    private Model expectedModel;
    private AnalyseCommand analyseCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());

        analyseCommand = new AnalyseCommand();
        analyseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void executeSuccess() {
        String expectedMessage = analyseCommand.execute().feedbackToUser;
        assertCommandSuccess(analyseCommand, model, expectedMessage, expectedModel);
    }
}
```
###### \java\seedu\carvicim\logic\commands\ArchiveCommandTest.java
``` java
public class ArchiveCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    @Test
    public void constructor_nullDateRange_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ArchiveCommand(null);
    }

    @Test
    public void executeSuccess() throws CommandException {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        DateRange dateRange = new DateRangeBuilder().build();
        ArchiveCommand archiveCommand = new ArchiveCommand(dateRange);
        archiveCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        CommandResult commandResult = archiveCommand.execute();
        assertEquals(ArchiveCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
    }

    @Test
    public void executeUnsuccess_invalidDateRange() throws Exception {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        DateRange dateRange = new DateRangeBuilder().withDateRange("Mar 25 2018", "Mar 03 2018").build();
        ArchiveCommand archiveCommand = new ArchiveCommand(dateRange);
        archiveCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_DATERANGE);
        archiveCommand.execute();
    }

    @Test
    public void executeUnsuccess_noJobs() throws Exception {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        DateRange dateRange = new DateRangeBuilder().withDateRange("Jan 01 2018", "Jan 01 2018").build();
        ArchiveCommand archiveCommand = new ArchiveCommand(dateRange);
        archiveCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        CommandResult commandResult = archiveCommand.execute();
        assertEquals(ArchiveCommand.MESSAGE_UNSUCCESS, commandResult.feedbackToUser);
    }

}
```
###### \java\seedu\carvicim\logic\commands\SortCommandTest.java
``` java
public class SortCommandTest {
    private Model model;
    private Model expectedModel;
    private SortCommand sortCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getCarvicimNonAlphabetically(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());

        sortCommand = new SortCommand();
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void executeSuccess() {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
```
###### \java\seedu\carvicim\logic\parser\ArchiveCommandParserTest.java
``` java
public class ArchiveCommandParserTest {

    private ArchiveCommandParser parser = new ArchiveCommandParser();

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ArchiveCommand.MESSAGE_USAGE);

        // missing start date prefix
        assertParseFailure(parser, VALID_START_DATE + DATERANGE_DESC_TWO,
                expectedMessage);

        // missing end date prefix
        assertParseFailure(parser, DATERANGE_DESC_ONE + VALID_END_DATE,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid date format
        assertParseFailure(parser, INVALID_DATERANGE_DESC,
                String.format(Date.MESSAGE_DATE_CONSTRAINTS, ArchiveCommand.MESSAGE_USAGE));
    }

}
```
###### \java\seedu\carvicim\storage\XmlArchiveJobStorageTest.java
``` java
public class XmlArchiveJobStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlArchiveJobStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readArchiveJob_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readArchiveJob(null);
    }

    private java.util.Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws Exception {
        return new XmlArchiveJobStorage(filePath).readArchiveJob(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readArchiveJob("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readArchiveJob("NotXmlFormatCarvicim.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readArchiveJob_invalidJobCarvicim_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readArchiveJob("invalidJobCarvicim.xml");
    }

    @Test
    public void readArchiveJob_invalidAndValidJobCarvicim_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readArchiveJob("invalidAndValidJobCarvicim.xml");
    }

    @Test
    public void saveArchiveJob_nullArchiveJob_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveArchiveJob(null, "SomeFile.xml");
    }

    /**
     * Saves {@code carvicim} at the specified {@code filePath}.
     */
    private void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) {
        try {
            new XmlArchiveJobStorage(filePath).saveArchiveJob(carvicim, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveArchiveJob_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveArchiveJob(new Carvicim(), null);
    }

}
```
###### \java\seedu\carvicim\storage\XmlSerializableArchiveJobTest.java
``` java
public class XmlSerializableArchiveJobTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlSerializableArchiveJobTest/");
    private static final File TYPICAL_JOBS_FILE = new File(TEST_DATA_FOLDER + "typicalJobsCarvicim.xml");
    private static final File INVALID_JOB_FILE = new File(TEST_DATA_FOLDER + "invalidJobCarvicim.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalJobsFile_success() throws Exception {
        XmlSerializableArchiveJob dataFromFile = XmlUtil.getDataFromFile(TYPICAL_JOBS_FILE,
               XmlSerializableArchiveJob.class);
        Carvicim carvicimFromFile = dataFromFile.toModelType();
        Carvicim typicalJobsCarvicim = TypicalEmployees.getTypicalCarvicimWithArchivedJob();
        assertEquals(carvicimFromFile, typicalJobsCarvicim);
    }

    @Test
    public void toModelType_invalidJobFile_throwsIllegalValueException() throws Exception {
        XmlSerializableArchiveJob dataFromFile = XmlUtil.getDataFromFile(INVALID_JOB_FILE,
                XmlSerializableArchiveJob.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

}
```
###### \java\seedu\carvicim\testutil\DateRangeBuilder.java
``` java
/**
 * A utility class to help with building DateRange objects.
 */
public class DateRangeBuilder {

    public static final String DEFAULT_START_DATE = "Mar 01 2018";
    public static final String DEFAULT_END_DATE = "Mar 25 2018";

    private Date startDate;
    private Date endDate;

    public DateRangeBuilder() {
        startDate = new Date(DEFAULT_START_DATE);
        endDate = new Date(DEFAULT_END_DATE);
    }

    /**
     * Initializes the DateRangeBuilder with the data of {@code dateRangeToCopy}.
     */
    public DateRangeBuilder(DateRange dateRangeToCopy) {
        startDate = dateRangeToCopy.getStartDate();
        endDate = dateRangeToCopy.getEndDate();
    }
    /**
     * Sets the {@code Date} of the {@code DateRange} that we are building.
     */
    public DateRangeBuilder withDateRange(String startDate, String endDate) {
        this.startDate = new Date(startDate);
        this.endDate = new Date(endDate);
        return this;
    }

    public DateRange build() {
        return new DateRange(startDate, endDate);
    }

}
```
###### \java\seedu\carvicim\testutil\TypicalEmployees.java
``` java
    /**
     * Returns an {@code Carvicim} with all the typical employees' name non alphabetically.
     */
    public static Carvicim getCarvicimNonAlphabetically() {
        Carvicim ab = new Carvicim();
        for (Employee employee : getTypicalEmployeesNonAlphabetically()) {
            try {
                ab.addEmployee(employee);
            } catch (DuplicateEmployeeException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    /**
     * Returns an {@code Carvicim} with all typical employees and two jobs.
     */
    public static Carvicim getTypicalCarvicimWithJobs() {
        Carvicim ab = new Carvicim();
        for (Employee employee : getTypicalEmployees()) {
            try {
                ab.addEmployee(employee);
            } catch (DuplicateEmployeeException e) {
                throw new AssertionError("not possible");
            }
        }

        UniqueEmployeeList assignedEmployeeList = new UniqueEmployeeList();
        try {
            assignedEmployeeList.add(ALICE);
        } catch (DuplicateEmployeeException e) {
            e.printStackTrace();
        }

        Job firstJob = new Job(new ClientBuilder().build(), new VehicleNumber(VALID_VEHICLE_NUMBER_A),
                new JobNumber("1"), new Date("Mar 01 2018"), assignedEmployeeList, new Status(Status.STATUS_CLOSED),
                new RemarkList());
        Job secondJob = new Job(new ClientBuilder().build(), new VehicleNumber(VALID_VEHICLE_NUMBER_B),
                new JobNumber("2"), new Date("Mar 01 2018"), assignedEmployeeList, new Status(Status.STATUS_ONGOING),
                new RemarkList());

        ab.addJob(firstJob);
        ab.addJob(secondJob);

        return ab;
    }

    /**
     * Returns an {@code Carvicim} with one archived job.
     */
    public static Carvicim getTypicalCarvicimWithArchivedJob() {
        Carvicim ab = new Carvicim();
        UniqueEmployeeList assignedEmployeeList = new UniqueEmployeeList();
        try {
            assignedEmployeeList.add(ALICE);
        } catch (DuplicateEmployeeException e) {
            e.printStackTrace();
        }

        Job firstJob = new Job(new ClientBuilder().build(), new VehicleNumber(VALID_VEHICLE_NUMBER_A),
                new JobNumber("1"), new Date("Mar 01 2018"), assignedEmployeeList, new Status(Status.STATUS_CLOSED),
                new RemarkList());

        ab.addJob(firstJob);

        return ab;
    }

```
