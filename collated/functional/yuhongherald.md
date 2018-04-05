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
###### \java\seedu\carvicim\logic\commands\AcceptAllCommand.java
``` java

/**
 * Accepts all remaining unreviewed job entries into Servicing Manager
 */
public class AcceptAllCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "acceptAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Accepts all unreviewed job entries. "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "%d job entries accepted!";

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, entries);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        if (importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("There are no job entries to review!");
        }
        try {
            importSession.reviewAllRemainingJobEntries(true);
            List<Job> jobs = new ArrayList<>(importSession.getSessionData().getReviewedJobEntries());
            model.addJobs(jobs);
            importSession.closeSession();
            return new CommandResult(getMessageSuccess(jobs.size()));
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (IOException e) {
            throw new CommandException("Unable to export file. Please close the application and try again.");
        } catch (UnitializedException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcceptAllCommand); // instanceof handles nulls
    }

}
```
###### \java\seedu\carvicim\logic\commands\AcceptCommand.java
``` java

/**
 * Accepts an unreviewed job entry using job number and adds into servicing manager
 */
public class AcceptCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "accept";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Accepts job entry using job number. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d accepted!";

    private int jobNumber;

    public AcceptCommand(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getMessageSuccess() {
        return String.format(MESSAGE_SUCCESS, jobNumber);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        if (importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("There are no job entries to review!");
        }
        try {
            importSession.getSessionData().reviewJobEntryUsingJobNumber(jobNumber, true, "");

        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (InvalidDataException e) {
            throw new CommandException(e.getMessage());
        }
        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcceptCommand); // instanceof handles nulls
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
        ImportAllCommand.COMMAND_WORD,
        ListEmployeeCommand.COMMAND_WORD,
        RedoCommand.COMMAND_WORD,
        SelectEmployeeCommand.COMMAND_WORD,
        SetCommand.COMMAND_WORD,
        UndoCommand.COMMAND_WORD,
        ThemeCommand.COMMAND_WORD,
        SortCommand.COMMAND_WORD,
        ImportCommand.COMMAND_WORD,
        SaveCommand.COMMAND_WORD,
        ListJobCommand.COMMAND_WORD,
        SwitchCommand.COMMAND_WORD,
        AcceptAllCommand.COMMAND_WORD,
        RejectAllCommand.COMMAND_WORD,
        RejectCommand.COMMAND_WORD,
        AcceptCommand.COMMAND_WORD
    };

    public final HashMap<String, String> commands;
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
     * Returns whether (@code commandWord) is in (@code COMMANDS)
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
        throwExceptionIfCommandWordsNotValid(currentWord, newWord);
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
     * throws a (@code CommandWordException) if:
     * 1. Both words are the same
     * 2. (@code newWord) overwrites the default word for another command
     * 3. (@code newWord) is already in use
     */
    private void throwExceptionIfCommandWordsNotValid(String currentWord, String newWord) throws CommandWordException {
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
     * Copies key and value of (@code command) from (@code commands)
     * to (@code verifiedCommands). Creates a new entry with default
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
        builder.append("Commands: \n");
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
 * Attempts to import all (@code JobEntry) into Servicing Manager
 */
public class ImportAllCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "importAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports job entries from from an excel file. "
            + "Parameters: FILEPATH\n"
            + "Example: " + COMMAND_WORD + "yourfile.xls";

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
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        try {
            importSession.initializeSession(filePath);
        } catch (FileAccessException e) {
            throw new CommandException(e.getMessage());
        } catch (FileFormatException e) {
            throw new CommandException("Excel file first row headers are not defined properly. "
                    + "Type 'help' to read more.");
        }
        try {
            importSession.reviewAllRemainingJobEntries(true);
            List<Job> jobs = new ArrayList<>(importSession.getSessionData().getReviewedJobEntries());
            model.addJobs(jobs);
            importSession.closeSession();
            return new CommandResult(getMessageSuccess(jobs.size()));
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (IOException e) {
            throw new CommandException("Unable to export file. Please close the application and try again.");
        } catch (UnitializedException e) {
            throw new CommandException(e.getMessage());
        }
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
            + "Example: " + COMMAND_WORD + "yourfile.xls";

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
        } catch (FileAccessException e) {
            throw new CommandException(e.getMessage());
        } catch (FileFormatException e) {
            throw new CommandException("Excel file first row headers are not defined properly. "
                    + "Type 'help' to read more.");
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
 * Rejects all remaining unreviewed job entries into Servicing Manager
 */
public class RejectAllCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "rejectAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Rejects all unreviewed job entries. "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "%d job entries rejected!";

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, entries);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        if (importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("There are no job entries to review!");
        }
        try {
            importSession.reviewAllRemainingJobEntries(false);
            List<Job> jobs = new ArrayList<>(importSession.getSessionData().getReviewedJobEntries());
            model.addJobs(jobs);
            importSession.closeSession();
            return new CommandResult(getMessageSuccess(jobs.size()));
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (IOException e) {
            throw new CommandException("Unable to export file. Please close the application and try again.");
        } catch (UnitializedException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RejectAllCommand); // instanceof handles nulls
    }

}
```
###### \java\seedu\carvicim\logic\commands\RejectCommand.java
``` java

/**
 * Rejects an unreviewed job entry using job number
 */
public class RejectCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "reject";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Rejects job entry using job number. "
            + "Example: " + COMMAND_WORD + " JOB_NUMBER";

    public static final String MESSAGE_SUCCESS = "Job #%d rejected!";

    private int jobNumber;

    public RejectCommand(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getMessageSuccess() {
        return String.format(MESSAGE_SUCCESS, jobNumber);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        if (importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("There are no job entries to review!");
        }
        try {
            importSession.getSessionData().reviewJobEntryUsingJobNumber(jobNumber, false, "");
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (InvalidDataException e) {
            throw new CommandException(e.getMessage());
        }
        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RejectCommand); // instanceof handles nulls
    }

}
```
###### \java\seedu\carvicim\logic\commands\SaveCommand.java
``` java

/**
 * Attempts to write reviewed jobs with feedback into an excel file
 */
public class SaveCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "save";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Saves your reviewed job entries as an excel file.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Your reviewed job entries have been saved to %s!";

    public String getMessageSuccess(String filePath) {
        return String.format(MESSAGE_SUCCESS, filePath);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        String message;
        if (!importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("Please review all remaining job entries before saving!");
        }
        try {
            importSession.reviewAllRemainingJobEntries(true);
            List<Job> jobs = new ArrayList<>(importSession.getSessionData().getReviewedJobEntries());
            model.addJobs(jobs);
            message = importSession.closeSession();
        } catch (IOException e) {
            throw new CommandException("Unable to export file. Please close the application and try again.");
        } catch (UnitializedException e) {
            throw new CommandException(e.getMessage());
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException((e.getMessage()));
        }
        ObservableList<Job> jobList = model.getFilteredJobList();
        if (model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess(message));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SaveCommand); // instanceof handles nulls
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

    public static final String MESSAGE_SUCCESS = "%s has been replaced with %s!";

    private final String currentWord;
    private final String newWord;

    /**
     * Creates an SetCommand to set the specified {@code CommandWords}
     */
    public SetCommand(String currentWord, String newWord) {
        this.currentWord = currentWord;
        this.newWord = newWord;
    }

    public String getMessageSuccess() {
        return String.format(MESSAGE_SUCCESS, currentWord, newWord);
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
        return new CommandResult(getMessageSuccess());
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
public class SwitchCommand extends Command {

    public static final String COMMAND_WORD = "switch";

    public static final String MESSAGE_SUCCESS = "Switched job lists.";


    @Override
    public CommandResult execute() {
        model.switchJobView();
        model.resetJobView();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\carvicim\logic\parser\AcceptCommandParser.java
``` java

