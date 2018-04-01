package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.ParserUtil.parseFilename;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.ImportAllCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;

//@@author yuhongherald

/**
 * Parses input arguments and creates a new AddEmployeeCommand object
 */
public class ImportAllCommandParser implements Parser<ImportAllCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the ImportAllCommand
     * and returns an ImportAllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportAllCommand parse(String args) throws ParseException {
        try {
            String filePath = parseFilename(args);
            return new ImportAllCommand(filePath);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportAllCommand.MESSAGE_USAGE));
        }
    }

}
