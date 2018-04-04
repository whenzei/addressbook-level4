# richardson0694
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
    public void parse_allFieldsPresent_success() {
        DateRange expectedDateRange = new DateRangeBuilder().withDateRange(VALID_START_DATE, VALID_END_DATE).build();

        // one assigned employee
        assertParseSuccess(parser, DATERANGE_DESC_ONE + DATERANGE_DESC_TWO,
                new ArchiveCommand(expectedDateRange));
    }

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

```