/**
 * Parses input arguments and creates a new ImporatAllCommand object
 */
public class AcceptCommandParser implements Parser<AcceptCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the ImportAllCommand
     * and returns an ImportAllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AcceptCommand parse(String args) throws ParseException {
        try {
            int jobNumber = parseInteger(args);
            return new AcceptCommand(jobNumber);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AcceptCommand.MESSAGE_USAGE));
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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportAllCommand.MESSAGE_USAGE));
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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\carvicim\logic\parser\RejectCommandParser.java
``` java

/**
 * Parses input arguments and creates a new ImporatAllCommand object
 */
public class RejectCommandParser implements Parser<RejectCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the ImportAllCommand
     * and returns an ImportAllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RejectCommand parse(String args) throws ParseException {
        try {
            int jobNumber = parseInteger(args);
            return new RejectCommand(jobNumber);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RejectCommand.MESSAGE_USAGE));
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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\carvicim\model\job\JobList.java
``` java
    /**
     * Filters (@code jobList) for jobs assigned to (@code employee).
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
###### \java\seedu\carvicim\storage\session\exceptions\DataIndexOutOfBoundsException.java
``` java
/**
 * Represents an error which occurs when trying to access data out of specified range.
 */
public class DataIndexOutOfBoundsException extends Exception {
    public static final String ERROR_MESSAGE = "%s expected index %d to %d, but got %d";

