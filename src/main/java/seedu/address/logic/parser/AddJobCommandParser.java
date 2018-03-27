package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNED_EMPLOYEE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VEHICLE_NUMBER;

import java.util.ArrayList;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddJobCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.job.VehicleNumber;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

//@@author whenzei
/**
 * Parses the input arguments and creates a new AddJobCommand object
 */
public class AddJobCommandParser implements Parser<AddJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddJobCommand
     * and returns an AddJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddJobCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_VEHICLE_NUMBER, PREFIX_ASSIGNED_EMPLOYEE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE,
                PREFIX_EMAIL, PREFIX_VEHICLE_NUMBER, PREFIX_ASSIGNED_EMPLOYEE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            VehicleNumber vehicleNumber =
                    ParserUtil.parseVehicleNumber(argMultimap.getValue(PREFIX_VEHICLE_NUMBER)).get();
            ArrayList<Index> assignedEmployeeIndices =
                    ParserUtil.parseIndices(argMultimap.getAllValues(PREFIX_ASSIGNED_EMPLOYEE));

            Person client = new Person(name, phone, email);
            return new AddJobCommand(client, vehicleNumber, assignedEmployeeIndices);

        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
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
