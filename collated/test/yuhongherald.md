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
    private static final String TEST_RESULT_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-results.xlsx";
    private static final String TEST_OUTPUT_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-comments.xlsx";
    private static final String EMPTY_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-empty.xls";
    private static final String EMPTY_OUTPUT_FILE =
            "storage/session/ImportSessionTest/CS2103-testsheet-empty-comments.xls";
    private static final String EMPTY_FILE_MESSAGE =
            "Missing header fields: client name, client phone, client email, "
            + "vehicle number, employee name, employee phone, employee email, ";
    private static final String NO_JOBS_MESSAGE = "Sheet 1 contains no valid job entries!";
    private String inputPath;
    private String outputPath;
    private String testPath;
    private String emptyPath;
    private String emptyOutputPath;

    @Before
    public void cleanUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        inputPath = classLoader.getResource(TEST_INPUT_FILE).getPath();
        testPath = classLoader.getResource(TEST_RESULT_FILE).getPath();
        emptyPath = classLoader.getResource(EMPTY_FILE).getPath();
        try {
            outputPath = classLoader.getResource(TEST_OUTPUT_FILE).getPath();
            deleteFile(outputPath);
        } catch (NullPointerException e) {
            ;
        }
        try {
            emptyOutputPath = classLoader.getResource(EMPTY_OUTPUT_FILE).getPath();
            deleteFile(emptyOutputPath);
        } catch (NullPointerException e) {
            ;
        }
    }

    @Test
    public void importTestFileWithErrorCorrection() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        ImportSession importSession = ImportSession.getInstance();

        File inputFile = new File(inputPath);
        importSession.initializeSession(inputFile.getPath());
        importSession.getSessionData().reviewAllRemainingJobEntries(true, "");
        String outputFilePath = importSession.closeSession();
        outputPath = classLoader.getResource(TEST_OUTPUT_FILE).getPath();
        File testFile = new File(testPath);
        File outputFile = new File(outputFilePath);
        File expectedOutputFile = new File(outputPath);
        assertEquals(expectedOutputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        importSession.getSessionData().freeResources();
        assertExcelFilesEquals(testFile, outputFile);
        try {
            importSession.initializeSession(inputPath);
        } catch (FileFormatException e) {
            assertEquals(NO_JOBS_MESSAGE, e.getMessage());
        } finally {
            deleteFile(outputFilePath);
            importSession.getSessionData().freeResources();
        }
    }

    @Test
    public void importTestFileEmpty() throws FileAccessException {
        ImportSession importSession = ImportSession.getInstance();

        try {
            importSession.initializeSession(emptyPath);
        } catch (FileFormatException e) {
            assertEquals(EMPTY_FILE_MESSAGE, e.getMessage());
        } finally {
            importSession.getSessionData().freeResources();
        }
    }

    /**
     * Asserts that 2 excel files are equal:
     * The have the same number of sheets
     * In each sheet, they have the same number of rows
     * In each row, they have the same number of columns
     * In each cell, they have the same content
     */
    private void assertExcelFilesEquals(File file1, File file2) throws IOException, InvalidFormatException {
        Workbook workbook1 = WorkbookFactory.create(file1);
        Workbook workbook2 = WorkbookFactory.create(file2);
        assertEquals(workbook1.getNumberOfSheets(), workbook2.getNumberOfSheets());
        Sheet sheet1;
        Sheet sheet2;
        for (int i = 0; i < workbook1.getNumberOfSheets(); i++) {
            sheet1 = workbook1.getSheetAt(workbook1.getFirstVisibleTab() + i);
            sheet2 = workbook2.getSheetAt(workbook1.getFirstVisibleTab() + i);
            assertSheetEquals(sheet1, sheet2);
        }
    }

    /**
     * Asserts that 2 sheets are equal:
     * In each sheet, they have the same number of rows
     * In each row, they have the same number of columns
     * In each cell, they have the same content
     */
    private void assertSheetEquals(Sheet sheet1, Sheet sheet2) {
        assertEquals(sheet1.getPhysicalNumberOfRows(), sheet2.getPhysicalNumberOfRows());
        Row row1;
        Row row2;
        for (int i = sheet1.getFirstRowNum(); i <= sheet1.getLastRowNum(); i++) {
            row1 = sheet1.getRow(i);
            row2 = sheet2.getRow(i);
        }
    }

    /**
     * Asserts that 2 rows are equal:
     * In each row, they have the same number of columns
     * In each cell, they have the same content
     */
    private void assertRowEquals(Row row1, Row row2) {
        assertEquals(row1.getPhysicalNumberOfCells(), row2.getPhysicalNumberOfCells());
        Cell cell1;
        Cell cell2;
        for (int i = row1.getFirstCellNum(); i <= row1.getLastCellNum(); i++) {
            cell1 = row1.getCell(i);
            cell2 = row2.getCell(i);
        }
    }

    /**
     * Asserts that 2 cells are equal:
     * In each cell, they have the same content
     */
    private void assertCellEquals(Cell cell1, Cell cell2) {
        DataFormatter dataFormatter = new DataFormatter();
        assertEquals(dataFormatter.formatCellValue(cell1), dataFormatter.formatCellValue(cell2));
    }

    /**
     * Deletes file at (@String filePath) if file exists
     */
    private void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
```