    public DataIndexOutOfBoundsException(String field, int lower, int upper, int actual) {
        super(String.format(ERROR_MESSAGE, field, lower, upper, actual));
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
###### \java\seedu\carvicim\storage\session\exceptions\UnitializedException.java
``` java
/**
 * Represents an error when {@link seedu.carvicim.storage.session.SessionData} is not initialized.
 */
public class UnitializedException extends Exception {
    public UnitializedException(String message) {
        super(message);
    }
}
```
###### \java\seedu\carvicim\storage\session\ImportSession.java
``` java
/**
 * Used to store data relevant to importing of (@code Job) from (@code inFile) and
 * exporting (@code Job) with commens to (@code outFile). Implements a Singleton design pattern.
 */
public class ImportSession {

    private static ImportSession importSession;

    private SessionData sessionData;

    private ImportSession() {
        sessionData = new SessionData();
    }

    public static ImportSession getInstance() {
        if (importSession == null) {
            importSession = new ImportSession();
        }
        return importSession;
    }

    public void setSessionData(SessionData sessionData) {
        requireNonNull(sessionData);
        this.sessionData = sessionData;
    }

    /**
     *  Opens excel file specified by (@code filepath) and initializes (@code SessionData) to support import operations
     */
    public void initializeSession(String filePath) throws FileAccessException, FileFormatException {
        sessionData.loadFile(filePath);
    }

    public void reviewAllRemainingJobEntries(boolean approve) throws DataIndexOutOfBoundsException {
        sessionData.reviewAllRemainingJobEntries(approve, "Imported with no comments.");
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    /**
     * Flushes feedback to (@code outFile) and releases resources. Currently not persistent.
     */
    public String closeSession() throws IOException, UnitializedException {
        return sessionData.saveData();
    }
}
```
###### \java\seedu\carvicim\storage\session\JobEntry.java
``` java
/**
 * Represents a job entry in an (@link ImportSession)
 */
public class JobEntry extends Job {
    public static final String NEWLINE = "\n";

    private final int sheetNumber;
    private final int rowNumber;

    private boolean reviewed;
    private boolean approved;
    private final ArrayList<String> comments;

    public JobEntry (Person client, VehicleNumber vehicleNumber, JobNumber jobNumber, Date date,
                     UniqueEmployeeList assignedEmployees, Status status, RemarkList remarks,
                     int sheetNumber, int rowNumber, String importComment) {
        super(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
        this.sheetNumber = sheetNumber;
        this.rowNumber = rowNumber;
        comments = new ArrayList<>();
        addComment(importComment);
        reviewed = false;
    }

    /**
     * Adds a non-empty comment to both remarks and comments.
     */
    private void addComment(String comment) {
        if (comment != null && isValidRemark(comment)) {
            remarks.add(new Remark(comment));
            comments.add(comment);
        }
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public boolean isApproved() {
        return approved;
    }

    /**
     * Marks (@code JobEntry) as reviewed.
     * @param approved whether (@code JobEntry) is going to be added to Carvicim
     * @param comment feedback for (@code JobEntry) in String representation
     */
    public void review(boolean approved, String comment) {
        this.approved = approved;
        addComment(comment);
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
     * Reads all the entries between (@code startIndex) and (@code endIndex) from a row in the excel file
     */
    public ArrayList<String> readDataFromSheet(Sheet sheet, int rowNumber)
            throws DataIndexOutOfBoundsException {
        if (rowNumber < sheet.getFirstRowNum() || rowNumber > sheet.getLastRowNum()) {
            throw new DataIndexOutOfBoundsException("Rows", sheet.getFirstRowNum(), sheet.getLastRowNum(), rowNumber);
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
 * A data structure used to keep track of job entries in an (@code ImportSession)
 */
public class SessionData {
    public static final String ERROR_MESSAGE_FILE_OPEN = "An excel file is already open.";
    public static final String ERROR_MESSAGE_INVALID_FILEPATH = "Please check the path to your file.";
    public static final String ERROR_MESSAGE_READ_PERMISSION = "Please enable file read permission.";
    public static final String ERROR_MESSAGE_FILE_FORMAT = "Unable to read the format of file. "
            + "Please ensure the file is in .xls or .xlsx format";
    public static final String ERROR_MESSAGE_IO_EXCEPTION = "Unable to read file. Please close the file and try again.";
    public static final String ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST = "There are no unreviewed job entries left!";

    public static final String FILE_PATH_CHARACTER = "/";
    public static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss";
    public static final String SAVEFILE_SUFFIX = "";
    public static final String TEMPFILE_SUFFIX = "temp";
    public static final String ERROR_MESSAGE_UNITIALIZED = "There is no imported file to save!";

    private final ArrayList<JobEntry> jobEntries;
    private final ArrayList<JobEntry> unreviewedJobEntries;
    private final ArrayList<JobEntry> reviewedJobEntries;
    private final ArrayList<SheetWithHeaderFields> sheets;
    // will be using an ObservableList

    private File importFile;
    private File tempFile;
    private Workbook workbook; // write comments to column after last row, with approval status
    private File saveFile;


    public SessionData() {
        jobEntries = new ArrayList<>();
        unreviewedJobEntries = new ArrayList<>();
        reviewedJobEntries = new ArrayList<>();
        sheets = new ArrayList<>();
    }

    public SessionData(ArrayList<JobEntry> jobEntries, ArrayList<JobEntry> unreviewedJobEntries,
                       ArrayList<JobEntry> reviewedJobEntries,
                       ArrayList<SheetWithHeaderFields> sheets,
                       File importFile, File tempFile, Workbook workbook, File saveFile) {
        this.jobEntries = jobEntries;
        this.unreviewedJobEntries = unreviewedJobEntries;
        this.reviewedJobEntries = reviewedJobEntries;
        this.sheets = sheets;
        this.importFile = importFile;
        this.tempFile = tempFile;
        this.workbook = workbook;
        this.saveFile = saveFile;
    }

    /**
     * Creates a copy of sessionData and returns it
     */
    public SessionData createCopy() {
        SessionData other = new SessionData(new ArrayList<>(jobEntries), new ArrayList<>(unreviewedJobEntries),
                new ArrayList<>(reviewedJobEntries), new ArrayList<>(sheets), importFile,
                tempFile, workbook, saveFile);
        return other;
    }

    public boolean isInitialized() {
        return (workbook != null);
    }

    /*===================================================================
    savefile methods
    ===================================================================*/

    /**
     * Creates a file using relative filePath of (@code importFile), then appending a timestamp and
     * (@code appendedName) to make it unique
     */
    private File generateFile(String appendedName) {
        requireNonNull(importFile);
        String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(importFile.getParent());
        stringBuilder.append(FILE_PATH_CHARACTER);
        stringBuilder.append(timeStamp);
        stringBuilder.append(importFile.getName());
        stringBuilder.append(appendedName);
        return new File(stringBuilder.toString());
    }

    public String getSaveFilePath() {
        if (saveFile != null) {
            return saveFile.getPath();
        }
        saveFile = generateFile(SAVEFILE_SUFFIX);
        return saveFile.getPath();
    }

    /**
     * Sets the filePath using relative address from import file
     */
    public void setSaveFile(String filePath) {
        saveFile = new File(filePath);
    }

    /*===================================================================
    Initialization methods
    ===================================================================*/

    /**
     * Attempts load file specified at (@code filePath) if there is no currently open file and
     * specified file exists, is readable and is an excel file
     */
    public void loadFile(String filePath) throws FileAccessException, FileFormatException {
        if (isInitialized()) {
            throw new FileAccessException(ERROR_MESSAGE_FILE_OPEN);
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileAccessException(ERROR_MESSAGE_INVALID_FILEPATH);
        } else if (!file.canRead()) {
            throw new FileAccessException(ERROR_MESSAGE_READ_PERMISSION);
        }
        importFile = file;

        try {
            workbook = createWorkBook(file);
        } catch (InvalidFormatException e) {
            throw new FileFormatException(ERROR_MESSAGE_FILE_FORMAT);
        } catch (IOException e) {
            throw new FileFormatException(ERROR_MESSAGE_IO_EXCEPTION);
        }

        initializeSessionData();
    }

    /**
     * Attempts to create a (@code Workbook) for a given (@code File)
     */
    private Workbook createWorkBook(File file) throws IOException, InvalidFormatException {
        tempFile = generateFile(TEMPFILE_SUFFIX);
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        Workbook workbook = WorkbookFactory.create(file);
        workbook.write(fileOutputStream);
        workbook.close();
        workbook = WorkbookFactory.create(tempFile);
        return workbook;
    }
    /**
     * Attempts to parse the column headers and retrieve job entries
     */
    public void initializeSessionData() throws FileFormatException {
        SheetWithHeaderFields sheetWithHeaderFields;
        SheetParser sheetParser;
        Sheet sheet;

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(workbook.getFirstVisibleTab() + i);
            sheetParser = new SheetParser(sheet);
            sheetWithHeaderFields = sheetParser.parseSheetWithHeaderField();
            addSheet(sheetWithHeaderFields);
        }
    }

    /**
     * Saves feedback to specified saveFile path
     */
    public String saveData() throws IOException, UnitializedException {
        if (!isInitialized()) {
            throw new UnitializedException(ERROR_MESSAGE_UNITIALIZED);
        }
        for (JobEntry jobEntry : jobEntries) {
            SheetWithHeaderFields sheet = sheets.get(jobEntry.getSheetNumber());
            sheet.commentJobEntry(jobEntry.getRowNumber(), jobEntry.getCommentsAsString());
            if (jobEntry.isApproved()) {
                sheet.approveJobEntry(jobEntry.getRowNumber());
            } else {
                sheet.rejectJobEntry(jobEntry.getRowNumber());
            }
        }

        if (saveFile == null) { // does not check if a file exists
            saveFile = generateFile(SAVEFILE_SUFFIX);
        }
        FileOutputStream fileOut = new FileOutputStream(saveFile);
        String path = saveFile.getAbsolutePath();
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        freeResources();
        return path;
    }
    /**
     * Releases resources associated with ImportSession by nulling field
     */
    public void freeResources() {
        workbook = null;
        importFile = null;
        if (tempFile != null) {
            tempFile.delete();
        }
        tempFile = null;
        saveFile = null;
        unreviewedJobEntries.clear();
        reviewedJobEntries.clear();
        sheets.clear();
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
    public List<Job> getReviewedJobEntries() {
        return Collections.unmodifiableList(reviewedJobEntries);
    }

    /**
     * Adds job entries from (@code sheetWithHeaderFields) into (@code SessionData)
     */
    public void addSheet(SheetWithHeaderFields sheetWithHeaderFields) {
        Iterator<JobEntry> jobEntryIterator = sheetWithHeaderFields.iterator();
        JobEntry jobEntry;
        while (jobEntryIterator.hasNext()) {
            jobEntry = jobEntryIterator.next();
            jobEntries.add(jobEntry);
            unreviewedJobEntries.add(jobEntry);
        }
        sheets.add(sheetWithHeaderFields.getSheetIndex(), sheetWithHeaderFields);
    }

    /**
     * Reviews all remaining jobs using (@code reviewJobEntry)
     */
    public void reviewAllRemainingJobEntries(boolean approved, String comments) throws DataIndexOutOfBoundsException {
        while (!getUnreviewedJobEntries().isEmpty()) {
            reviewJobEntry(0, approved, comments);
        }
    }

    /**
     * Reviews a (@code JobEntry) specified by (@code listIndex)
     * @param jobNumber index of (@code JobEntry) in (@code unreviewedJobEntries)
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     */
    public void reviewJobEntryUsingJobNumber(int jobNumber, boolean approved, String comments)
            throws DataIndexOutOfBoundsException, InvalidDataException {
        if (unreviewedJobEntries.isEmpty()) {
            throw new IllegalStateException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        }
        JobEntry entry;
        for (int i = 0; i < unreviewedJobEntries.size(); i++) {
            entry = unreviewedJobEntries.get(i);
            if (entry.getJobNumber().asInteger() == jobNumber) {
                reviewJobEntry(i, approved, comments);
                return;
            }
        }
        throw new InvalidDataException("Job number not found!");
    }

    /**
     * Reviews a (@code JobEntry) specified by (@code listIndex)
     * @param listIndex index of (@code JobEntry) in (@code unreviewedJobEntries)
     * @param approved whether job entry will be added to Carvicim
     * @param comments feedback in string representation
     */
    public void reviewJobEntry(int listIndex, boolean approved, String comments) throws DataIndexOutOfBoundsException {
        if (unreviewedJobEntries.isEmpty()) {
            throw new IllegalStateException(ERROR_MESSAGE_EMPTY_UNREVIWED_JOB_LIST);
        } else if (listIndex < 0 || listIndex >= unreviewedJobEntries.size()) {
            throw new DataIndexOutOfBoundsException("Rows", 0, unreviewedJobEntries.size(), listIndex);
        }

        JobEntry jobEntry = unreviewedJobEntries.get(listIndex);
        jobEntry.review(approved, comments);
        unreviewedJobEntries.remove(jobEntry);
        if (approved) {
            reviewedJobEntries.add(jobEntry);
        }
    }
}
```
###### \java\seedu\carvicim\storage\session\SheetParser.java
``` java
/**
 * a
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
        STATUS, REMARKS
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
     * Reads the (@code Sheet) and converts it into (@code SheetWithHeaderFields)
     */
    public SheetWithHeaderFields parseSheetWithHeaderField() throws FileFormatException {
        parseFirstRow();
        if (!missingCompulsoryFields.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(ERROR_MESSAGE_MISSING_FIELDS);
            for (String field : missingCompulsoryFields) {
                stringBuilder.append(field);
                stringBuilder.append(MESSAGE_SEPARATOR);
            }
            throw new FileFormatException(stringBuilder.toString());
        }
        createCommentField(APPROVAL_STATUS, APPROVAL_STATUS_INDEX);
        createCommentField(COMMENTS, COMMENTS_INDEX);
        return new SheetWithHeaderFields(sheet, compulsoryFields, commentFields, optionalFields);
    }

    /**
     * Creates a new column with header (@code name) (@code offset) columns after last column.
     * @param offset
     */
    private void createCommentField(String name, int offset) {
        int index = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() + offset;
        commentFields.put(name, new RowData(index, index));
    }

    /**
     * Processes the header fields in the first row into (@code headerFields) and throws (@code FileFormatException)
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
            if (currentField.equals(lastField)) {
                continue;
            }
            if (!isFieldPresent(currentField)) {
                lastField = INVALID_FIELD;
                lastFieldIndex = i - 1;
                continue;
            }
            addHeaderField(currentField, new RowData(i, lastFieldIndex));
            lastField = currentField;
            lastFieldIndex = i - 1;
        }
    }

    /**
     * Removes header field from (@code missingCompulsoryFields) or (@code missingOptionalFields) and
     * places it into (@code headerFields)
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
     * Checks if (@code field) is present in (@code fields), ignoring case
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
    private Label id;
    @FXML
    private Label vehicleNumber;
    @FXML
    private Label startDate;
    @FXML
    private Label status;

    public JobCard(Job job) {
        super(FXML);
        this.job = job;
        id.setText(job.getJobNumber().toString());
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
###### \java\seedu\carvicim\ui\JobListPanel.java
``` java
/**
 * Panel containing the list of jobs.
 */
public class JobListPanel extends UiPart<Region> {
    private static final String FXML = "JobListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(JobListPanel.class);

    private ObservableList<Job> jobList;

    @FXML
    private ListView<JobCard> jobListView;

    public JobListPanel(ObservableList<Job> jobList) {
        super(FXML);
        this.jobList = jobList;
        setConnections(jobList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Job> jobList) {
        ObservableList<JobCard> mappedList = EasyBind.map(
                jobList, (job) -> new JobCard(job));
        jobListView.setItems(mappedList);
        jobListView.setCellFactory(listView -> new JobListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        jobListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in job list panel changed to : '" + newValue + "'");
                        raise(new JobPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code JobCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            jobListView.scrollTo(index);
            jobListView.getSelectionModel().clearAndSelect(index);
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code JobCard}.
     */
    class JobListViewCell extends ListCell<JobCard> {

        @Override
        protected void updateItem(JobCard person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(person.getRoot());
            }
        }
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updateList(event.getNewSelection().employee);
    }

    @Subscribe
    private void handleDisplayAllJobsEvent(DisplayAllJobsEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        jobList = event.getJobList();
        setConnections(jobList);
    }

    private void updateList(Employee employee) {
        ObservableList<Job> filteredList = FXCollections.unmodifiableObservableList(
                jobList.filtered(JobList.filterByEmployee(jobList, employee)));
        setConnections(filteredList);
    }

}
```
###### \resources\view\JobListCard.fxml
``` fxml

<HBox id="cardPane" fx:id="jobCardPane" xmlns="http://javafx.com/javafx/9.0.1" xmlns:fx="http://javafx.com/fxml/1">
  <GridPane HBox.hgrow="ALWAYS">
    <columnConstraints>
      <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
    </columnConstraints>
    <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
      <padding>
        <Insets bottom="5" left="15" right="5" top="5" />
      </padding>
         <HBox prefHeight="18.0" prefWidth="130.0">
            <children>
            <Label fx:id="startDate" styleClass="cell_small_label" text="\$startDate" />
               <Label fx:id="status" alignment="BOTTOM_RIGHT" text="\$status">
                  <HBox.margin>
                     <Insets left="200.0" />
                  </HBox.margin>
               </Label>
            </children>
         </HBox>
      <HBox alignment="CENTER_LEFT" spacing="5">
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
###### \resources\view\JobListPanel.fxml
``` fxml

<VBox xmlns="http://javafx.com/javafx/9.0.1" xmlns:fx="http://javafx.com/fxml/1">
  <ListView fx:id="jobListView" />
</VBox>
```
