package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddEmployeeCommand;
import seedu.address.logic.commands.AddJobCommand;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandWords;
import seedu.address.logic.commands.DeleteEmployeeCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindEmployeeCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ImportAllCommand;
import seedu.address.logic.commands.ListEmployeeCommand;
import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.ThemeCommand;
import seedu.address.logic.commands.UndoCommand;

import seedu.address.logic.commands.exceptions.CommandWordException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Reference to command words used.
     */
    private final CommandWords commandWords;

    /**
     * Used only for testing purposes.
     */
    public AddressBookParser() {
        this.commandWords = new CommandWords();
    }

    public AddressBookParser(CommandWords commandWords) {
        this.commandWords = commandWords;
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        try {
            commandWord = commandWords.getCommandKey(commandWord);
        } catch (CommandWordException e) {
            // do nothing, default throws parseException
        }

        switch (commandWord) {
        case AddEmployeeCommand.COMMAND_WORD:
            return new AddEmployeeCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteEmployeeCommand.COMMAND_WORD:
            return new DeleteEmployeeCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindEmployeeCommand.COMMAND_WORD:
            return new FindEmployeeCommandParser().parse(arguments);

        case ImportAllCommand.COMMAND_WORD:
            return new ImportAllCommandParser().parse(arguments);

        case ListEmployeeCommand.COMMAND_WORD:
            return new ListEmployeeCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case SortCommand.COMMAND_WORD:
            return new SortCommand();

        case ThemeCommand.COMMAND_WORD:
            return new ThemeCommandParser().parse(arguments);

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case SetCommand.COMMAND_WORD:
            return new SetCommandParser().parse(arguments);

        case ArchiveCommand.COMMAND_WORD:
            return new ArchiveCommandParser().parse(arguments);

        case AddJobCommand.COMMAND_WORD:
            return new AddJobCommandParser().parse(arguments);

        case LoginCommand.COMMAND_WORD:
            return new LoginCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
