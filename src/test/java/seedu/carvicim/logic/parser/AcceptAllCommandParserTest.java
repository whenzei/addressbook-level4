package seedu.carvicim.logic.parser;

import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.AcceptAllCommand;

//@@author yuhongherald
public class AcceptAllCommandParserTest {
    private AcceptAllCommandParser parser = new AcceptAllCommandParser();

    @Test
    public void parse_acceptAllWithoutComment_success() {
        assertParseSuccess(parser, "", new AcceptAllCommand(""));
    }

    @Test
    public void parse_acceptAllWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, comment, new AcceptAllCommand(comment));
    }
}
