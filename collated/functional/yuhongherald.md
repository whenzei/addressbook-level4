# yuhongherald
###### \java\seedu\carvicim\commons\events\ui\DisplayAllJobsEvent.java
``` java
public class DisplayAllJobsEvent extends BaseEvent {


    private final ObservableList<Job> jobList;

    public DisplayAllJobsEvent(ObservableList<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ObservableList<Job> getJobList() {
        return jobList;
    }
}
```
###### \java\seedu\carvicim\commons\events\ui\JobListSwitchEvent.java
``` java
/**
 * Indicates that there is a change in job list.
 */
public class JobListSwitchEvent extends BaseEvent {

    public final String message;

    public JobListSwitchEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\carvicim\logic\commands\AcceptAllCommand.java
``` java

/**
 * Accepts all remaining unreviewed job entries into Servicing Manager with {@code comment}
 */
public class AcceptAllCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "acceptAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Accepts all unreviewed job entries. "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "%d job entries accepted!";

    private final String comment;

    public AcceptAllCommand(String comment) {
        this.comment = comment;
    }

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, entries);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        SessionData sessionData = ImportSession.getInstance().getSessionData();
        if (sessionData.getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException(MESSAGE_NO_JOB_ENTRIES);
        }
        List<Job> jobs = new ArrayList<>(sessionData
                .reviewAllRemainingJobEntries(true, comment));
        model.addJobsAndNewEmployees(jobs);
        if (model.isViewingImportedJobs()) {
            model.switchJobView();
            model.resetJobView();
        }
        return new CommandResult(getMessageSuccess(jobs.size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcceptAllCommand) // instanceof handles nulls
                && comment.equals(((AcceptAllCommand) other).comment);
    }

}
```
###### \java\seedu\carvicim\logic\commands\AcceptCommand.java
``` java

/**
 * Accepts an unreviewed job entry using job number and adds into servicing manager, adding comment into remarksList
 */
public class AcceptCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "accept";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Accepts job entry using index. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d accepted!";

    private final int jobIndex;
    private final String comment;

    public AcceptCommand(int jobIndex, String comment) {
        this.jobIndex = jobIndex;
        this.comment = comment;
    }

    public String getMessageSuccess() {
        return String.format(MESSAGE_SUCCESS, jobIndex);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        SessionData sessionData = ImportSession.getInstance().getSessionData();
        if (sessionData.getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException(MESSAGE_NO_JOB_ENTRIES);
        }
        Job job = sessionData.reviewJobEntryUsingJobIndex(jobIndex, true, comment);
        ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(job);
        model.addJobsAndNewEmployees(jobs);

        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcceptCommand) // instanceof handles nulls
                && jobIndex == ((AcceptCommand) other).jobIndex
                && comment.equals(((AcceptCommand) other).comment);
    }

}
```
###### \java\seedu\carvicim\logic\commands\CommandWords.java
``` java
/**
 * A serializable data structure used to contain the mappings of a command to a word
 */
public class CommandWords implements Serializable {
    public static final String MESSAGE_UNUSED = "%s is not an active command.";
    public static final String MESSAGE_USED = "%s is already used.";
    public static final String MESSAGE_NO_CHANGE = "Old and new command word is the same.";
    public static final String MESSAGE_OVERWRITE_DEFAULT = "%s is a default command.";
    /**
     * Stores a list of COMMANDS by their command word
     */
    public static final String[] COMMANDS = {
        AddEmployeeCommand.COMMAND_WORD,
        AnalyseCommand.COMMAND_WORD,
        ArchiveCommand.COMMAND_WORD,
        ClearCommand.COMMAND_WORD,
        DeleteEmployeeCommand.COMMAND_WORD,
        ExitCommand.COMMAND_WORD,
        FindEmployeeCommand.COMMAND_WORD,
        HelpCommand.COMMAND_WORD,
        HistoryCommand.COMMAND_WORD,
        ListEmployeeCommand.COMMAND_WORD,
        RedoCommand.COMMAND_WORD,
        AddJobCommand.COMMAND_WORD,
        SelectEmployeeCommand.COMMAND_WORD,
        SetCommand.COMMAND_WORD,
        UndoCommand.COMMAND_WORD,
        ThemeCommand.COMMAND_WORD,
        SortCommand.COMMAND_WORD,
        ImportCommand.COMMAND_WORD,
        ListJobCommand.COMMAND_WORD,
        SwitchCommand.COMMAND_WORD,
        AcceptAllCommand.COMMAND_WORD,
        RejectAllCommand.COMMAND_WORD,
        RejectCommand.COMMAND_WORD,
        AcceptCommand.COMMAND_WORD,
        ImportAllCommand.COMMAND_WORD
    };

    private final HashMap<String, String> commands;
    /**
     * Creates a data structure to maintain used command words.
     */
    public CommandWords() {
        commands = new HashMap<>();
        for (String command : COMMANDS) {
            commands.put(command, command);
        }
    }

    public CommandWords(CommandWords commandWords) {
        requireNonNull(commandWords);
        commands = new HashMap<>();
        commands.putAll(commandWords.commands);
    }

    /**
     * Returns whether {@code commandWord} is in {@code COMMANDS}
     */
    public static boolean isDefaultCommandWord(String commandWord) {
        for (String command: COMMANDS) {
            if (command.equals(commandWord)) {
                return true;
            }
        }
        return false;
    }

    public static String getMessageUnused(String commandWord) {
        return String.format(MESSAGE_UNUSED, commandWord);
    }

    public static String getMessageOverwriteDefault(String commandWord) {
        return String.format(MESSAGE_OVERWRITE_DEFAULT, commandWord);
    }

    public static String getMessageUsed(String commandWord) {
        return String.format(MESSAGE_USED, commandWord);
    }

    public static String getMessageNoChange() {
        return MESSAGE_NO_CHANGE;
    }

    public HashMap<String, String> getCommands() {
        return new HashMap<>(commands);
    }

    /**
     * Retrieves a command word using a key
     * @param key
     * @return command
     * @throws CommandWordException
     */
    public String getCommandWord(String key) throws CommandWordException {
        String commandWord = commands.get(key);
        if (commandWord == null) {
            throw new CommandWordException(getMessageUnused(key));
        }
        return commandWord;
    }

    /**
     * Retrieves a command key using word
     * @param value
     * @return command
     * @throws CommandWordException
     */
    public String getCommandKey(String value) throws CommandWordException {
        Iterator<Map.Entry<String, String>> commandList = commands.entrySet().iterator();
        Map.Entry<String, String> currentCommand;
        while (commandList.hasNext()) {
            currentCommand = commandList.next();
            if (currentCommand.getValue().equals(value)) {
                return currentCommand.getKey();
            }
        }
        throw new CommandWordException(getMessageUnused(value));
    }

