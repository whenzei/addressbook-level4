package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static seedu.carvicim.logic.commands.ThemeCommand.NUMBER_OF_THEMES;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_THEME;

import org.junit.Rule;
import org.junit.Test;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.ui.SetThemeRequestEvent;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.ui.testutil.EventsCollectorRule;

//@author owzhenwei
public class ThemeCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_setTheme_success() {
        assertExecutionSuccess(INDEX_FIRST_THEME);
    }

    @Test
    public void execute_setTheme_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(NUMBER_OF_THEMES + 1);
        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_THEME_INDEX);
    }

    /**
     * Executes a {@code ThemeCommand} with the given {@code index}, and checks that {@code SetThemeRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        ThemeCommand themeCommand = prepareCommand(index);

        try {
            CommandResult commandResult = themeCommand.execute();
            assertEquals(String.format(ThemeCommand.MESSAGE_THEME_CHANGE_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        SetThemeRequestEvent lastEvent = (SetThemeRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, lastEvent.getSelectedIndex());
    }

    /**
     * Executes a {@code ThemeCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        ThemeCommand themeCommand = prepareCommand(index);

        try {
            themeCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code ThemeCommand} with parameters {@code index}.
     */
    private ThemeCommand prepareCommand(Index index) {
        ThemeCommand themeCommand = new ThemeCommand(index);
        return themeCommand;
    }
}
