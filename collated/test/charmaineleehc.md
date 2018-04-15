# charmaineleehc
###### \java\seedu\carvicim\logic\commands\EmailCommandTest.java
``` java
public class EmailCommandTest {

    private Model model;

    @Before
    public void setUp() {
        this.model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
    }

    @Test
    public void execute_outOfBoundsJobNumber_failure() {
        String outOfBoundsJobNumber = Integer.toString(model.getFilteredJobList().size() + 1);
        EmailCommand emailCommand = new EmailCommand(new JobNumber(outOfBoundsJobNumber));
        emailCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(emailCommand, model, Messages.MESSAGE_INVALID_JOB_NUMBER);
    }

    @Test
    public void equals() {
        EmailCommand emailCommandJobOne = new EmailCommand(new JobNumber("1"));
        EmailCommand emailCommandJobTwo = new EmailCommand(new JobNumber("2"));

        assertTrue(emailCommandJobOne.equals(emailCommandJobOne));

        EmailCommand emailCommandJobOneCopy = new EmailCommand(new JobNumber("1"));
        assertTrue(emailCommandJobOne.equals(emailCommandJobOneCopy));

        assertFalse(emailCommandJobOne.equals(1));

        assertFalse(emailCommandJobOne.equals(null));

        assertFalse(emailCommandJobOne.equals(emailCommandJobTwo));

    }

}
```
###### \java\seedu\carvicim\logic\commands\FindByTagCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindByTagCommand}.
 */
public class FindByTagCommandTest {
    private Model model = new ModelManager(getTypicalCarvicim(), new UserPrefs());

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("first"));
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("second"));

        FindByTagCommand findFirstCommand = new FindByTagCommand(firstPredicate);
        FindByTagCommand findSecondCommand = new FindByTagCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindByTagCommand findFirstCommandCopy = new FindByTagCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindByTagCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_oneKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindByTagCommand command = prepareCommand("technician");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        FindByTagCommand command = prepareCommand("mechanic technician");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    @Test
    public void execute_nonExistantTag_noPersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindByTagCommand command = prepareCommand("actor");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    /**
     * Parses {@code userInput} into a {@code FindByTagCommand}.
     */
    private FindByTagCommand prepareCommand(String userInput) {
        FindByTagCommand command =
                new FindByTagCommand(new TagContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     *     - the {@code Carvicim} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindByTagCommand command, String expectedMessage,
                                      List<Employee> expectedList) {
        Carvicim expectedCarvicim = new Carvicim(model.getCarvicim());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedCarvicim, model.getCarvicim());
    }
}
```
###### \java\seedu\carvicim\logic\parser\CarvicimParserTest.java
``` java
    @Test
    public void parseCommand_findByTag() throws Exception {
        List<String> keywords = Arrays.asList("mechanic", "technician");
        FindByTagCommand command = (FindByTagCommand) parser.parseCommand(
                FindByTagCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindByTagCommand(new TagContainsKeywordsPredicate(keywords)), command);
    }
```
###### \java\seedu\carvicim\logic\parser\CarvicimParserTest.java
``` java
    @Test
    public void parseCommand_email() throws Exception {
        EmailCommand command = (EmailCommand) parser.parseCommand(
                EmailCommand.COMMAND_WORD + " " + PREFIX_JOB_NUMBER + "1");
        assertEquals(new EmailCommand(new JobNumber("1")), command);
    }
```
###### \java\seedu\carvicim\logic\parser\EmailCommandParserTest.java
``` java
public class EmailCommandParserTest {

    private EmailCommandParser parser = new EmailCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + EmailCommand.COMMAND_WORD + " " + PREFIX_JOB_NUMBER + "1",
                new EmailCommand(new JobNumber("1")));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE);

        //job number not stated
        assertParseFailure(parser, EmailCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, EmailCommand.COMMAND_WORD + " " + PREFIX_JOB_NUMBER
                + "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\carvicim\logic\parser\FindByTagCommandParserTest.java
``` java
public class FindByTagCommandParserTest {
    private FindByTagCommandParser parser = new FindByTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindByTagCommand expectedFindByTagCommand =
                new FindByTagCommand(new TagContainsKeywordsPredicate(Arrays.asList("technician", "mechanic")));
        assertParseSuccess(parser, "technician mechanic", expectedFindByTagCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n technician \n \t mechanic  \t", expectedFindByTagCommand);
    }

}
```
###### \java\seedu\carvicim\model\person\TagContainsKeywordsPredicateTest.java
``` java
public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TagContainsKeywordsPredicate firstPredicate = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        TagContainsKeywordsPredicate secondPredicate = new TagContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate firstPredicateCopy = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personHasTagsMatchingKeywords_returnsTrue() {
        // One keyword
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("mechanic"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("mechanic", "technician").build()));

        // Multiple keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mechanic", "technician"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("mechanic", "technician").build()));

        // Only one matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mechanic", "technician"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("cleaner", "technician").build()));

        // Mixed-case keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mEcHaNiC", "TeChNiCiAn"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("mechanic", "technician").build()));

    }

    @Test
    public void test_personDoesNotHaveTagsContainingKeywords_returnsFalse() {
        // Zero keywords
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new EmployeeBuilder().withTags("mechanic").build()));

        // Non-matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mechanic"));
        assertFalse(predicate.test(new EmployeeBuilder().withTags("technician", "cleaner").build()));

        // Keywords match phone, email and address, but does not match any tag name
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("12345", "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new EmployeeBuilder().withTags("mechanic").withPhone("12345")
                .withEmail("alice@email.com").build()));
    }
}
```