    /**
     * Sets currentWord to newWord
     * @param currentWord Active command word to be replaced
     * @param newWord Command word to be replaced with
     * @throws CommandWordException currentWord is not valid
     */
    public void setCommandWord(String currentWord, String newWord) throws CommandWordException {
        requireNonNull(currentWord, newWord);
        throwCommanwWordExceptionIfWordsNotValid(currentWord, newWord);
        if (isDefaultCommandWord(currentWord)) {
            commands.remove(currentWord);
            commands.put(currentWord, newWord);
            return;
        }
        Iterator<Map.Entry<String, String>> commandList = commands.entrySet().iterator();
        Map.Entry<String, String> currentCommand;
        while (commandList.hasNext()) {
            currentCommand = commandList.next();
            if (currentCommand.getValue().equals(currentWord)) {
                commands.remove(currentCommand.getKey());
                commands.put(currentCommand.getKey(), newWord);
                return;
            }
        }
        throw new CommandWordException(getMessageUnused(currentWord));
    }

    /**
     * throws a {@code CommandWordException} if:
     * 1. Both words are the same
     * 2. {@code newWord} overwrites the default word for another command
     * 3. {@code newWord} is already in use
     */
    private void throwCommanwWordExceptionIfWordsNotValid(String currentWord, String newWord) throws
            CommandWordException {
        if (currentWord.equals(newWord)) {
            throw new CommandWordException(getMessageNoChange());
        }
        if (isDefaultCommandWord(newWord)
                && !commands.get(newWord).equals(currentWord)) {
            throw new CommandWordException(getMessageOverwriteDefault(newWord));
        }
        if (commands.containsValue(newWord)) {
            throw new CommandWordException(getMessageUsed(newWord));
        }
    }

    /**
     * Copies key and value of {@code command} from {@code commands}
     * to {@code verifiedCommands}. Creates a new entry with default
     * key = value if missing.
     */
    private void moveVerifiedWord(String command, HashMap<String, String> verifiedCommands) {
        verifiedCommands.put(command, commands.getOrDefault(command, command));
    }

    /**
     * Checks if hashmap contains invalid command keys and adds any missing
     * command keys
     */
    public void checkIntegrity() {
        HashMap<String, String> verifiedCommands = new HashMap<>();
        for (String command : COMMANDS) {
            moveVerifiedWord(command, verifiedCommands);
        }
        commands.clear();
        commands.putAll(verifiedCommands);
    }


    /**
     * Resets the existing data of this {@code CommandWords} with {@code newCommandWords}.
     */
    public void resetData(CommandWords newCommandWords) {
        requireNonNull(newCommandWords);
        commands.clear();
        commands.putAll(newCommandWords.commands);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof CommandWords)) {
            return false;
        }

        // state check
        CommandWords other = (CommandWords) obj;
        for (String commandKey : commands.keySet()) {
            if (!commands.get(commandKey).equals(other.commands.get(commandKey))) {
                return false;
            }
        }
        return commands.size() == other.commands.size();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Default commands : custom word \n");
        Iterator<Map.Entry<String, String>> commandList = commands.entrySet().iterator();
        Map.Entry<String, String> currentCommand;
        ArrayList<String> lines = new ArrayList<>();
        while (commandList.hasNext()) {
            currentCommand = commandList.next();
            lines.add(currentCommand.getKey() + ":" + currentCommand.getValue() + "\n");
        }
        Collections.sort(lines);
        for (int i = 0; i < lines.size(); i++) {
            builder.append(lines.get(i));
        }
        return builder.toString();
    }

}
```
###### \java\seedu\carvicim\logic\commands\exceptions\CommandWordException.java
``` java
/**
 * Represents an error which occurs during execution of {@link seedu.carvicim.logic.commands.SetCommand}.
 */
public class CommandWordException extends Exception {
    public CommandWordException(String message) {
        super(message);
    }
}
```
###### \java\seedu\carvicim\logic\commands\ImportAllCommand.java
``` java
/**
 * Attempts to import all {@code JobEntry} into Servicing Manager
 */
public class ImportAllCommand extends Command {

    public static final String COMMAND_WORD = "importAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports job entries from from an excel file. "
            + "Parameters: FILEPATH\n"
            + "Example: " + COMMAND_WORD + " yourfile.xls";

    public static final String MESSAGE_SUCCESS = "%s has been imported, with %d job entries!";

    private final String filePath;

    public ImportAllCommand(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, filePath, entries);
    }

    @Override
    public CommandResult execute() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        try {
            importSession.initializeSession(filePath);
        } catch (FileAccessException | FileFormatException | InvalidDataException e) {
            throw new CommandException(e.getMessage());
        }
        List<Job> jobs = new ArrayList<>(importSession.getSessionData()
                .reviewAllRemainingJobEntries(true, ""));
        model.addJobsAndNewEmployees(jobs);
        importSession.closeSession();
        return new CommandResult(getMessageSuccess(jobs.size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportAllCommand // instanceof handles nulls
                && filePath.equals(((ImportAllCommand) other).filePath));
    }

}
```
###### \java\seedu\carvicim\logic\commands\ImportCommand.java
``` java

/**
 * Attempts to import specified file into Servicing Manager
 */
public class ImportCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports an excel file for reviewing. "
            + "Parameters: FILEPATH\n"
            + "Example: " + COMMAND_WORD + " yourfile.xls";

    public static final String MESSAGE_SUCCESS = "%s has been imported, with %d job entries!";

    private final String filePath;

    public ImportCommand(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, filePath, entries);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        try {
            importSession.initializeSession(filePath);
        } catch (FileAccessException | FileFormatException | InvalidDataException e) {
            throw new CommandException(e.getMessage());
        }

        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess(importSession.getSessionData()
                .getUnreviewedJobEntries().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && filePath.equals(((ImportCommand) other).filePath));
    }

}
```
###### \java\seedu\carvicim\logic\commands\ListJobCommand.java
``` java
public class ListJobCommand extends Command {

    public static final String COMMAND_WORD = "listj";

    public static final String MESSAGE_SUCCESS = "Listed all jobs";


