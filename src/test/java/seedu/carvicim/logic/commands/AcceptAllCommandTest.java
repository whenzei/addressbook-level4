package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.carvicim.commons.core.Messages.MESSAGE_NO_JOB_ENTRIES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class AcceptAllCommandTest extends ImportCommandTestEnv {
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
        RemarkList excelRemarkList1 = new RemarkList();
        excelRemarkList1.add(new Remark("Haha"));
        excelRemarkList1.add(new Remark("whew"));

        Employee maya = new Employee(new Name("Maya"), new Phone("87654321"), new Email("maya@gmail.com"),
                Collections.emptySet());
        Person client2 = new Person(new Name("JS"), new Phone("91234567"), new Email("js@gmail.com"));

        UniqueEmployeeList uniqueEmployeeList2 = new UniqueEmployeeList();
        uniqueEmployeeList2.add(maya);
        RemarkList excelRemarkList2 = new RemarkList();
        excelRemarkList2.add(new Remark("first"));
        excelRemarkList2.add(new Remark("second"));
        excelRemarkList2.add(new Remark("last"));

        Job job1 = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), excelRemarkList1);
        Job job2 = new Job(client2, new VehicleNumber("SXX1234X"), new JobNumber("2"), new Date(),
                uniqueEmployeeList2, new Status(Status.STATUS_ONGOING), excelRemarkList2);
        List<Job> jobList = new ArrayList<>();
        jobList.add(job1);
        jobList.add(job2);

        comment = new Remark("good job!");
        RemarkList remarkList1 = new RemarkList();
        RemarkList remarkList2 = new RemarkList();
        remarkList1.add(new Remark("Haha"));
        remarkList1.add(new Remark("whew"));
        remarkList1.add(comment);
        remarkList2.add(new Remark("first"));
        remarkList2.add(new Remark("second"));
        remarkList2.add(new Remark("last"));
        remarkList2.add(comment);

        Job jobWithComment1 = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), remarkList1);
        Job jobWithComment2 = new Job(client2, new VehicleNumber("SXX1234X"), new JobNumber("2"), new Date(),
                uniqueEmployeeList2, new Status(Status.STATUS_ONGOING), remarkList2);
        List<Job> jobListWithComment = new ArrayList<>();
        jobListWithComment.add(jobWithComment1);
        jobListWithComment.add(jobWithComment2);

        expectedModelWithoutComment = new ModelIgnoreJobDates();
        expectedModelWithoutComment.addJobsAndNewEmployees(jobList);
        expectedModelWithComment = new ModelIgnoreJobDates();
        expectedModelWithComment.addJobsAndNewEmployees(jobListWithComment);
    }

    @Test
    public void equals() throws Exception {
        String comment = "comment";
        AcceptAllCommand acceptAllCommand1 = prepareCommand(comment);
        AcceptAllCommand acceptAllCommand1Copy = prepareCommand(comment);
        AcceptAllCommand acceptAllCommand2 = prepareCommand("");

        // same object -> returns true
        assertTrue(acceptAllCommand1.equals(acceptAllCommand1));

        // same values -> returns true
        assertTrue(acceptAllCommand1.equals(acceptAllCommand1Copy));

        // different types -> returns false
        assertFalse(acceptAllCommand1.equals(1));

        // different comments -> return false
        assertFalse(acceptAllCommand1.equals(acceptAllCommand2));

        // null -> return false
        assertFalse(acceptAllCommand1.equals(null));
    }

    @Test
    public void execute_acceptAllWithoutComment_success() throws Exception {
        prepareInputFiles();
        AcceptAllCommand command = prepareCommand("");
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModelWithoutComment.equals(command.model));
        assertOutputResultFilesEqual();
        commandCleanup(command);
    }

    @Test
    public void execute_acceptAllWithoutImport_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        AcceptAllCommand command = prepareCommand(comment.toString());
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(MESSAGE_NO_JOB_ENTRIES, e.getMessage());
        }
        commandCleanup(command);
    }

    /**
     * Returns AcceptAllCommand with {@code comments}, with default data
     */
    protected AcceptAllCommand prepareCommand(String comments) throws Exception {
        JobNumber.initialize(0);
        AcceptAllCommand command = new AcceptAllCommand(comments);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
