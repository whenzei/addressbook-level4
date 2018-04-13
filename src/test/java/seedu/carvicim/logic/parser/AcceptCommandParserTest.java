package seedu.carvicim.logic.parser;

import org.junit.Test;

import seedu.carvicim.storage.ImportSessionTest;


//@@author yuhongherald
public class AcceptCommandParserTest extends ImportSessionTest {
    private AcceptCommandParser parser = new AcceptCommandParser();

    @Test
    public void parse_acceptWithoutComment_success() throws Exception {
        setup(ERROR_INPUT_FILE, ERROR_RESULT_FILE, ERROR_OUTPUT_FILE);
        assertOutputResultEqual();
        cleanup();
    }

    @Test
    public void parse_acceptWithComment_success() {
    }

    @Test
    public void parse_acceptIndexOutOfBounds_failure() {
    }
}
