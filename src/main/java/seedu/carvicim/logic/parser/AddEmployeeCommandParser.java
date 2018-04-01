package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.AddEmployeeCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddEmployeeCommand object
 */
public class AddEmployeeCommandParser implements Parser<AddEmployeeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddEmployeeCommand
     * and returns an AddEmployeeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddEmployeeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            Employee employee = new Employee(name, phone, email, tagList);

            return new AddEmployeeCommand(employee);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
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
