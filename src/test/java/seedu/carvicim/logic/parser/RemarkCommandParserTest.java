package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_JOB_NUMBER_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_REMARK_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.JOB_NUMBER_DESC_A;
import static seedu.carvicim.logic.commands.CommandTestUtil.JOB_NUMBER_DESC_B;
import static seedu.carvicim.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.carvicim.logic.commands.CommandTestUtil.REMARK_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_JOB_NUMBER_ONE;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_REMARK;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.RemarkCommand;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.remark.Remark;

//@@author whenzei
public class RemarkCommandParserTest {
    private RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Remark expectedRemark = new Remark(VALID_REMARK);
        JobNumber expectedJobNumber = new JobNumber(VALID_JOB_NUMBER_ONE);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + JOB_NUMBER_DESC_A + REMARK_DESC,
                new RemarkCommand(expectedRemark, expectedJobNumber));

        // multiple job number - last job taken
        assertParseSuccess(parser, JOB_NUMBER_DESC_B + JOB_NUMBER_DESC_A + REMARK_DESC,
                new RemarkCommand(expectedRemark, expectedJobNumber));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);

        // missing job number prefix
        assertParseFailure(parser, VALID_JOB_NUMBER_ONE + REMARK_DESC, expectedMessage);

        // missing remark prefix
        assertParseFailure(parser, JOB_NUMBER_DESC_A + VALID_REMARK, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid job number
        assertParseFailure(parser, INVALID_JOB_NUMBER_DESC + REMARK_DESC, JobNumber.MESSAGE_JOB_NUMBER_CONSTRAINTS);

        // invalid remark
        assertParseFailure(parser, JOB_NUMBER_DESC_A + INVALID_REMARK_DESC, Remark.MESSAGE_REMARKS_CONSTRAINTS);
    }
}
