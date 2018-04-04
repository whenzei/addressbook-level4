# yuhongherald
###### \java\seedu\carvicim\logic\commands\SetCommandTest.java
``` java
public class SetCommandTest {

    @Test
    public void execute_changeAdd_success() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(expectedModel, currentWord, newWord);
        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageSuccess(), expectedModel);
    }

    @Test
    public void execute_changeAddUsingDefault_success() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(actualModel, currentWord, newWord);
        newWord = getUnusedCommandWord(actualModel);
        setCommandWord(expectedModel, currentWord, newWord);
        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageSuccess(), expectedModel);
    }

    @Test
    public void execute_changeAddBackToDefault_success() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(actualModel, currentWord, newWord);
        SetCommand newCommand = prepareCommand(actualModel, newWord, currentWord);
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageSuccess(), expectedModel);
    }


    @Test
    public void execute_changeSet_success() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = SetCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(expectedModel, currentWord, newWord);
        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageSuccess(), expectedModel);

        setCommandWord(expectedModel, newWord, currentWord);
        SetCommand newCommand2 = prepareCommand(actualModel, newWord, currentWord);
        assertCommandSuccess(newCommand2, actualModel, newCommand2.getMessageSuccess(), expectedModel);
    }

    @Test
    public void execute_changeCommand_failureUsed() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(actualModel, SetCommand.COMMAND_WORD, newWord);
        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandFailure(newCommand, actualModel, newCommand.getMessageUsed());
    }

    @Test
    public void execute_changeCommand_failureDefault() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = SetCommand.COMMAND_WORD;

        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandFailure(newCommand, actualModel, CommandWords.getMessageOverwriteDefault(newWord));
    }


    @Test
    public void execute_changeCommand_failureUnused() {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model testModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = getUnusedCommandWord(actualModel);
        String newWord = getUnusedCommandWord(actualModel, currentWord);

        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandFailure(newCommand, actualModel, newCommand.getMessageUnused());
    }

    @Test
    public void execute_changeCommand_failureNoChange() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = currentWord;

        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandFailure(newCommand, actualModel, CommandWords.getMessageNoChange());
    }

    private void setCommandWord(Model expectedModel, String currentWord, String newWord) throws CommandWordException {
        expectedModel.getCommandWords().setCommandWord(currentWord, newWord);
    }

    public static String getUnusedCommandWord(Model actualModel) {
        String newWord = "a";
        for (int i = 0; i < actualModel.getCommandWords().commands.size(); i++) {
            if (!actualModel.getCommandWords().commands.containsValue(newWord)) {
                return newWord;
            }
            newWord += "a";
        }
        return newWord;
    }

    public static String getUnusedCommandWord(Model actualModel, String otherWord) {
        if (otherWord == null || otherWord.equals("")) {
            return  getUnusedCommandWord(actualModel);
        }
        String newWord = "a";
        for (int i = 0; i < actualModel.getCommandWords().commands.size(); i++) {
            if (!actualModel.getCommandWords().commands.containsValue(newWord)
                    && !newWord.equals(otherWord)) {
                return newWord;
            }
            newWord += "a";
        }
        return newWord;
    }

    /**
     * Generates a new {@code SetCommand} which upon execution replaces (@code currentWord) with (@code newWord).
     */
    private SetCommand prepareCommand(Model model, String currentWord, String newWord) {
        SetCommand command = new SetCommand(currentWord, newWord);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\carvicim\logic\parser\SetCommandParserTest.java
``` java
public class SetCommandParserTest {
    private SetCommandParser parser = new SetCommandParser();

    @Test
    public void parse_twoCommandWords_success() {
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getWord();
        String args = String.join(" ", currentWord, newWord);
        assertParseSuccess(parser, args, new SetCommand(currentWord, newWord));
    }

    @Test
    public void parse_noCommandWord_failure() {
        String currentWord = "";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetCommand.MESSAGE_USAGE);
        assertParseFailure(parser, currentWord, expectedMessage);
    }

    @Test
    public void parse_oneCommandWord_failure() {
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetCommand.MESSAGE_USAGE);
        assertParseFailure(parser, currentWord, expectedMessage);
    }

    public static String getWord() {
        return "a";
    }
}
```
###### \java\seedu\carvicim\storage\ImportSessionTest.java
``` java
public class ImportSessionTest {
    private static final String TEST_INPUT_FILE = "storage/session/ImportSessionTest/CS2103-testsheet.xlsx";
    private static final String TEST_OUTPUT_FILE = "";
    private static final String OUTFILE_NAME = "outFile";

    @Test
    public void importTestFileWithErrorCorrection() throws Exception {
        ImportSession importSession = ImportSession.getInstance();

        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource(TEST_INPUT_FILE)
                .getPath();
        importSession.initializeSession(path);
        importSession.reviewAllRemainingJobEntries(true);
        importSession.closeSession();
        //deleteFile(OUTFILE_NAME);
    }

    private void compareExcelFiles() {
        ;
    }

    private void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }
}
```
