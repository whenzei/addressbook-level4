package seedu.carvicim.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.carvicim.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicim;

import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandWordException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;

//@@author yuhongherald
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
