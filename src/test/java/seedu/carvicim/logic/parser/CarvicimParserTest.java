package seedu.carvicim.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.carvicim.logic.commands.CommandTestUtil.ASSIGNED_EMPLOYEE_INDEX_DESC_ONE;
import static seedu.carvicim.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_VEHICLE_NUMBER_A;
import static seedu.carvicim.logic.commands.CommandTestUtil.VEHICLE_NUMBER_DESC_ONE;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_THEME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.AddEmployeeCommand;
import seedu.carvicim.logic.commands.AddJobCommand;
import seedu.carvicim.logic.commands.ClearCommand;
import seedu.carvicim.logic.commands.DeleteEmployeeCommand;
import seedu.carvicim.logic.commands.ExitCommand;
import seedu.carvicim.logic.commands.FindEmployeeCommand;
import seedu.carvicim.logic.commands.HelpCommand;
import seedu.carvicim.logic.commands.HistoryCommand;
import seedu.carvicim.logic.commands.ListEmployeeCommand;
import seedu.carvicim.logic.commands.RedoCommand;
import seedu.carvicim.logic.commands.SelectEmployeeCommand;
import seedu.carvicim.logic.commands.ThemeCommand;
import seedu.carvicim.logic.commands.UndoCommand;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.NameContainsKeywordsPredicate;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.testutil.ClientBuilder;
import seedu.carvicim.testutil.EmployeeBuilder;
import seedu.carvicim.testutil.PersonUtil;

public class CarvicimParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final CarvicimParser parser = new CarvicimParser();

    @Test
    public void parseCommand_add() throws Exception {
        Employee employee = new EmployeeBuilder().build();
        AddEmployeeCommand command = (AddEmployeeCommand) parser.parseCommand(PersonUtil.getAddCommand(employee));
        assertEquals(new AddEmployeeCommand(employee), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteEmployeeCommand command = (DeleteEmployeeCommand) parser.parseCommand(
                DeleteEmployeeCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteEmployeeCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindEmployeeCommand command = (FindEmployeeCommand) parser.parseCommand(
                FindEmployeeCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindEmployeeCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListEmployeeCommand.COMMAND_WORD) instanceof ListEmployeeCommand);
        assertTrue(parser.parseCommand(ListEmployeeCommand.COMMAND_WORD + " 3") instanceof ListEmployeeCommand);
    }

    @Test
    public void parseCommand_selectEmployee() throws Exception {
        SelectEmployeeCommand command = (SelectEmployeeCommand) parser.parseCommand(
                SelectEmployeeCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectEmployeeCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_theme() throws Exception {
        ThemeCommand command = (ThemeCommand) parser.parseCommand(ThemeCommand.COMMAND_WORD + " "
                + INDEX_FIRST_THEME.getOneBased());
        assertEquals(new ThemeCommand(INDEX_FIRST_THEME), command);
    }

    /* Requires bug fix
    @Test
    public void parseCommand_remark() throws Exception {
        RemarkCommand command = (RemarkCommand) parser.parseCommand(RemarkCommand.COMMAND_WORD + " "
                + JOB_NUMBER_DESC_A + REMARK_DESC);
        Remark remark = new Remark(VALID_REMARK);
        JobNumber jobNumber = new JobNumber(VALID_JOB_NUMBER_ONE);

        assertEquals(new RemarkCommand(remark, jobNumber), command);
    }
    */

    @Test
    public void parseCommand_addJob() throws Exception {
        AddJobCommand command = (AddJobCommand) parser.parseCommand(AddJobCommand.COMMAND_WORD + " "
                + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + VEHICLE_NUMBER_DESC_ONE
                + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE);
        Person client = new ClientBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).build();
        VehicleNumber vehicleNumber = new VehicleNumber(VALID_VEHICLE_NUMBER_A);
        ArrayList<Index> targetIndices = new ArrayList<>();
        targetIndices.add(Index.fromOneBased(1));

        assertEquals(new AddJobCommand(client, vehicleNumber, targetIndices), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }

}
