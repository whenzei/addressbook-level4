package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.carvicim.logic.commands.AcceptAllCommand;
import seedu.carvicim.logic.commands.AcceptCommand;
import seedu.carvicim.logic.commands.AddEmployeeCommand;
import seedu.carvicim.logic.commands.AddJobCommand;
import seedu.carvicim.logic.commands.AnalyseCommand;
import seedu.carvicim.logic.commands.ArchiveCommand;
import seedu.carvicim.logic.commands.ClearCommand;
import seedu.carvicim.logic.commands.CloseJobCommand;
import seedu.carvicim.logic.commands.Command;
import seedu.carvicim.logic.commands.CommandWords;
import seedu.carvicim.logic.commands.DeleteEmployeeCommand;
import seedu.carvicim.logic.commands.EmailCommand;
import seedu.carvicim.logic.commands.ExitCommand;
import seedu.carvicim.logic.commands.FindEmployeeCommand;
import seedu.carvicim.logic.commands.HelpCommand;
import seedu.carvicim.logic.commands.HistoryCommand;
import seedu.carvicim.logic.commands.ImportAllCommand;
import seedu.carvicim.logic.commands.ImportCommand;
import seedu.carvicim.logic.commands.ListEmployeeCommand;
import seedu.carvicim.logic.commands.ListJobCommand;
import seedu.carvicim.logic.commands.RedoCommand;
import seedu.carvicim.logic.commands.RejectAllCommand;
import seedu.carvicim.logic.commands.RejectCommand;
import seedu.carvicim.logic.commands.SaveCommand;
import seedu.carvicim.logic.commands.SelectEmployeeCommand;
import seedu.carvicim.logic.commands.SetCommand;
import seedu.carvicim.logic.commands.SortCommand;
import seedu.carvicim.logic.commands.SwitchCommand;
import seedu.carvicim.logic.commands.ThemeCommand;
import seedu.carvicim.logic.commands.UndoCommand;

import seedu.carvicim.logic.commands.exceptions.CommandWordException;
import seedu.carvicim.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class CarvicimParser {

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
    public CarvicimParser() {
        this.commandWords = new CommandWords();
    }

    public CarvicimParser(CommandWords commandWords) {
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

        case SelectEmployeeCommand.COMMAND_WORD:
            return new SelectEmployeeCommandParser().parse(arguments);

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
        /* Requires bug fix
        case RemarkCommand.COMMAND_WORD:
            return new RemarkCommandParser().parse(arguments);
        */
        case EmailCommand.COMMAND_WORD:
            return new EmailCommand();

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments);

        case SaveCommand.COMMAND_WORD:
            return new SaveCommand();

        case ListJobCommand.COMMAND_WORD:
            return new ListJobCommand();

        case SwitchCommand.COMMAND_WORD:
            return new SwitchCommand();

        case AcceptAllCommand.COMMAND_WORD:
            return new AcceptAllCommand();

        case RejectAllCommand.COMMAND_WORD:
            return new RejectAllCommand();

        case RejectCommand.COMMAND_WORD:
            return new RejectCommandParser().parse(arguments);

        case CloseJobCommand.COMMAND_WORD:
            return new CloseJobCommandParser().parse(arguments);

        case AcceptCommand.COMMAND_WORD:
            return new AcceptCommandParser().parse(arguments);

        case AnalyseCommand.COMMAND_WORD:
            return new AnalyseCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
