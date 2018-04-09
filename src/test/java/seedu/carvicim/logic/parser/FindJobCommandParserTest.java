package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.carvicim.logic.commands.FindJobCommand;
import seedu.carvicim.model.job.JobDetailsContainKeyWordsPredicate;

//@@author whenzei
public class FindJobCommandParserTest {

    private FindJobCommandParser parser = new FindJobCommandParser();

    @Test
    public void parse_emptyArg_throwParseException() {
        assertParseFailure(parser, "         ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindJobCommand expectedFindJobCommand =
                new FindJobCommand(new JobDetailsContainKeyWordsPredicate(Arrays.asList("Apr", "Feb")));
        assertParseSuccess(parser, "Apr Feb", expectedFindJobCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Apr \n \t Feb  \t", expectedFindJobCommand);
    }

}