    @Override
    public CommandResult execute() {
        model.resetJobView();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\carvicim\logic\commands\RejectAllCommand.java
``` java

/**
 * Rejects all remaining unreviewed job entries into Servicing Manager with {@code comment}
 */
public class RejectAllCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "rejectAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Rejects all unreviewed job entries. "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "%d job entries rejected!";

    private final String comment;

    public RejectAllCommand(String comment) {
        this.comment = comment;
    }

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, entries);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        SessionData sessionData = ImportSession.getInstance().getSessionData();
        if (sessionData.getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException(MESSAGE_NO_JOB_ENTRIES);
        }
        List<Job> jobs = new ArrayList<>(sessionData.reviewAllRemainingJobEntries(false, comment));
        if (model.isViewingImportedJobs()) {
            model.switchJobView();
            model.resetJobView();
        }
        return new CommandResult(getMessageSuccess(jobs.size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RejectAllCommand) // instanceof handles nulls
                && comment.equals(((RejectAllCommand) other).comment);
    }

}
```
###### \java\seedu\carvicim\logic\commands\RejectCommand.java
``` java

/**
 * Rejects an unreviewed job entry using job number, adding comment into remarksList
 */
public class RejectCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "reject";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Rejects job entry using job index. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d rejected!";

    private final int jobIndex;
    private final String comment;

    public RejectCommand(int jobIndex, String comment) {
        this.jobIndex = jobIndex;
        this.comment = comment;
    }

    public String getMessageSuccess() {
        return String.format(MESSAGE_SUCCESS, jobIndex);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        SessionData sessionData = ImportSession.getInstance().getSessionData();
        if (sessionData.getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException(MESSAGE_NO_JOB_ENTRIES);
        }
        Job job = sessionData.reviewJobEntryUsingJobIndex(jobIndex, false, comment);

        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RejectCommand) // instanceof handles nulls
                && jobIndex == ((RejectCommand) other).jobIndex
                && comment.equals(((RejectCommand) other).comment);
    }

}
```
###### \java\seedu\carvicim\logic\commands\SetCommand.java
``` java
/**
 * Sets a command word to user preferred command word
 */
public class SetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "set";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a command word to user preference. "
            + "Parameters: CURRENT_COMMAND_WORD NEW_COMMAND_WORD\n"
            + "Example: " + COMMAND_WORD + " set st";

    public static final String MESSAGE_ALIAS_SUCCESS = "%s has been replaced with %s!";
    public static final String MESSAGE_DEFAULT_SUCCESS = "%s has been set as an alternative for %s!";
    public static final String MESSAGE_REMOVE_ALIAS_SUCCESS = "%s has been removed!";

    private final String currentWord;
    private final String newWord;

    /**
     * Creates an SetCommand to set the specified {@code CommandWords}
     */
    public SetCommand(String currentWord, String newWord) {
        this.currentWord = currentWord;
        this.newWord = newWord;
    }

    public String getMessageAliasSuccess() {
        return String.format(MESSAGE_ALIAS_SUCCESS, currentWord, newWord);
    }

    public String getMessageRemoveAliasSuccess() {
        return String.format(MESSAGE_REMOVE_ALIAS_SUCCESS, currentWord);
    }

    public String getMessageDefaultSuccess() {
        return String.format(MESSAGE_DEFAULT_SUCCESS, newWord, currentWord);
    }

    public String getMessageUsed() {
        return CommandWords.getMessageUsed(newWord);
    }

    public String getMessageUnused() {
        return CommandWords.getMessageUnused(currentWord);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.getCommandWords().setCommandWord(currentWord, newWord);
        } catch (CommandWordException e) {
            throw new CommandException(e.getMessage());
        }
        if (CommandWords.isDefaultCommandWord(currentWord)) {
            return new CommandResult(getMessageDefaultSuccess());
        } else if (CommandWords.isDefaultCommandWord(newWord)) {
            return new CommandResult(getMessageRemoveAliasSuccess());
        }
        return new CommandResult(getMessageAliasSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetCommand // instanceof handles nulls
                && currentWord.equals(((SetCommand) other).currentWord)
                && newWord.equals(((SetCommand) other).newWord));
    }
}
```
###### \java\seedu\carvicim\logic\commands\SwitchCommand.java
``` java
public class SwitchCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "switch";

    public static final String MESSAGE_SUCCESS = "Switched job lists.";


    @Override
    public CommandResult executeUndoableCommand() {
        model.switchJobView();
        model.resetJobView();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\carvicim\logic\parser\AcceptAllCommandParser.java
``` java

/**
 * Parses input arguments and creates a new AcceptAllCommand object
 */
public class AcceptAllCommandParser implements Parser<AcceptAllCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the AcceptAllCommand
     * and returns an AcceptAllCommand object for execution.
     */
    public AcceptAllCommand parse(String args) {
        String comment = args.trim();
        if (!comment.equals("")) {
            checkArgument(isValidRemark(comment), MESSAGE_REMARKS_CONSTRAINTS);
        }
        return new AcceptAllCommand(comment);
    }

}
```
###### \java\seedu\carvicim\logic\parser\AcceptCommandParser.java
``` java

/**
 * Parses input arguments and creates a new AcceptCommand object
 */
public class AcceptCommandParser implements Parser<AcceptCommand> {

    public static final int NUMBER_OF_ARGUMENTS = 2;
    public static final String SPACE = " ";
    public static final int COMMENTS_INDEX = 1;
    public static final String ERROR_MESSAGE = MESSAGE_INVALID_JOB_INDEX + "\n" + AcceptCommand.MESSAGE_USAGE;

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the AcceptCommand
     * and returns an AcceptCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AcceptCommand parse(String args) throws ParseException {
        String[] arguments = args.trim().split(SPACE, NUMBER_OF_ARGUMENTS);
        String comment = "";
        if (arguments.length == NUMBER_OF_ARGUMENTS) {
            comment = arguments[COMMENTS_INDEX].trim();
            checkArgument(isValidRemark(comment), MESSAGE_REMARKS_CONSTRAINTS);
        }
        try {
            int jobNumber = parseInteger(arguments[0]);
            return new AcceptCommand(jobNumber, comment);
        } catch (IllegalValueException ive) {
            throw new ParseException(ERROR_MESSAGE);
        }
    }

}
```
###### \java\seedu\carvicim\logic\parser\ImportAllCommandParser.java
``` java

/**
 * Parses input arguments and creates a new ImporatAllCommand object
 */
public class ImportAllCommandParser implements Parser<ImportAllCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the ImportAllCommand
     * and returns an ImportAllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportAllCommand parse(String args) throws ParseException {
        try {
            String filePath = parseFilename(args);
            return new ImportAllCommand(filePath);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage() + "\n" + ImportCommand.MESSAGE_USAGE);
        }
    }

}
```
###### \java\seedu\carvicim\logic\parser\ImportCommandParser.java
``` java

/**
 * Parses input arguments and creates a new ImporatAllCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the ImportAllCommand
     * and returns an ImportAllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportCommand parse(String args) throws ParseException {
        try {
            String filePath = parseFilename(args);
            return new ImportCommand(filePath);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage() + "\n" + ImportCommand.MESSAGE_USAGE);
        }
    }

}
```
###### \java\seedu\carvicim\logic\parser\RejectAllCommandParser.java
``` java

/**
 * Parses input arguments and creates a new RejectAllCommand object
 */
public class RejectAllCommandParser implements Parser<RejectAllCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the RejectAllCommand
     * and returns a RejectAllCommand object for execution.
     */
    public RejectAllCommand parse(String args) {
        String comment = args.trim();
        if (!comment.equals("")) {
            checkArgument(isValidRemark(comment), MESSAGE_REMARKS_CONSTRAINTS);
        }
        return new RejectAllCommand(comment);
    }

}
```
###### \java\seedu\carvicim\logic\parser\RejectCommandParser.java
``` java

/**
 * Parses input arguments and creates a new RejectCommand object
 */
public class RejectCommandParser implements Parser<RejectCommand> {

