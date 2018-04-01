package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.carvicim.logic.commands.FindEmployeeCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindEmployeeCommand object
 */
public class FindEmployeeCommandParser implements Parser<FindEmployeeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindEmployeeCommand
     * and returns an FindEmployeeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindEmployeeCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEmployeeCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindEmployeeCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
