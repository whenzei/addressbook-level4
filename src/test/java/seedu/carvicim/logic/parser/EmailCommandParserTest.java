package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_JOB_NUMBER;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.EmailCommand;
import seedu.carvicim.model.job.JobNumber;

//@@author charmaineleehc
public class EmailCommandParserTest {

    private EmailCommandParser parser = new EmailCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + EmailCommand.COMMAND_WORD + " " + PREFIX_JOB_NUMBER + "1",
                new EmailCommand(new JobNumber("1")));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE);

        //job number not stated
        assertParseFailure(parser, EmailCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, EmailCommand.COMMAND_WORD + " " + PREFIX_JOB_NUMBER
                + "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
    }
}
