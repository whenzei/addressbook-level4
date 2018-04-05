package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_JOB_NUMBER;

import java.util.stream.Stream;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.CloseJobCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.job.JobNumber;

/**
 * Parses input arguments and creates a new CloseJobCommand object
 */
public class CloseJobCommandParser implements Parser<CloseJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CloseJobCommand
     * and returns an CloseJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CloseJobCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_JOB_NUMBER);

        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_NUMBER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CloseJobCommand.MESSAGE_USAGE));
        }

        try {
            JobNumber jobNumber = ParserUtil.parseJobNumber(argMultimap.getValue(PREFIX_JOB_NUMBER)).get();

            return new CloseJobCommand(jobNumber);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CloseJobCommand.MESSAGE_USAGE));
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
