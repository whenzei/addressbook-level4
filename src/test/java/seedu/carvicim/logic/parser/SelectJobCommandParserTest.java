package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.commands.CommandTestUtil.JOB_NUMBER_DESC_A;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.SelectJobCommand;
import seedu.carvicim.model.job.JobNumber;

/**
 * Test scope: similar to {@code DeleteEmployeeCommandParserTest}.
 * @see DeleteEmployeeCommandParserTest
 */
public class SelectJobCommandParserTest {
    private SelectJobCommandParser parser = new SelectJobCommandParser();

    @Test
    public void parse_validArgs_returnsSelectJobCommand() {
        assertParseSuccess(parser, JOB_NUMBER_DESC_A, new SelectJobCommand(new JobNumber("1")));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectJobCommand.MESSAGE_USAGE));
    }
}
