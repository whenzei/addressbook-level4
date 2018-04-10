package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.ParserUtil.parseInteger;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.RejectCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;

//@@author yuhongherald

/**
 * Parses input arguments and creates a new RejectCommand object
 */
public class RejectCommandParser implements Parser<RejectCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the RejectCommand
     * and returns a RejectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RejectCommand parse(String args) throws ParseException {
        String[] arguments = args.split(" ", 2);
        String comment = "";
        if (arguments.length == 2) {
            comment = arguments[1].trim();
        }
        try {
            int jobNumber = parseInteger(arguments[0]);
            return new RejectCommand(jobNumber, comment);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RejectCommand.MESSAGE_USAGE));
        }
    }

}
