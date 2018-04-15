package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.carvicim.logic.commands.FindByTagCommand;
import seedu.carvicim.model.person.TagContainsKeywordsPredicate;

//@@author charmaineleehc
public class FindByTagCommandParserTest {
    private FindByTagCommandParser parser = new FindByTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindByTagCommand expectedFindByTagCommand =
                new FindByTagCommand(new TagContainsKeywordsPredicate(Arrays.asList("technician", "mechanic")));
        assertParseSuccess(parser, "technician mechanic", expectedFindByTagCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n technician \n \t mechanic  \t", expectedFindByTagCommand);
    }

}
