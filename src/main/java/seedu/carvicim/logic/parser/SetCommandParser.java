package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.ParserUtil.parseWords;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.SetCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;

//@@author yuhongherald
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
