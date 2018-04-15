package seedu.carvicim.logic.parser;

import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.carvicim.logic.parser.RejectCommandParser.ERROR_MESSAGE;

import org.junit.Test;

import seedu.carvicim.logic.commands.RejectCommand;



//@@author yuhongherald
public class RejectCommandParserTest {
    private RejectCommandParser parser = new RejectCommandParser();

    @Test
    public void parse_rejectWithoutComment_success() {
        assertParseSuccess(parser, " 1", new RejectCommand(1, ""));
    }

    @Test
    public void parse_rejectWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, " 1 " + comment, new RejectCommand(1, comment));
    }

    @Test
    public void parse_invalidNumber_failure() {
        assertParseFailure(parser, "", ERROR_MESSAGE);
    }
}
