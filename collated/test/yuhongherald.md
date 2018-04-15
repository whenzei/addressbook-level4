# yuhongherald
###### \java\seedu\carvicim\logic\commands\AcceptAllCommandTest.java
``` java
public class AcceptAllCommandTest extends ImportCommandTestEnv {
    private Remark comment;

    private ModelIgnoreJobDates expectedModelWithoutComment;
    private ModelIgnoreJobDates expectedModelWithComment;

    @Before
    public void setup() throws Exception {
        Employee jim = new Employee(new Name("Jim"), new Phone("87654321"), new Email("jim@gmail.com"),
                Collections.emptySet());
        Person client = new Person(new Name("JD"), new Phone("91234567"), new Email("jd@gmail.com"));
        UniqueEmployeeList uniqueEmployeeList = new UniqueEmployeeList();
        uniqueEmployeeList.add(jim);
        RemarkList excelRemarkList1 = new RemarkList();
        excelRemarkList1.add(new Remark("Haha"));
        excelRemarkList1.add(new Remark("whew"));

        Employee maya = new Employee(new Name("Maya"), new Phone("87654321"), new Email("maya@gmail.com"),
                Collections.emptySet());
        Person client2 = new Person(new Name("JS"), new Phone("91234567"), new Email("js@gmail.com"));

        UniqueEmployeeList uniqueEmployeeList2 = new UniqueEmployeeList();
        uniqueEmployeeList2.add(maya);
        RemarkList excelRemarkList2 = new RemarkList();
        excelRemarkList2.add(new Remark("first"));
        excelRemarkList2.add(new Remark("second"));
        excelRemarkList2.add(new Remark("last"));

        Job job1 = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), excelRemarkList1);
        Job job2 = new Job(client2, new VehicleNumber("SXX1234X"), new JobNumber("2"), new Date(),
                uniqueEmployeeList2, new Status(Status.STATUS_ONGOING), excelRemarkList2);
        List<Job> jobList = new ArrayList<>();
        jobList.add(job1);
        jobList.add(job2);

        comment = new Remark("good job!");
        RemarkList remarkList1 = new RemarkList();
        RemarkList remarkList2 = new RemarkList();
        remarkList1.add(new Remark("Haha"));
        remarkList1.add(new Remark("whew"));
        remarkList1.add(comment);
        remarkList2.add(new Remark("first"));
        remarkList2.add(new Remark("second"));
        remarkList2.add(new Remark("last"));
        remarkList2.add(comment);

        Job jobWithComment1 = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), remarkList1);
        Job jobWithComment2 = new Job(client2, new VehicleNumber("SXX1234X"), new JobNumber("2"), new Date(),
                uniqueEmployeeList2, new Status(Status.STATUS_ONGOING), remarkList2);
        List<Job> jobListWithComment = new ArrayList<>();
        jobListWithComment.add(jobWithComment1);
        jobListWithComment.add(jobWithComment2);

        expectedModelWithoutComment = new ModelIgnoreJobDates();
        expectedModelWithoutComment.addJobsAndNewEmployees(jobList);
        expectedModelWithComment = new ModelIgnoreJobDates();
        expectedModelWithComment.addJobsAndNewEmployees(jobListWithComment);
    }

    @Test
    public void equals() throws Exception {
        String comment = "comment";
        AcceptAllCommand acceptAllCommand1 = prepareCommand(comment);
        AcceptAllCommand acceptAllCommand1Copy = prepareCommand(comment);
        AcceptAllCommand acceptAllCommand2 = prepareCommand("");

        // same object -> returns true
        assertTrue(acceptAllCommand1.equals(acceptAllCommand1));

        // same values -> returns true
        assertTrue(acceptAllCommand1.equals(acceptAllCommand1Copy));

        // different types -> returns false
        assertFalse(acceptAllCommand1.equals(1));

        // different comments -> return false
        assertFalse(acceptAllCommand1.equals(acceptAllCommand2));

        // null -> return false
        assertFalse(acceptAllCommand1.equals(null));
    }

    @Test
    public void execute_acceptAllWithoutComment_success() throws Exception {
        prepareInputFiles();
        AcceptAllCommand command = prepareCommand("");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModelWithoutComment.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_acceptAllWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        AcceptAllCommand command = prepareCommand(comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns AcceptAllCommand with {@code comments}, with default data
     */
    protected AcceptAllCommand prepareCommand(String comments) throws Exception {
        JobNumber.initialize(0);
        AcceptAllCommand command = new AcceptAllCommand(comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\carvicim\logic\commands\AcceptCommandTest.java
``` java
public class AcceptCommandTest extends ImportCommandTestEnv {
    private Remark comment;

