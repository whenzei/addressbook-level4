package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.carvicim.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.carvicim.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.carvicim.testutil.TypicalEmployees.BENSON;
import static seedu.carvicim.testutil.TypicalEmployees.CARL;
import static seedu.carvicim.testutil.TypicalEmployees.DANIEL;
import static seedu.carvicim.testutil.TypicalEmployees.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.DeleteEmployeeCommand;
import seedu.carvicim.logic.commands.FindEmployeeCommand;
import seedu.carvicim.logic.commands.RedoCommand;
import seedu.carvicim.logic.commands.UndoCommand;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.tag.Tag;

public class FindEmployeeCommandSystemTest extends CarvicimSystemTest {

    @Test
    public void find() {
        /* Case: find multiple persons in carvicim book, command with leading spaces and trailing spaces
         * -> 2 persons found
         */
        String command = "   " + FindEmployeeCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);

        /* Case: repeat previous find command where employee list is displaying the persons we are finding
         * -> 2 persons found
         */
        command = FindEmployeeCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel);

        /* Case: find employee where employee list is not displaying the employee we are finding -> 1 employee found */
        command = FindEmployeeCommand.COMMAND_WORD + " Carl";
        ModelHelper.setFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);

        /* Case: find multiple persons in carvicim book, 2 keywords -> 2 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " Benson Daniel";
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);

        /* Case: find multiple persons in carvicim book, 2 keywords in reversed order -> 2 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " Daniel Benson";
        assertCommandSuccess(command, expectedModel);

        /* Case: find multiple persons in carvicim book, 2 keywords with 1 repeat -> 2 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " Daniel Benson Daniel";
        assertCommandSuccess(command, expectedModel);

        /* Case: find multiple persons in carvicim book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindEmployeeCommand.COMMAND_WORD + " Daniel Benson NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same persons in carvicim book after deleting 1 of them -> 1 employee found */
        executeCommand(DeleteEmployeeCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getCarvicim().getEmployeeList().contains(BENSON));
        command = FindEmployeeCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);

        /* Case: find employee in carvicim book, keyword is same as name but of different case -> 1 employee found */
        command = FindEmployeeCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);

        /* Case: find employee in carvicim book, keyword is substring of name -> 0 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " Mei";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);

        /* Case: find employee in carvicim book, name is substring of keyword -> 0 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " Meiers";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);

        /* Case: find employee not in carvicim book -> 0 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);

        /* Case: find phone number of employee in carvicim book -> 0 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " " + DANIEL.getPhone().value;
        assertCommandSuccess(command, expectedModel);

        /* Case: find email of employee in carvicim book -> 0 persons found */
        command = FindEmployeeCommand.COMMAND_WORD + " " + DANIEL.getEmail().value;
        assertCommandSuccess(command, expectedModel);

        /* Case: find tags of employee in carvicim book -> 0 persons found */
        List<Tag> tags = new ArrayList<>(DANIEL.getTags());
        command = FindEmployeeCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);

        /* Case: find while a employee is selected -> selected card deselected */
        showAllPersons();
        selectPerson(Index.fromOneBased(1));
        assertFalse(getPersonListPanel().getHandleToSelectedCard().getName().equals(DANIEL.getName().fullName));
        command = FindEmployeeCommand.COMMAND_WORD + " Daniel";
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);

        /* Case: find employee in empty carvicim book -> 0 persons found */
        deleteAllPersons();
        command = FindEmployeeCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);

        /* Case: mixed case command word -> rejected */
        command = "FiNde Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_PERSONS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see CarvicimSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpectedError(command, expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
