package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.SetCommand;

//@@author yuhongherald
public class SetCommandParserTest {
    private SetCommandParser parser = new SetCommandParser();

    @Test
    public void parse_twoCommandWords_success() {
        String currentWord = AddCommand.COMMAND_WORD;
        String newWord = getWord();
        String args = String.join(" ", currentWord, newWord);
        assertParseSuccess(parser, args, new SetCommand(currentWord, newWord));
    }

    @Test
    public void parse_noCommandWord_failure() {
        String currentWord = "";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetCommand.MESSAGE_USAGE);
        assertParseFailure(parser, currentWord, expectedMessage);
    }

    @Test
    public void parse_oneCommandWord_failure() {
        String currentWord = AddCommand.COMMAND_WORD;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetCommand.MESSAGE_USAGE);
        assertParseFailure(parser, currentWord, expectedMessage);
    }

    public static String getWord() {
        return "a";
    }
}
