package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.carvicim.logic.commands.FindJobCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.job.JobDetailsContainKeyWordsPredicate;

/**
 * Parses input arguments and creates a new FindJobCommand object
 */
public class FindJobCommandParser implements Parser<FindJobCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindJobCommand
     * and returns an FindJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindJobCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindJobCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindJobCommand(new JobDetailsContainKeyWordsPredicate(Arrays.asList(nameKeywords)));
    }

}
