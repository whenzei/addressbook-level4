package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.DeleteEmployeeCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteEmployeeCommand object
 */
public class DeleteEmployeeCommandParser implements Parser<DeleteEmployeeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteEmployeeCommand
     * and returns an DeleteEmployeeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteEmployeeCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteEmployeeCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEmployeeCommand.MESSAGE_USAGE));
        }
    }

}
