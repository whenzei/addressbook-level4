package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_JOB_NUMBER;

import java.util.stream.Stream;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.SelectJobCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.job.JobNumber;

/**
 * Parses the input arguments and creates a new SelectJobCommand object
 */
public class SelectJobCommandParser implements Parser<SelectJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectJobCommand
     * and returns an SelectJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public SelectJobCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_JOB_NUMBER);

        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_NUMBER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectJobCommand.MESSAGE_USAGE));
        }

        try {
            JobNumber jobNumber = ParserUtil.parseJobNumber(argMultimap.getValue(PREFIX_JOB_NUMBER)).get();
            return new SelectJobCommand(jobNumber);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectJobCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
