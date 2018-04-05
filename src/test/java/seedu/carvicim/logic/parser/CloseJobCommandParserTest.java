package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_JOB_NUMBER_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.JOB_NUMBER_DESC_A;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_JOB_NUMBER_ONE;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.CloseJobCommand;
import seedu.carvicim.model.job.JobNumber;

//@@author whenzei
public class CloseJobCommandParserTest {

    private CloseJobCommandParser parser = new CloseJobCommandParser();

    @Test
    public void parse_validArgs_returnsCloseJobCommand() {
        assertParseSuccess(parser, JOB_NUMBER_DESC_A, new CloseJobCommand(new JobNumber(VALID_JOB_NUMBER_ONE)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, INVALID_JOB_NUMBER_DESC, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CloseJobCommand.MESSAGE_USAGE));
    }
}
