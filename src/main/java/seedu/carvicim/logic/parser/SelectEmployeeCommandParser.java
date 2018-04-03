package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.SelectEmployeeCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SelectEmployeeCommand object
 */
public class SelectEmployeeCommandParser implements Parser<SelectEmployeeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectEmployeeCommand
     * and returns an SelectEmployeeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectEmployeeCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SelectEmployeeCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEmployeeCommand.MESSAGE_USAGE));
        }
    }
}
