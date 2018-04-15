package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.carvicim.commons.core.Messages.MESSAGE_NO_JOB_ENTRIES;
import static seedu.carvicim.storage.session.SessionData.ERROR_MESSAGE_INVALID_JOB_INDEX;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import seedu.carvicim.logic.CommandHistory;
import seedu.carvicim.logic.UndoRedoStack;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.Status;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.model.remark.RemarkList;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

//@@author yuhongherald
public class AcceptCommandTest extends ImportCommandTestEnv {
    private Remark comment;

    private ModelIgnoreJobDates expectedModelWithoutComment;
    private ModelIgnoreJobDates expectedModelWithComment;

    @Before
    public void setup() throws Exception {
        Employee jim = new Employee(new Name("Jim"), new Phone("87654321"), new Email("jim@gmail.com"),
                Collections.emptySet());
        Person client = new Person(new Name("JD"), new Phone("91234567"), new Email("jd@gmail.com"));
        UniqueEmployeeList uniqueEmployeeList = new UniqueEmployeeList();
        uniqueEmployeeList.add(jim);
        Remark existingExcelRemark = new Remark("Haha");
        Remark existingExcelRemark2 = new Remark("whew");
        RemarkList excelRemarkList = new RemarkList();
        excelRemarkList.add(existingExcelRemark);
        excelRemarkList.add(existingExcelRemark2);
        Job job = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), excelRemarkList);
        comment = new Remark("good job!");
        RemarkList remarkList = new RemarkList();
        remarkList.add(existingExcelRemark);
        remarkList.add(existingExcelRemark2);
        remarkList.add(comment);
        Job jobWithComment = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), remarkList);
        expectedModelWithoutComment = new ModelIgnoreJobDates(jim, job);
        expectedModelWithComment = new ModelIgnoreJobDates(jim, jobWithComment);
    }

    @Test
    public void equals() throws Exception {
        String comment = "comment";
        AcceptCommand acceptCommand1 = prepareCommand(1, comment);
        AcceptCommand acceptCommand1Copy = prepareCommand(1, comment);
        AcceptCommand acceptCommand2 = prepareCommand(2, comment);
        AcceptCommand acceptCommand3 = prepareCommand(1, "");

        // same object -> returns true
        assertTrue(acceptCommand1.equals(acceptCommand1));

        // same values -> returns true
        assertTrue(acceptCommand1.equals(acceptCommand1Copy));

        // different types -> returns false
        assertFalse(acceptCommand1.equals(1));

        // different job index -> return false
        assertFalse(acceptCommand1.equals(acceptCommand2));

        // different comments -> return false
        assertFalse(acceptCommand1.equals(acceptCommand3));

        // null -> return false
        assertFalse(acceptCommand1.equals(null));
    }

    @Test
    public void execute_acceptWithoutComment_success() throws Exception {
        prepareInputFiles();
        AcceptCommand command = prepareCommand(1, "");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModelWithoutComment.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_acceptWithComment_success() throws Exception {
        prepareInputFiles();
        AcceptCommand command = prepareCommand(1, comment.toString());
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModelWithComment.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_acceptOutOfBounds_failure() throws Exception {
        prepareInputFiles();
        AcceptCommand command = prepareCommand(3, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_INVALID_JOB_INDEX, e.getMessage());
        }
        commandCleanup(command);
    }

    @Test
    public void execute_acceptWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        AcceptCommand command = prepareCommand(1, comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns AcceptCommand with {@code jobIndex} and {@code comments}, with default data
     */
    protected AcceptCommand prepareCommand(int jobIndex, String comments) throws Exception {
        JobNumber.initialize(0);
        AcceptCommand command = new AcceptCommand(jobIndex, comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
