package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.commands.CommandTestUtil.DATERANGE_DESC_ONE;
import static seedu.carvicim.logic.commands.CommandTestUtil.DATERANGE_DESC_TWO;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_DATERANGE_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_END_DATE;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_START_DATE;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.ArchiveCommand;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.DateRange;
import seedu.carvicim.testutil.DateRangeBuilder;

//@@author richardson0694
public class ArchiveCommandParserTest {

    private ArchiveCommandParser parser = new ArchiveCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        DateRange expectedDateRange = new DateRangeBuilder().withDateRange(VALID_START_DATE, VALID_END_DATE).build();

        // one assigned employee
        assertParseSuccess(parser, DATERANGE_DESC_ONE + DATERANGE_DESC_TWO,
                new ArchiveCommand(expectedDateRange));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ArchiveCommand.MESSAGE_USAGE);

        // missing start date prefix
        assertParseFailure(parser, VALID_START_DATE + DATERANGE_DESC_TWO,
                expectedMessage);

        // missing end date prefix
        assertParseFailure(parser, DATERANGE_DESC_ONE + VALID_END_DATE,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid date format
        assertParseFailure(parser, INVALID_DATERANGE_DESC,
                String.format(Date.MESSAGE_DATE_CONSTRAINTS, ArchiveCommand.MESSAGE_USAGE));
    }

}
