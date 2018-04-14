package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_JOB_INDEX;
import static seedu.carvicim.commons.util.AppUtil.checkArgument;
import static seedu.carvicim.logic.parser.ParserUtil.parseInteger;
import static seedu.carvicim.model.remark.Remark.MESSAGE_REMARKS_CONSTRAINTS;
import static seedu.carvicim.model.remark.Remark.isValidRemark;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.logic.commands.AcceptCommand;
import seedu.carvicim.logic.commands.RejectCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;

//@@author yuhongherald

/**
 * Parses input arguments and creates a new RejectCommand object
 */
public class RejectCommandParser implements Parser<RejectCommand> {

    public static final String SPACE = " ";
    public static final int NUMBER_OF_ARGUMENTS = 2;
    public static final int COMMENTS_INDEX = 1;
    public static final int JOB_INDEX_INDEX = 0;
    public static final String ERROR_MESSAGE = MESSAGE_INVALID_JOB_INDEX + "\n" + AcceptCommand.MESSAGE_USAGE;

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the RejectCommand
     * and returns a RejectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RejectCommand parse(String args) throws ParseException {
        String[] arguments = args.trim().split(SPACE, NUMBER_OF_ARGUMENTS);
        String comment = "";
        if (arguments.length == NUMBER_OF_ARGUMENTS) {
            comment = arguments[COMMENTS_INDEX].trim();
            checkArgument(isValidRemark(comment), MESSAGE_REMARKS_CONSTRAINTS);
        }
        try {
            int jobNumber = parseInteger(arguments[JOB_INDEX_INDEX]);
            return new RejectCommand(jobNumber, comment);
        } catch (IllegalValueException ive) {
            throw new ParseException(ERROR_MESSAGE);
        }
    }

}
