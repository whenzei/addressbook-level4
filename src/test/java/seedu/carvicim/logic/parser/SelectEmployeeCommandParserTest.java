package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.carvicim.logic.commands.SelectEmployeeCommand;

/**
 * Test scope: similar to {@code DeleteEmployeeCommandParserTest}.
 * @see DeleteEmployeeCommandParserTest
 */
public class SelectEmployeeCommandParserTest {

    private SelectEmployeeCommandParser parser = new SelectEmployeeCommandParser();

    @Test
    public void parse_validArgs_returnsSelectCommand() {
        assertParseSuccess(parser, "1", new SelectEmployeeCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectEmployeeCommand.MESSAGE_USAGE));
    }
}
