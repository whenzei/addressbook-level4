package seedu.carvicim.logic.parser;

import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.carvicim.logic.parser.SetCommandParser.ERROR_MESSAGE;

import org.junit.Test;

import seedu.carvicim.logic.commands.AddEmployeeCommand;
import seedu.carvicim.logic.commands.SetCommand;

//@@author yuhongherald
public class SetCommandParserTest {
    private SetCommandParser parser = new SetCommandParser();

    @Test
    public void parse_twoCommandWords_success() {
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        String newWord = getWord();
        String args = String.join(" ", currentWord, newWord);
        assertParseSuccess(parser, args, new SetCommand(currentWord, newWord));
    }

    @Test
    public void parse_noCommandWord_failure() {
        String currentWord = "";
        assertParseFailure(parser, currentWord, ERROR_MESSAGE);
    }

    @Test
    public void parse_oneCommandWord_failure() {
        String currentWord = AddEmployeeCommand.COMMAND_WORD;
        assertParseFailure(parser, currentWord, ERROR_MESSAGE);
    }

    public static String getWord() {
        return "a";
    }
}