    private ModelIgnoreJobDates expectedModelWithoutComment;
    private ModelIgnoreJobDates expectedModelWithComment;

    @Before
    public void setup() throws Exception {
        Employee jim = new Employee(new Name("Jim"), new Phone("87654321"), new Email("jim@gmail.com"),
                Collections.emptySet());
        Person client = new Person(new Name("JD"), new Phone("91234567"), new Email("jd@gmail.com"));
        UniqueEmployeeList uniqueEmployeeList = new UniqueEmployeeList();
        uniqueEmployeeList.add(jim);
        Remark existingExcelRemark = new Remark("Haha");
        Remark existingExcelRemark2 = new Remark("whew");
        RemarkList excelRemarkList = new RemarkList();
        excelRemarkList.add(existingExcelRemark);
        excelRemarkList.add(existingExcelRemark2);
        Job job = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), excelRemarkList);
        comment = new Remark("good job!");
        RemarkList remarkList = new RemarkList();
        remarkList.add(existingExcelRemark);
        remarkList.add(existingExcelRemark2);
        remarkList.add(comment);
        Job jobWithComment = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), remarkList);
        expectedModelWithoutComment = new ModelIgnoreJobDates(jim, job);
        expectedModelWithComment = new ModelIgnoreJobDates(jim, jobWithComment);
    }

    @Test
    public void equals() throws Exception {
        String comment = "comment";
        AcceptCommand acceptCommand1 = prepareCommand(1, comment);
        AcceptCommand acceptCommand1Copy = prepareCommand(1, comment);
        AcceptCommand acceptCommand2 = prepareCommand(2, comment);
        AcceptCommand acceptCommand3 = prepareCommand(1, "");

        // same object -> returns true
        assertTrue(acceptCommand1.equals(acceptCommand1));

        // same values -> returns true
        assertTrue(acceptCommand1.equals(acceptCommand1Copy));

        // different types -> returns false
        assertFalse(acceptCommand1.equals(1));

        // different job index -> return false
        assertFalse(acceptCommand1.equals(acceptCommand2));

        // different comments -> return false
        assertFalse(acceptCommand1.equals(acceptCommand3));

        // null -> return false
        assertFalse(acceptCommand1.equals(null));
    }

    @Test
    public void execute_acceptWithoutComment_success() throws Exception {
        prepareInputFiles();
        AcceptCommand command = prepareCommand(1, "");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModelWithoutComment.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_acceptWithComment_success() throws Exception {
        prepareInputFiles();
        AcceptCommand command = prepareCommand(1, comment.toString());
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModelWithComment.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_acceptOutOfBounds_failure() throws Exception {
        prepareInputFiles();
        AcceptCommand command = prepareCommand(3, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_INVALID_JOB_INDEX, e.getMessage());
        }
        commandCleanup(command);
    }

    @Test
    public void execute_acceptWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        AcceptCommand command = prepareCommand(1, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns AcceptCommand with {@code jobIndex} and {@code comments}, with default data
     */
    protected AcceptCommand prepareCommand(int jobIndex, String comments) throws Exception {
        JobNumber.initialize(0);
        AcceptCommand command = new AcceptCommand(jobIndex, comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\carvicim\logic\commands\ImportAllCommandTest.java
``` java
public class ImportAllCommandTest extends ImportCommandTestEnv {

    private ModelIgnoreJobDates expectedModel;

    @Before
    public void setup() throws Exception {
        Employee jim = new Employee(new Name("Jim"), new Phone("87654321"), new Email("jim@gmail.com"),
                Collections.emptySet());
        Person client = new Person(new Name("JD"), new Phone("91234567"), new Email("jd@gmail.com"));
        UniqueEmployeeList uniqueEmployeeList = new UniqueEmployeeList();
        uniqueEmployeeList.add(jim);
        RemarkList excelRemarkList = new RemarkList();
        excelRemarkList.add(new Remark("Haha"));
        excelRemarkList.add(new Remark("whew"));
        Job job = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), excelRemarkList);

        Employee maya = new Employee(new Name("Maya"), new Phone("87654321"), new Email("maya@gmail.com"),
                Collections.emptySet());
        Person client2 = new Person(new Name("JS"), new Phone("91234567"), new Email("js@gmail.com"));

        UniqueEmployeeList uniqueEmployeeList2 = new UniqueEmployeeList();
        uniqueEmployeeList2.add(maya);
        RemarkList excelRemarkList2 = new RemarkList();
        excelRemarkList2.add(new Remark("first"));
        excelRemarkList2.add(new Remark("second"));
        excelRemarkList2.add(new Remark("last"));
        Job job2 = new Job(client2, new VehicleNumber("SXX1234X"), new JobNumber("2"), new Date(),
                uniqueEmployeeList2, new Status(Status.STATUS_ONGOING), excelRemarkList2);

        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        jobList.add(job2);
        expectedModel = new ModelIgnoreJobDates();
        expectedModel.addJobsAndNewEmployees(jobList);
    }

    @Test
    public void equals() throws Exception {
        String filePath = "CS2103-testsheet.xlsx";
        String altFilePath = "CS2103-testsheet-corrupt.xlsx";
        ImportAllCommand importCommand1 = prepareCommand(filePath);
        ImportAllCommand importCommand1Copy = prepareCommand(filePath);
        ImportAllCommand importCommand2 = prepareCommand(altFilePath);

        // same object -> returns true
        assertTrue(importCommand1.equals(importCommand1));

        // same values -> returns true
        assertTrue(importCommand1.equals(importCommand1Copy));

        // different types -> returns false
        assertFalse(importCommand1.equals(1));

        // different filepath -> return false
        assertFalse(importCommand1.equals(importCommand2));

        // null -> return false
        assertFalse(importCommand1.equals(null));
    }

    @Test
    public void execute_importValidExcelFile_success() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(ERROR_INPUT_FILE, ERROR_IMPORTED_FILE, ERROR_OUTPUT_FILE);

        ImportAllCommand command = prepareCommand(inputPath);
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
    }

    @Test
    public void execute_importInvalidExcelFile_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(NON_EXCEL_FILE, NON_EXCEL_FILE, NON_EXCEL_OUTPUT_FILE);
        ImportAllCommand command = prepareCommand(inputPath);
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_FILE_FORMAT, e.getMessage());
        }
    }

    /**
     * Returns ImportAllCommand with {@code filePath}, with default data
     */
    protected ImportAllCommand prepareCommand(String filePath) throws Exception {
        JobNumber.initialize(0);
        ImportAllCommand command = new ImportAllCommand(filePath);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
```
###### \java\seedu\carvicim\logic\commands\ImportCommandTest.java
``` java
public class ImportCommandTest extends ImportCommandTestEnv {

    @Test
    public void equals() throws Exception {
        String filePath = "CS2103-testsheet.xlsx";
        String altFilePath = "CS2103-testsheet-corrupt.xlsx";
        ImportCommand importCommand1 = prepareCommand(filePath);
        ImportCommand importCommand1Copy = prepareCommand(filePath);
        ImportCommand importCommand2 = prepareCommand(altFilePath);

        // same object -> returns true
        assertTrue(importCommand1.equals(importCommand1));

        // same values -> returns true
        assertTrue(importCommand1.equals(importCommand1Copy));

        // different types -> returns false
        assertFalse(importCommand1.equals(1));

        // different filepath -> return false
        assertFalse(importCommand1.equals(importCommand2));

        // null -> return false
        assertFalse(importCommand1.equals(null));
    }

    @Test
    public void execute_importValidExcelFile_success() throws Exception {
        Model expectedModel = new ModelManager();
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(ERROR_INPUT_FILE, ERROR_IMPORTED_FILE, ERROR_OUTPUT_FILE);

        ImportCommand command = prepareCommand(inputPath);
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_importInvalidExcelFile_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(NON_EXCEL_FILE, NON_EXCEL_FILE, NON_EXCEL_OUTPUT_FILE);
        ImportCommand command = prepareCommand(inputPath);
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_FILE_FORMAT, e.getMessage());
        }
    }

    /**
     * Returns ImportCommand with {@code filePath}, with default data
     */
    protected ImportCommand prepareCommand(String filePath) throws Exception {
        JobNumber.initialize(1);
        ImportCommand command = new ImportCommand(filePath);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
```
###### \java\seedu\carvicim\logic\commands\RejectAllCommandTest.java
``` java
public class RejectAllCommandTest extends ImportCommandTestEnv {
    private Remark comment;
    private ModelIgnoreJobDates expectedModel;
    @Before
    public void setup() throws Exception {
        comment = new Remark("comment");
        expectedModel = new ModelIgnoreJobDates();
    }

    @Test
    public void equals() throws Exception {
        String comment = "comment";
        RejectAllCommand rejectCommand1 = prepareCommand(comment);
        RejectAllCommand rejectCommandCopy = prepareCommand(comment);
        RejectAllCommand rejectCommand2 = prepareCommand("");

        // same object -> returns true
        assertTrue(rejectCommand1.equals(rejectCommand1));

        // same values -> returns true
        assertTrue(rejectCommand1.equals(rejectCommandCopy));

        // different types -> returns false
        assertFalse(rejectCommand1.equals(1));

        // different comments -> return false
        assertFalse(rejectCommand1.equals(rejectCommand2));

        // null -> return false
        assertFalse(rejectCommand1.equals(null));
    }

    @Test
    public void execute_rejectWithoutComment_success() throws Exception {
        prepareInputFiles();
        RejectAllCommand command = prepareCommand("");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectAllWithComment_success() throws Exception {
        prepareInputFiles();
        RejectAllCommand command = prepareCommand(comment.toString());
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectAllWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        RejectAllCommand command = prepareCommand(comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns RejectCommand with {@code comments}, with default data
     */
    protected RejectAllCommand prepareCommand(String comments) throws Exception {
        JobNumber.initialize(1);
        RejectAllCommand command = new RejectAllCommand(comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\carvicim\logic\commands\RejectCommandTest.java
``` java
public class RejectCommandTest extends ImportCommandTestEnv {
    private Remark comment;
    private ModelIgnoreJobDates expectedModel;
    @Before
    public void setup() throws Exception {
        comment = new Remark("comment");
        expectedModel = new ModelIgnoreJobDates();
    }

    @Test
    public void equals() throws Exception {
        String comment = "comment";
        RejectCommand rejectCommand1 = prepareCommand(1, comment);
        RejectCommand rejectCommandCopy = prepareCommand(1, comment);
        RejectCommand rejectCommand2 = prepareCommand(2, comment);
        RejectCommand rejectCommand3 = prepareCommand(1, "");

        // same object -> returns true
        assertTrue(rejectCommand1.equals(rejectCommand1));

        // same values -> returns true
        assertTrue(rejectCommand1.equals(rejectCommandCopy));

        // different types -> returns false
        assertFalse(rejectCommand1.equals(1));

        // different job index -> return false
        assertFalse(rejectCommand1.equals(rejectCommand2));

        // different comments -> return false
        assertFalse(rejectCommand1.equals(rejectCommand3));

        // null -> return false
        assertFalse(rejectCommand1.equals(null));
    }

    @Test
    public void execute_rejectWithoutComment_success() throws Exception {
        prepareInputFiles();
        RejectCommand command = prepareCommand(1, "");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectWithComment_success() throws Exception {
        prepareInputFiles();
        RejectCommand command = prepareCommand(1, comment.toString());
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_rejectOutOfBounds_failure() throws Exception {
        prepareInputFiles();
        RejectCommand command = prepareCommand(3, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_INVALID_JOB_INDEX, e.getMessage());
        }
        commandCleanup(command);
    }

    @Test
    public void execute_rejectWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        RejectCommand command = prepareCommand(1, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns RejectCommand with {@code jobIndex} and {@code comments}, with default data
     */
    protected RejectCommand prepareCommand(int jobIndex, String comments) throws Exception {
        JobNumber.initialize(1);
        RejectCommand command = new RejectCommand(jobIndex, comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\carvicim\logic\commands\SetCommandTest.java
``` java
public class SetCommandTest {

    @Test
    public void equals() {
        Model model = new ModelManager(getTypicalCarvicim(),  new UserPrefs());
        String word1 = "word1";
        String word2 = "word2";
        SetCommand setCommand1 = prepareCommand(model, AddJobCommand.COMMAND_WORD, word1);
        SetCommand setCommand1Copy = prepareCommand(model, AddJobCommand.COMMAND_WORD, word1);
        SetCommand setCommand2 = prepareCommand(model, AddEmployeeCommand.COMMAND_WORD, word1);
        SetCommand setCommand3 = prepareCommand(model, AddJobCommand.COMMAND_WORD, word2);

        // same object -> returns true
        assertTrue(setCommand1.equals(setCommand1));

        // same values -> returns true
        assertTrue(setCommand1.equals(setCommand1Copy));

        // different types -> returns false
        assertFalse(setCommand1.equals(1));

        // different current word -> return false
        assertFalse(setCommand1.equals(setCommand2));

        // different new word -> return false
        assertFalse(setCommand1.equals(setCommand3));

        // null -> return false
        assertFalse(setCommand1.equals(null));
    }

    @Test
    public void execute_changeAdd_success() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(expectedModel, currentWord, newWord);
        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageDefaultSuccess(), expectedModel);
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
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageDefaultSuccess(), expectedModel);
    }

    @Test
    public void execute_changeAddBackToDefault_success() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(actualModel, currentWord, newWord);
        SetCommand newCommand = prepareCommand(actualModel, newWord, currentWord);
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageRemoveAliasSuccess(), expectedModel);
    }


    @Test
    public void execute_addSetAliasAndRemove_success() throws CommandWordException {
        Model actualModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalCarvicim(), new UserPrefs());
        String currentWord = SetCommand.COMMAND_WORD;
        String newWord = getUnusedCommandWord(actualModel);

        setCommandWord(expectedModel, currentWord, newWord);
        SetCommand newCommand = prepareCommand(actualModel, currentWord, newWord);
        assertCommandSuccess(newCommand, actualModel, newCommand.getMessageDefaultSuccess(), expectedModel);

        setCommandWord(expectedModel, newWord, currentWord);
        SetCommand newCommand2 = prepareCommand(actualModel, newWord, currentWord);
        assertCommandSuccess(newCommand2, actualModel, newCommand2.getMessageRemoveAliasSuccess(), expectedModel);
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
        for (int i = 0; i < actualModel.getCommandWords().getCommands().size(); i++) {
            if (!actualModel.getCommandWords().getCommands().containsValue(newWord)) {
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
        for (int i = 0; i < actualModel.getCommandWords().getCommands().size(); i++) {
            if (!actualModel.getCommandWords().getCommands().containsValue(newWord)
                    && !newWord.equals(otherWord)) {
                return newWord;
            }
            newWord += "a";
        }
        return newWord;
    }

    /**
     * Generates a new {@code SetCommand} which upon execution replaces {@code currentWord} with {@code newWord}.
     */
    private SetCommand prepareCommand(Model model, String currentWord, String newWord) {
        SetCommand command = new SetCommand(currentWord, newWord);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\carvicim\logic\commands\SwitchCommandTest.java
``` java
public class SwitchCommandTest {
    @Test
    public void execute_switch_success() throws CommandException {
        SwitchCommand command = prepareCommand();
        command.execute();
        Model expectedModel = new ModelManager();
        expectedModel.switchJobView();
        assertEquals(expectedModel.isViewingImportedJobs(), command.model.isViewingImportedJobs());
    }

    private SwitchCommand prepareCommand() {
        SwitchCommand command = new SwitchCommand();
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\carvicim\logic\parser\AcceptAllCommandParserTest.java
``` java
public class AcceptAllCommandParserTest {
    private AcceptAllCommandParser parser = new AcceptAllCommandParser();

    @Test
    public void parse_acceptAllWithoutComment_success() {
        assertParseSuccess(parser, "", new AcceptAllCommand(""));
    }

    @Test
    public void parse_acceptAllWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, comment, new AcceptAllCommand(comment));
    }
}
```
###### \java\seedu\carvicim\logic\parser\AcceptCommandParserTest.java
``` java
public class AcceptCommandParserTest {
    private AcceptCommandParser parser = new AcceptCommandParser();

    @Test
    public void parse_acceptWithoutComment_success() {
        assertParseSuccess(parser, " 1", new AcceptCommand(1, ""));
    }

    @Test
    public void parse_acceptWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, " 1 " + comment, new AcceptCommand(1, comment));
    }

    @Test
    public void parse_invalidNumber_failure() {
        assertParseFailure(parser, "", ERROR_MESSAGE);
    }
}
```
###### \java\seedu\carvicim\logic\parser\RejectAllCommandParserTest.java
``` java
public class RejectAllCommandParserTest {
    private RejectAllCommandParser parser = new RejectAllCommandParser();

    @Test
    public void parse_acceptAllWithoutComment_success() {
        assertParseSuccess(parser, "", new RejectAllCommand(""));
    }

    @Test
    public void parse_acceptAllWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, comment, new RejectAllCommand(comment));
    }
}
```
###### \java\seedu\carvicim\logic\parser\RejectCommandParserTest.java
``` java
public class RejectCommandParserTest {
    private RejectCommandParser parser = new RejectCommandParser();

    @Test
    public void parse_rejectWithoutComment_success() {
        assertParseSuccess(parser, " 1", new RejectCommand(1, ""));
    }

    @Test
    public void parse_rejectWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, " 1 " + comment, new RejectCommand(1, comment));
    }

    @Test
    public void parse_invalidNumber_failure() {
        assertParseFailure(parser, "", ERROR_MESSAGE);
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
        assertParseFailure(parser, currentWord, ERROR_MESSAGE);
    }

    @Test
    public void parse_oneCommandWord_failure() {
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        assertParseFailure(parser, currentWord, ERROR_MESSAGE);
    }

    public static String getWord() {
        return "a";
    }
}
```
###### \java\seedu\carvicim\storage\ImportSessionTestEnv.java
``` java

/**
 * Used to test classes that manipulate sessionData in importSession
 */
public abstract class ImportSessionTestEnv {
    protected static final String RESOURCE_PATH = "storage/session/ImportSessionTest/";
    protected static final String ERROR_INPUT_FILE = "CS2103-testsheet.xlsx";
    protected static final String ERROR_RESULT_FILE = "CS2103-testsheet-results.xlsx";
    protected static final String ERROR_OUTPUT_FILE = "CS2103-testsheet-comments.xlsx";
    protected static final String ERROR_IMPORTED_FILE = "CS2103-testsheet-import.xlsx";
    protected static final String MULTIPLE_INPUT_FILE = "CS2103-testsheet-multiple.xlsx";
    protected static final String MULTIPLE_RESULT_FILE = "CS2103-testsheet-multiple-results.xlsx";
    protected static final String MULTIPLE_OUTPUT_FILE = "CS2103-testsheet-multiple-comments.xlsx";
    protected static final String CORRUPT_INPUT_FILE = "CS2103-testsheet-corrupt.xlsx";
    protected static final String CORRUPT_RESULT_FILE = "CS2103-testsheet-corrupt-results.xlsx";
    protected static final String CORRUPT_OUTPUT_FILE = "CS2103-testsheet-corrupt-comments.xlsx";
    protected static final String NON_EXCEL_FILE = "non-excel-file.xlsx";
    protected static final String NON_EXCEL_OUTPUT_FILE = "non-excel-file-comments.xlsx";
    protected static final String MISSING_HEADER_FIELD_FILE = "missing-header-field.xlsx";
    protected static final String MISSING_HEADER_FIELD_OUTPUT_FILE = "missing_header_field-comments.xlsx";
    protected String expectedOutputPath;
    protected String inputPath;
    protected String outputPath;
    protected String resultPath;
    protected String outputFilePath;

    protected File testFile;
    protected File outputFile;
    protected File expectedOutputFile;

    /**
     * Attempts to delete -comments file if found
     */
    protected void cleanup() throws IOException {
        try {
            deleteFile(outputPath);
        } catch (NullPointerException e) {
            ;
        }
    }

    /**
     * asserts output file from importAll of input file is the same as result file
     */
    protected void assertOutputResultFilesEqual() throws Exception {
        assertEquals(expectedOutputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        ImportSession.getInstance().getSessionData().freeResources();
        assertExcelFilesEquals(testFile, outputFile);
    }

    protected void setup(String inputPath, String resultPath, String outputPath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        this.inputPath = classLoader.getResource(RESOURCE_PATH + inputPath).getPath();
        this.resultPath = classLoader.getResource(RESOURCE_PATH + resultPath).getPath();
        this.expectedOutputPath = RESOURCE_PATH + outputPath;
        try {
            this.outputPath = classLoader.getResource(expectedOutputPath).getPath();
            deleteFile(this.outputPath);
        } catch (NullPointerException e) {
            ;
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
        workbook1.close();
        workbook2.close();
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
     * Deletes file at {@code filePath} if file exists
     */
    private void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
```
