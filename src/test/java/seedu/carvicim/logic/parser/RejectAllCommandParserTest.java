package seedu.carvicim.logic.parser;

import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.RejectAllCommand;

//@@author yuhongherald
public class RejectAllCommandParserTest {
    private RejectAllCommandParser parser = new RejectAllCommandParser();

    @Test
    public void parse_acceptAllWithoutComment_success() {
        assertParseSuccess(parser, "", new RejectAllCommand(""));
    }

    @Test
    public void parse_acceptAllWithComment_success() {
        String comment = "comment";
        assertParseSuccess(parser, comment, new RejectAllCommand(comment));
    }
}
