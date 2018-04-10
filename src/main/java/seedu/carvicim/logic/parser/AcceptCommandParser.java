package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.ParserUtil.parseInteger;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.AcceptCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;

//@@author yuhongherald

/**
 * Parses input arguments and creates a new AcceptCommand object
 */
public class AcceptCommandParser implements Parser<AcceptCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the AcceptCommand
     * and returns an AcceptCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AcceptCommand parse(String args) throws ParseException {
        String[] arguments = args.trim().split(" ", 2);
        String comment = "";
        if (arguments.length == 2) {
            comment = arguments[1].trim();
        }
        try {
            int jobNumber = parseInteger(arguments[0]);
            return new AcceptCommand(jobNumber, comment);
        } catch (IllegalValueException ive) {
            for (String a : arguments) {
                System.out.println(a);
            }
            System.out.println(args);
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AcceptCommand.MESSAGE_USAGE));
        }
    }

}
