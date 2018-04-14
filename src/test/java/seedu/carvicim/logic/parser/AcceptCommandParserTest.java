package seedu.carvicim.logic.parser;

import static seedu.carvicim.logic.parser.AcceptCommandParser.ERROR_MESSAGE;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.AcceptCommand;

//@@author yuhongherald
public class AcceptCommandParserTest {
    private AcceptCommandParser parser = new AcceptCommandParser();

    @Test
    public void parse_acceptWithoutComment_success() {
        assertParseSuccess(parser, " 1", new AcceptCommand(1, ""));
    }

    @Test
    public void parse_acceptWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, " 1 " + comment, new AcceptCommand(1, comment));
    }

    @Test
    public void parse_invalidNumber_failure() {
        assertParseFailure(parser, "", ERROR_MESSAGE);
    }
}
