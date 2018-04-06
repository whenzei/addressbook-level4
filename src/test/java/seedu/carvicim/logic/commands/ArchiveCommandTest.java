package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalCarvicimWithJobs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.carvicim.commons.core.Messages;
import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.job.DateRange;
import seedu.carvicim.testutil.DateRangeBuilder;

//@@author richardson0694
public class ArchiveCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    @Test
    public void constructor_nullDateRange_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ArchiveCommand(null);
    }

    @Test
    public void executeSuccess() throws CommandException {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        DateRange dateRange = new DateRangeBuilder().build();
        ArchiveCommand archiveCommand = new ArchiveCommand(dateRange);
        archiveCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        CommandResult commandResult = archiveCommand.execute();
        assertEquals(ArchiveCommand.MESSAGE_SUCCESS, commandResult.feedbackToUser);
    }

    @Test
    public void executeUnsuccess_invalidDateRange() throws Exception {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        DateRange dateRange = new DateRangeBuilder().withDateRange("Mar 25 2018", "Mar 03 2018").build();
        ArchiveCommand archiveCommand = new ArchiveCommand(dateRange);
        archiveCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_DATERANGE);
        archiveCommand.execute();
    }

    @Test
    public void executeUnsuccess_noJobs() throws Exception {
        model = new ModelManager(getTypicalCarvicimWithJobs(), new UserPrefs());
        DateRange dateRange = new DateRangeBuilder().withDateRange("Jan 01 2018", "Jan 01 2018").build();
        ArchiveCommand archiveCommand = new ArchiveCommand(dateRange);
        archiveCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        CommandResult commandResult = archiveCommand.execute();
        assertEquals(ArchiveCommand.MESSAGE_UNSUCCESS, commandResult.feedbackToUser);
    }

}