    public static final String SPACE = " ";
    public static final int NUMBER_OF_ARGUMENTS = 2;
    public static final int COMMENTS_INDEX = 1;
    public static final int JOB_INDEX_INDEX = 0;
    public static final String ERROR_MESSAGE = MESSAGE_INVALID_JOB_INDEX + "\n" + AcceptCommand.MESSAGE_USAGE;

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the RejectCommand
     * and returns a RejectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RejectCommand parse(String args) throws ParseException {
        String[] arguments = args.trim().split(SPACE, NUMBER_OF_ARGUMENTS);
        String comment = "";
        if (arguments.length == NUMBER_OF_ARGUMENTS) {
            comment = arguments[COMMENTS_INDEX].trim();
            checkArgument(isValidRemark(comment), MESSAGE_REMARKS_CONSTRAINTS);
        }
        try {
            int jobNumber = parseInteger(arguments[JOB_INDEX_INDEX]);
            return new RejectCommand(jobNumber, comment);
        } catch (IllegalValueException ive) {
            throw new ParseException(ERROR_MESSAGE);
        }
    }

}
```
###### \java\seedu\carvicim\logic\parser\SetCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddEmployeeCommand object
 */
public class SetCommandParser implements Parser<SetCommand> {

    public static final String ERROR_MESSAGE = MESSAGE_INSUFFICIENT_WORDS + "\n" + SetCommand.MESSAGE_USAGE;

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the SetCommand
     * and returns a SetCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetCommand parse(String args) throws ParseException {
        try {
            String[] commandWords = parseWords(args);
            return new SetCommand(commandWords[0], commandWords[1]);
        } catch (IllegalValueException ive) {
            throw new ParseException(ERROR_MESSAGE);
        }
    }

}
```
###### \java\seedu\carvicim\model\job\JobList.java
``` java
    /**
     * Filters {@code jobList} for jobs assigned to {@code employee}.
     */
    public static Predicate<Job> filterByEmployee(ObservableList<Job> jobList, Employee employee) {
        Predicate<Job> predicate = new Predicate<Job>() {
            @Override
            public boolean test(Job job) {
                return job.getAssignedEmployees().contains(employee);
            }
        };
        return predicate;
    }

```
###### \java\seedu\carvicim\model\ModelManager.java
``` java
    @Override
    public boolean isViewingImportedJobs() {
        return isViewingImportedJobs;
    }

    @Override
    public void switchJobView() {
        isViewingImportedJobs = !isViewingImportedJobs;
        if (isViewingImportedJobs) {
            EventsCenter.getInstance().post(
                    new JobListSwitchEvent(JobListIndicator.IMPORTED));
        } else {
            EventsCenter.getInstance().post(
                    new JobListSwitchEvent(JobListIndicator.SAVED));
        }
    }

    @Override
    public void resetJobView() {
        ObservableList<Job> jobList;
        if (isViewingImportedJobs) {
            jobList = FXCollections.observableList(
                    ImportSession.getInstance().getSessionData().getUnreviewedJobEntries());
        } else {
            updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
            jobList = getFilteredJobList();
        }
        EventsCenter.getInstance().post(
                new DisplayAllJobsEvent(FXCollections.unmodifiableObservableList(jobList)));
    }

    @Override
    public void showOngoingJobs() {
        updateFilteredJobList(PREDICATE_SHOW_ONGOING_JOBS);
        EventsCenter.getInstance().post(
                new DisplayAllJobsEvent(FXCollections.unmodifiableObservableList(getFilteredJobList())));
    }

    @Override
    public void resetJobDisplayPanel() {
        EventsCenter.getInstance().post(new JobDisplayPanelResetRequestEvent());
    }

```
###### \java\seedu\carvicim\model\ModelManager.java
``` java
    @Override
    public void addJobsAndNewEmployees(List<Job> jobs) {
        requireNonNull(jobs);
        for (Job job : jobs) {
            addMissingEmployees(job.getAssignedEmployeesAsSet());
            addJob(job);
        }
    }

    @Override
    public void addMissingEmployees(Set<Employee> employees) {
        Iterator<Employee> employeeIterator = employees.iterator();
        while (employeeIterator.hasNext()) {
            try {
                addPerson(employeeIterator.next());
            } catch (DuplicateEmployeeException e) {
                // discard the result
            }
        }
    }

```
###### \java\seedu\carvicim\storage\session\exceptions\DataIndexOutOfBoundsException.java
``` java
/**
 * Represents an error which occurs when trying to access data out of specified range.
 */
public class DataIndexOutOfBoundsException extends Exception {
    public static final String ERROR_MESSAGE = "Rows expected index %d to %d, but got %d";

    public DataIndexOutOfBoundsException(int lower, int upper, int actual) {
        super(String.format(ERROR_MESSAGE, lower, upper, actual));
    }
}
```
###### \java\seedu\carvicim\storage\session\exceptions\FileAccessException.java
``` java
/**
 * Represents an error from attempting to read an excel file in {@link seedu.carvicim.storage.session.ImportSession}.
 */
public class FileAccessException extends Exception {
    public FileAccessException(String message) {
        super(message);
    }
}
```
###### \java\seedu\carvicim\storage\session\exceptions\FileFormatException.java
``` java
/**
 * Represents an error from attempting to read an excel file in {@link seedu.carvicim.storage.session.ImportSession}.
 */
public class FileFormatException extends Exception {
    public FileFormatException(String message) {
        super(message);
    }
}
```
###### \java\seedu\carvicim\storage\session\exceptions\InvalidDataException.java
``` java
/**
 * Represents an error when data supplied to {@link seedu.carvicim.storage.session.SessionData} is in wrong format.
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
```
###### \java\seedu\carvicim\storage\session\exceptions\UninitializedException.java
``` java
/**
 * Represents an error when {@link seedu.carvicim.storage.session.SessionData} is not initialized.
 */
public class UninitializedException extends Exception {
    public UninitializedException(String message) {
        super(message);
    }
}
```
###### \java\seedu\carvicim\storage\session\ImportSession.java
``` java
/**
 * Used to store data relevant to importing of {@code Job} from {@code inFile} and
 * exporting {@code Job} with commens to {@code outFile}. Implements a Singleton design pattern.
 */
public class ImportSession {

    public static final String ERROR_MESSAGE_EXPORT =
            "Unable to export file. Please close the application and try again.";
    public static final String CURRENT_DIRECTORY = ".";
    public static final String TEMP_SUFFIX = ".temp";
    private static final Logger logger = LogsCenter.getLogger(ImportSession.class);

    private static ImportSession importSession;

    private SessionData sessionData;

    private ImportSession() {
        sessionData = new SessionData();
    }

    public static ImportSession getInstance() {
        if (importSession == null) {
            logger.info("New ImportSession instance initialized");
            importSession = new ImportSession();
        }
        return importSession;
    }

    /**
     * Attempts to clean all the temp files in working directory. Does not clean all the .temp files, just
     * keeps their total size constant after close.
     */
    public static void cleanCache() {
        File folder = new File(CURRENT_DIRECTORY);
        File[] files = folder.listFiles();
        boolean success;
        logger.info("Beginning importSession cleanup:");
        for (File file : files) {
            success = true;
            if (file.getName().endsWith(TEMP_SUFFIX)) {
                success = file.delete();
            }
            if (!success) {
                logger.warning(String.format("File %s cannot be deleted", file.getAbsolutePath()));
            }
        }
    }

    public void setSessionData(SessionData sessionData) throws CommandException {
        logger.info("Attempting to set sessionData:");
        requireNonNull(sessionData);
        try {
            this.sessionData.closeWorkBook();
            sessionData.loadTempWorkBook();
            this.sessionData = sessionData;
        } catch (FileAccessException | FileFormatException | InvalidDataException e) {
            logger.warning("Failed to set session data: " + e.getMessage());
            throw new CommandException(e.getMessage());
        }
    }

    /**
     *  Opens excel file specified by {@code filepath} and initializes {@code SessionData} to support import operations
     */
    public void initializeSession(String filePath) throws FileAccessException, FileFormatException,
            InvalidDataException {
        sessionData.loadFile(filePath);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    /**
     * Flushes feedback to {@code pathToOutfile} and releases resources.
     */
    public String closeSession() throws CommandException {
        logger.info("Attempting to close session:");
        try {
            return sessionData.saveDataToSaveFile();
        } catch (IOException e) {
            logger.warning("IOException occurred while closing session.");
            throw new CommandException(ERROR_MESSAGE_EXPORT);
        } catch (UninitializedException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
```
###### \java\seedu\carvicim\storage\session\JobEntry.java
``` java
/**
 * Represents a job entry in an {@link ImportSession}
 */
public class JobEntry extends Job {
    public static final String NEWLINE = "\n";

    private final int sheetNumber;
    private final int rowNumber;

    private final ArrayList<String> comments;

    public JobEntry (Person client, VehicleNumber vehicleNumber, JobNumber jobNumber, Date date,
                     UniqueEmployeeList assignedEmployees, Status status, RemarkList remarks,
                     int sheetNumber, int rowNumber, String importComment) {
        super(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
        this.sheetNumber = sheetNumber;
        this.rowNumber = rowNumber;
        comments = new ArrayList<>();
        if (importComment != null &&  !importComment.isEmpty()) {
            comments.add(importComment);
            remarks.add(new Remark(importComment));
        }
    }

    /**
     * Marks {@code JobEntry} as reviewed.
     * @param approved whether {@code JobEntry} is going to be added to Carvicim
     * @param comment feedback for {@code JobEntry} in String representation
     */
    public void review(boolean approved, String comment) {
        comments.add(comment);
    }

    /**
     * Removes last comment from comments and remarks in the event that it cannot be saved to disk
     */
    public void unreviewLastComment() {
        if (comments.isEmpty()) {
            return;
        }
        comments.remove(comments.size() - 1);
    }

    /**
     * Adds all comments into remarks
     */
    public void confirmLastReview() {
        if (comments.isEmpty()) {
            return;
        }
        if (!comments.get(comments.size() - 1).isEmpty()) {
            remarks.add(new Remark(comments.get(comments.size() - 1)));
        }
    }

    public List<String> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public String getCommentsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String comment : comments) {
            stringBuilder.append(comment);
            stringBuilder.append(NEWLINE);
        }
        return stringBuilder.toString();
    }

    public int getSheetNumber() {
        return sheetNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
```
###### \java\seedu\carvicim\storage\session\RowData.java
``` java
/**
 * Represents a field that spans one or more columns
 */
public class RowData {
    private final int startIndex;
    private final int endIndex;

    public RowData(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Reads all the entries between {@code startIndex} and {@code endIndex} from a row in the excel file
     */
    public ArrayList<String> readDataFromSheet(Sheet sheet, int rowNumber)
            throws DataIndexOutOfBoundsException {
        if (rowNumber < sheet.getFirstRowNum() || rowNumber > sheet.getLastRowNum()) {
            throw new DataIndexOutOfBoundsException(sheet.getFirstRowNum(), sheet.getLastRowNum(), rowNumber);
        }
        Row row = sheet.getRow(rowNumber);
        ArrayList<String> data = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        for (int i = startIndex; i <= endIndex; i++) {
            data.add(dataFormatter.formatCellValue(row.getCell(i)));
        }
        return data;
    }
}
```
###### \java\seedu\carvicim\storage\session\SessionData.java
``` java
/**
 * A data structure used to keep track of job entries in an {@code ImportSession}
 */
public class SessionData {
    public static final String ERROR_MESSAGE_INVALID_FILEPATH = "Please check the path to your file.";
    public static final String ERROR_MESSAGE_READ_PERMISSION = "Please enable file read permission.";
    public static final String ERROR_MESSAGE_FILE_FORMAT = "Unable to read the format of file. "
            + "Please ensure the file is in .xls or .xlsx format";
    public static final String ERROR_MESSAGE_IO_EXCEPTION = "Unable to read file. Please close the file and try again.";
    public static final String ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST = "There are no unreviewed job entries left!";

    public static final String FILE_PATH_CHARACTER = "/";
    public static final String SAVEFILE_SUFFIX = "-comments";
    public static final String ERROR_MESSAGE_UNINITIALIZED = "There is no imported file to save!";
    public static final String XLS_SUFFIX = ".xls";
    public static final String XLSX_SUFFIX = ".xlsx";
    public static final String TEMPFILE_NAME = "comments.temp";
    public static final String TEMPWORKBOOKFILE_NAME = "workbook.temp";
    public static final String ERROR_MESSAGE_INVALID_JOB_INDEX = "Job index not found!";
    public static final String TIMESTAMP_REGEX = "yyyy.MM.dd.HH.mm.ss.SSS";
    public static final String CURRENT_DIRECTORY = ".";

    private static final Logger logger = LogsCenter.getLogger(SessionData.class);

    private final ArrayList<JobEntry> unreviewedJobEntries;
    private final ArrayList<JobEntry> reviewedJobEntries;
    private final ArrayList<SheetWithHeaderFields> sheets;

    private File importFile;
    private Workbook workbook; // write comments to column after last row, with approval status
    private File tempFile;
    private File saveFile;
    private File tempWorkbookFile;

    public SessionData() {
        unreviewedJobEntries = new ArrayList<>();
        reviewedJobEntries = new ArrayList<>();
        sheets = new ArrayList<>();
    }

    public SessionData(File importFile, File tempFile, File saveFile) {
        this.unreviewedJobEntries = new ArrayList<>();
        this.reviewedJobEntries = new ArrayList<>();
        this.sheets = new ArrayList<>();
        this.importFile = importFile;
        this.tempFile = tempFile;
        this.saveFile = saveFile;
        // workbook and sheets have to be rewritten to file on undo
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(TIMESTAMP_REGEX).format(new Date());
    }

    /**
     * Creates a copy of sessionData and returns it
     */
    public SessionData createCopy() throws CommandException {
        SessionData other;
        tempFile = new File(CURRENT_DIRECTORY + getTimeStamp() + TEMPFILE_NAME);
        try {
            logger.info("Attempting to save file: " + tempFile.getAbsolutePath());
            saveDataToFile(tempFile);
        } catch (IOException e) {
            logger.warning("IOException occured while saving file");
            throw new CommandException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (UninitializedException e) {
            tempFile = null; // no data to save
        }
        other = new SessionData(importFile, tempFile, saveFile);
        return other;
    }

    public boolean isInitialized() {
        return (workbook != null);
    }

    /*===================================================================
    savefile methods
    ===================================================================*/

    /**
     * Creates a file using relative filePath of {@code importFile}, then appending a SAVEFILE_SUFFIX
     */
    private File generateSaveFile() {
        requireNonNull(importFile);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(importFile.getParent());
        stringBuilder.append(FILE_PATH_CHARACTER);
        String fullFillName = importFile.getName();
        String fileName;
        String suffix = "";
        if (fullFillName.endsWith(XLS_SUFFIX)) {
            suffix = XLS_SUFFIX;
        } else if (fullFillName.endsWith(XLSX_SUFFIX)) {
            suffix = XLSX_SUFFIX;
        }
        fileName = fullFillName.substring(0, fullFillName.length() - suffix.length());

        stringBuilder.append(fileName);
        stringBuilder.append(SAVEFILE_SUFFIX);
        stringBuilder.append(suffix);
        return new File(stringBuilder.toString());
    }

    /*===================================================================
    Initialization methods
    ===================================================================*/

    /**
     * Attempts load file specified at {@code filePath} if there is no currently open file and
     * specified file exists, is readable and is an excel file
     */
    public void loadFile(String filePath) throws FileAccessException, FileFormatException, InvalidDataException {
        if (isInitialized()) {
            // this check has been removed, can import to overwrite current import session
        }
        logger.info("Attempting to load file: " + filePath);
        freeResources(); // from previous session
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileAccessException(ERROR_MESSAGE_INVALID_FILEPATH);
        } else if (!file.canRead()) {
            logger.warning("Cannot read file: " + filePath);
            throw new FileAccessException(ERROR_MESSAGE_READ_PERMISSION);
        }
        importFile = file;

        try {
            setWorkBook(file);
        } catch (InvalidFormatException | IllegalArgumentException e) {
            throw new FileFormatException(ERROR_MESSAGE_FILE_FORMAT);
        } catch (IOException e) {
            throw new FileFormatException(ERROR_MESSAGE_IO_EXCEPTION);
        }
        initializeSessionData();
    }

    /**
     * Loads a workbook from a previous snapshot
     */
    public void loadTempWorkBook() throws FileAccessException, FileFormatException, InvalidDataException {
        if (tempFile == null) {
            return;
        }
        logger.info("Loading temp workbook from: " + tempFile.getAbsolutePath());
        if (saveFile.exists() && !saveFile.delete()) {
            logger.warning("Unable to overwrite save file: " + saveFile.getAbsolutePath());
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        }
        try {
            setWorkBook(tempFile);
        } catch (IOException e) {
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (InvalidFormatException | IllegalArgumentException e) {
            throw new FileFormatException(ERROR_MESSAGE_FILE_FORMAT);
        } finally {
            tempFile.delete();
            tempFile = null;
        }
        initializeSessionData();
    }

    /**
     * Attempts to create and set {@code Workbook} for a given {@code File}
     */
    private void setWorkBook(File file) throws IOException, InvalidFormatException, IllegalArgumentException {
        logger.info("Setting workbook from file: " + file);
        requireNonNull(file);
        saveFile = generateSaveFile();
        if (saveFile.exists()) {
            logger.info("Save file detected: " + saveFile);
            try {
                workbook = WorkbookFactory.create(saveFile);
            } catch (EmptyFileException e) {
                logger.warning("Failed to load save file: " + saveFile);
                saveFile.delete();
                saveFile = null;
                setWorkBook(file);
            }
        } else {
            workbook = WorkbookFactory.create(file);
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook = WorkbookFactory.create(saveFile);
        }
    }

    /**
     * Attempts to parse the column headers and retrieve job entries
     */
    public void initializeSessionData() throws InvalidDataException {
        SheetWithHeaderFields sheetWithHeaderFields;
        SheetParser sheetParser;
        Sheet sheet;
        logger.info("Setting session data");
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(workbook.getFirstVisibleTab() + i);
            sheetParser = new SheetParser(sheet);
            try {
                sheetWithHeaderFields = sheetParser.parseSheetWithHeaderField();
                addSheet(sheetWithHeaderFields);
            } catch (FileFormatException e) {
                logger.warning("Suppressed error while importing sheet " + i + " : " + e.getMessage());
                sheets.add(null);
            }
        }
    }

    public String saveDataToSaveFile() throws IOException, UninitializedException {
        return saveDataToFile(saveFile);
    }

    /**
     * Saves feedback to {@code file}
     */
    public String saveDataToFile(File file) throws IOException, UninitializedException {
        requireNonNull(file);
        if (!isInitialized()) {
            throw new UninitializedException(ERROR_MESSAGE_UNINITIALIZED);
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        String path = file.getAbsolutePath();
        workbook.write(fileOut);
        fileOut.close();
        return path;
    }

    /**
     * Releases resources associated with ImportSession by nulling field
     */
    public void freeResources() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                logger.warning("Failed to close workbook"); // can't close it, but application is already closing
            }
        }
        workbook = null;
        importFile = null;
        if (tempFile != null && !tempFile.delete()) {
            logger.warning("Failed to delete tempFile " + tempFile.getAbsolutePath());
        }
        tempFile = null;
        saveFile = null;
        unreviewedJobEntries.clear();
        reviewedJobEntries.clear();
        sheets.clear();
    }

    /**
     * Attempts to close {@code workBook} so that the file associated can be modified
     */
    public void closeWorkBook() throws FileAccessException {
        if (workbook == null) {
            return;
        }
        File newFile = new File(CURRENT_DIRECTORY + getTimeStamp() + TEMPWORKBOOKFILE_NAME);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (IOException e) {
            throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
        } finally {
            try {
                workbook.close();
                newFile.delete();
            } catch (IOException e) {
                throw new FileAccessException(ERROR_MESSAGE_IO_EXCEPTION);
            }
        }
    }

    /**
     * Adds job entries from {@code sheetWithHeaderFields} into {@code SessionData}
     */
    public void addSheet(SheetWithHeaderFields sheetWithHeaderFields) {
        Iterator<JobEntry> jobEntryIterator = sheetWithHeaderFields.iterator();
        JobEntry jobEntry;
        while ((jobEntry = jobEntryIterator.next()) != null) {
            unreviewedJobEntries.add(jobEntry);
        }
        sheets.add(sheetWithHeaderFields.getSheetIndex(), sheetWithHeaderFields);
    }

    /*===================================================================
    Job review methods
    ===================================================================*/

    /**
     * @return a copy of unreviewed jobs stored in this sheet
     */
    public List<Job> getUnreviewedJobEntries() {
        return Collections.unmodifiableList(unreviewedJobEntries);
    }

    /**
     * @return a copy of reviewed jobs stored in this sheet
     */
    public List<Job> getReviewedJobEntries() { // marked for deletion
        return Collections.unmodifiableList(reviewedJobEntries);
    }

    /**
     * Reviews all remaining jobs using {@code reviewJobEntry}. Writes to {@code saveFile} when done.
     */
    public ArrayList<JobEntry> reviewAllRemainingJobEntries(boolean approved, String comments) throws CommandException {
        logger.info("Reviewing all remaining entries: ");
        ArrayList<JobEntry> reviewedEntries = new ArrayList<>();
        try {
            while (!getUnreviewedJobEntries().isEmpty()) {
                reviewedEntries.add(reviewJobEntry(0, approved, comments));
            }
        } catch (DataIndexOutOfBoundsException e) {
            logger.warning("Index went out of bounds before completion, should not happen: " + e.getMessage());
            throw new CommandException(e.getMessage());
        }

        try {
            saveDataToSaveFile();
        } catch (UninitializedException e) {
            unreviewJobEntries(reviewedEntries);
            throw new CommandException(e.getMessage());
        } catch (IOException e) {
            logger.warning("Unable to save to file: " + e.getMessage());
            unreviewJobEntries(reviewedEntries);
            throw new CommandException(ERROR_MESSAGE_IO_EXCEPTION);
        }
        for (JobEntry jobEntry: reviewedEntries) {
            jobEntry.confirmLastReview();
        }
        return reviewedEntries;
    }

    /**
     * Reviews a {@code JobEntry} specified by {@code listIndex}. Writes to {@code saveFile} when done.
     * @param jobIndex index of {@code JobEntry} in {@code unreviewedJobEntries}
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     * @return reviewed jobEntry
     */
    public JobEntry reviewJobEntryUsingJobIndex(int jobIndex, boolean approved, String comments)
            throws CommandException {
        logger.info("Reviewing job index " + jobIndex);
        if (unreviewedJobEntries.isEmpty()) {
            throw new CommandException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        }
        if (unreviewedJobEntries.size() < jobIndex || jobIndex <= 0) {
            throw new CommandException(ERROR_MESSAGE_INVALID_JOB_INDEX);
        }
        JobEntry entry = unreviewedJobEntries.get(Index.fromOneBased(jobIndex).getZeroBased());
        try {
            reviewJobEntry(Index.fromOneBased(jobIndex).getZeroBased(), approved, comments);
        } catch (DataIndexOutOfBoundsException e) {
            logger.warning("Index went out of bounds :" + e.getMessage());
            throw new CommandException(e.getMessage());
        }
        try {
            saveDataToSaveFile();
        } catch (IOException e) {
            logger.warning("Unable to save to file: " + e.getMessage());
            unreviewJobEntry(entry);
            throw new CommandException(ERROR_MESSAGE_IO_EXCEPTION);
        } catch (UninitializedException e) {
            unreviewJobEntry(entry);
            throw new CommandException(ERROR_MESSAGE_UNINITIALIZED);
        }
        entry.confirmLastReview();
        return entry;
    }

    /**
     * Reviews a {@code JobEntry} specified by {@code listIndex}
     * @param listIndex index of {@code JobEntry} in {@code unreviewedJobEntries}
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     * @return reviewed jobEntry
     */
    private JobEntry reviewJobEntry(int listIndex, boolean approved, String comments) throws
            DataIndexOutOfBoundsException {
        if (unreviewedJobEntries.isEmpty()) {
            throw new IllegalStateException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        } else if (listIndex < 0 || listIndex >= unreviewedJobEntries.size()) {
            throw new DataIndexOutOfBoundsException(0, unreviewedJobEntries.size(), listIndex);
        }

        JobEntry jobEntry = unreviewedJobEntries.get(listIndex);
        jobEntry.review(approved, comments);
        unreviewedJobEntries.remove(jobEntry);
        reviewedJobEntries.add(jobEntry);
        SheetWithHeaderFields sheet = sheets.get(jobEntry.getSheetNumber());
        sheet.commentJobEntry(jobEntry.getRowNumber(), jobEntry.getCommentsAsString());
        if (approved) {
            sheet.approveJobEntry(jobEntry.getRowNumber());
        } else {
            sheet.rejectJobEntry(jobEntry.getRowNumber());
        }
        return jobEntry;
    }

    private void unreviewJobEntries(ArrayList<JobEntry> jobEntries) {
        for (JobEntry jobEntry : jobEntries) {
            unreviewJobEntry(jobEntry);
        }
    }

    /**
     * Reverses the reviewing process of {@code jobEntry} in the event that it cannot be written to file
     */
    private void unreviewJobEntry(JobEntry jobEntry) {
        sheets.get(jobEntry.getSheetNumber()).unreviewJobEntry(jobEntry.getRowNumber());
        jobEntry.unreviewLastComment();
        reviewedJobEntries.remove(jobEntry);
        unreviewedJobEntries.add(jobEntry);
    }
}
```
###### \java\seedu\carvicim\storage\session\SheetParser.java
``` java
/**
 * Used to parse a Sheet from a an Excel Workbook into {@link SheetWithHeaderFields}
 */
public class SheetParser {
    public static final String INVALID_FIELD = "INVALID_FIELD";
    public static final String ERROR_MESSAGE_MISSING_FIELDS = "Missing header fields: ";
    public static final String ERROR_MESSAGE_DUPLICATE_FIELDS = "Duplicate header field: ";

    // Compulsory header fields
    public static final String CLIENT_NAME = "client name";
    public static final String CLIENT_PHONE = "client phone";
    public static final String CLIENT_EMAIL = "client email";
    public static final String VEHICLE_NUMBER = "vehicle number";
    public static final String EMPLOYEE_NAME = "employee name";
    public static final String EMPLOYEE_PHONE = "employee phone";
    public static final String EMPLOYEE_EMAIL = "employee email";

    // Comment header fields
    public static final String APPROVAL_STATUS = "approval status";
    public static final String COMMENTS = "comments";
    public static final int APPROVAL_STATUS_INDEX = 0;
    public static final int COMMENTS_INDEX = 1;

    // Optional header fields
    // public static final String JOB_NUMBER = "job number";
    // public static final String DATE = "date";
    public static final String STATUS = "status";
    public static final String REMARKS = "remarks";


    public static final String[] JOB_ENTRY_COMPULSORY_FIELDS = { // ignore case when reading headings
        CLIENT_NAME, CLIENT_PHONE, CLIENT_EMAIL, VEHICLE_NUMBER, EMPLOYEE_NAME, EMPLOYEE_PHONE, EMPLOYEE_EMAIL
    };
    public static final String[] JOB_ENTRY_OPTIONAL_FIELDS = { // ignore case when reading headings
        REMARKS
    };
    public static final String MESSAGE_SEPARATOR = ", ";

    private final Sheet sheet;
    private final ArrayList<String> missingCompulsoryFields;
    private final ArrayList<String> missingOptionalFields;
    private final HashMap<String, RowData> compulsoryFields;
    private final HashMap<String, RowData> commentFields;
    private final HashMap<String, RowData> optionalFields;


    public SheetParser(Sheet sheet) {
        this.sheet = sheet;
        missingCompulsoryFields = new ArrayList<>(
                Arrays.asList(JOB_ENTRY_COMPULSORY_FIELDS));
        missingOptionalFields =  new ArrayList<>(
                Arrays.asList(JOB_ENTRY_OPTIONAL_FIELDS));
        compulsoryFields = new HashMap<>();
        commentFields = new HashMap<>();
        optionalFields = new HashMap<>();
    }

    /**
     * Reads the {@code Sheet} and converts it into {@code SheetWithHeaderFields}
     */
    public SheetWithHeaderFields parseSheetWithHeaderField() throws FileFormatException, InvalidDataException {
        parseFirstRow();
        if (!missingCompulsoryFields.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(ERROR_MESSAGE_MISSING_FIELDS);
            for (String field : missingCompulsoryFields) {
                stringBuilder.append(field);
                stringBuilder.append(MESSAGE_SEPARATOR);
            }
            throw new InvalidDataException(stringBuilder.toString());
        }
        createCommentField(APPROVAL_STATUS, APPROVAL_STATUS_INDEX);
        createCommentField(COMMENTS, COMMENTS_INDEX);
        return new SheetWithHeaderFields(sheet, compulsoryFields, commentFields, optionalFields);
    }

    /**
     * Creates a new column with header {@code name} {@code offset} columns after last column.
     * @param offset
     */
    private void createCommentField(String name, int offset) {
        int index = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() + offset;
        commentFields.put(name, new RowData(index, index));
    }

    /**
     * Processes the header fields in the first row into {@code headerFields} and throws {@code FileFormatException}
     * if there are missing compulsory header fields
     */
    private void parseFirstRow() throws FileFormatException {
        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        DataFormatter dataFormatter = new DataFormatter();
        int lastFieldIndex = firstRow.getLastCellNum();
        String lastField = INVALID_FIELD;
        String currentField;
        // traverse the row from the back to assist detecting end of row
        for (int i = firstRow.getLastCellNum(); i >= firstRow.getFirstCellNum(); i--) {
            currentField = dataFormatter.formatCellValue(firstRow.getCell(i)).toLowerCase();
            if (lastField.equals(currentField) || !isFieldPresent(currentField)) {
                continue;
            }
            addHeaderField(currentField, new RowData(i, lastFieldIndex));
            lastField = currentField;
            lastFieldIndex = i - 1;
        }
    }

    /**
     * Removes header field from {@code missingCompulsoryFields} or {@code missingOptionalFields} and
     * places it into {@code headerFields}
     */
    private void addHeaderField(String currentField, RowData rowData) throws FileFormatException {
        if (missingCompulsoryFields.contains(currentField)) {
            missingCompulsoryFields.remove(currentField);
            compulsoryFields.put(currentField, rowData);
        } else if (missingOptionalFields.contains(currentField)) {
            missingOptionalFields.remove(currentField);
            optionalFields.put(currentField, rowData);
        } else {
            throw new FileFormatException(ERROR_MESSAGE_DUPLICATE_FIELDS + currentField);
        }
    }

    /**
     * Checks if {@code field} is present in {@code fields}, ignoring case
     */
    private boolean isFieldPresent(String field) {
        return missingCompulsoryFields.contains(field) || missingOptionalFields.contains(field);
    }
}
```
###### \java\seedu\carvicim\ui\JobCard.java
``` java
/**
 * An UI component that displays information of a {@code Employee}.
 */
public class JobCard extends UiPart<Region> {

    private static final String FXML = "JobListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on Carvicim level 4</a>
     */

    public final Job job;

    @FXML
    private HBox cardPane;
    @FXML
    private Label client;
    @FXML
    private Label index;
    @FXML
    private Label id;
    @FXML
    private Label vehicleNumber;
    @FXML
    private Label startDate;
    @FXML
    private Label status;

    public JobCard(Job job, int index) {
        super(FXML);
        this.job = job;
        this.index.setText(Integer.toString(index) + ".");
        id.setText("Job number: #" + job.getJobNumber().toString());
        client.setText(job.getClient().getName().toString());
        vehicleNumber.setText(job.getVehicleNumber().toString());
        startDate.setText(job.getDate().toString());
        status.setText("[" + job.getStatus().toString() + "]");
        if (job.getStatus().toString().equals("ongoing")) {
            status.setStyle("-fx-text-fill: green");
        } else {
            status.setStyle("-fx-text-fill: red");
        }
        // remarks are not supported in this version, might want to expand into new window due to space constraints
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JobCard)) {
            return false;
        }

        // state check
        JobCard card = (JobCard) other;
        return id.getText().equals(card.id.getText())
                && job.equals(card.job);
    }
}
```
###### \java\seedu\carvicim\ui\JobListIndicator.java
``` java
/**
 * A text box to indicate which tab the job list panel is on
 */
public class JobListIndicator extends UiPart<Region> {

    public static final String IMPORTED = "unreviewed jobs";
    public static final String SAVED = "saved jobs";

    private static final Logger logger = LogsCenter.getLogger(JobListIndicator.class);
    private static final String FXML = "JobListIndicator.fxml";

    private final StringProperty displayed = new SimpleStringProperty(SAVED);

    @FXML
    private TextArea jobListIndicator;

    public JobListIndicator() {
        super(FXML);
        jobListIndicator.textProperty().bind(displayed);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(JobListSwitchEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> displayed.setValue(event.message));
    }

}
```
###### \java\seedu\carvicim\ui\MainWindow.java
``` java
    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        if (helpWindow == null) {
            helpWindow = new HelpWindow();
        }
        if (!helpWindow.getRoot().isShowing()) {
            helpWindow.show();
        }
        helpWindow.getRoot().requestFocus();
    }

```
###### \resources\view\JobListCard.fxml
``` fxml

<HBox id="cardPane" fx:id="jobCardPane" xmlns="http://javafx.com/javafx/8.0.161" xmlns:fx="http://javafx.com/fxml/1">
  <GridPane HBox.hgrow="ALWAYS">
    <columnConstraints>
      <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
    </columnConstraints>
    <VBox alignment="CENTER_LEFT" minHeight="105" prefHeight="105.0" prefWidth="156.0" GridPane.columnIndex="0">
      <padding>
        <Insets bottom="5" left="15" right="5" top="5" />
      </padding>
         <HBox prefHeight="18.0" prefWidth="135.0">
            <children>
            <Label fx:id="startDate" styleClass="cell_small_label" text="\$startDate" />
               <Label fx:id="status" alignment="BOTTOM_RIGHT" text="\$status">
                  <HBox.margin>
                     <Insets left="100.0" />
                  </HBox.margin>
               </Label>
            </children>
         </HBox>
      <HBox alignment="CENTER_LEFT" spacing="5">
          <Label fx:id="index" styleClass="cell_big_label">
              <minWidth>
                  <!-- Ensures that the label text is never truncated -->
                  <Region fx:constant="USE_PREF_SIZE" />
              </minWidth>
          </Label>
          <Label fx:id="id" styleClass="cell_big_label">
              <minWidth>
                  <!-- Ensures that the label text is never truncated -->
                  <Region fx:constant="USE_PREF_SIZE" />
              </minWidth>
          </Label>
      </HBox>
     <Label fx:id="client" styleClass="cell_big_label" text="\$client" />
      <Label fx:id="vehicleNumber" styleClass="cell_small_label" text="\$vehicleNumber" />
    </VBox>
      <rowConstraints>
         <RowConstraints />
      </rowConstraints>
  </GridPane>
</HBox>
```
###### \resources\view\JobListIndicator.fxml
``` fxml

<StackPane fx:id="placeHolder" styleClass="label" xmlns="http://javafx.com/javafx/8.0.161" xmlns:fx="http://javafx.com/fxml/1">
  <TextArea fx:id="jobListIndicator" editable="false" styleClass="label" />
</StackPane>
```
###### \resources\view\JobListPanel.fxml
``` fxml

<VBox xmlns="http://javafx.com/javafx/9.0.1" xmlns:fx="http://javafx.com/fxml/1">
  <ListView fx:id="jobListView" VBox.vgrow="ALWAYS" />
</VBox>
```
