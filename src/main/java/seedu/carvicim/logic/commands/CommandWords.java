package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import seedu.carvicim.logic.commands.exceptions.CommandWordException;

//@@author yuhongherald
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
