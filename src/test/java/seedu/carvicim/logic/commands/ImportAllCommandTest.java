package seedu.carvicim.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static seedu.carvicim.storage.session.SessionData.ERROR_MESSAGE_FILE_FORMAT;

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
public class ImportAllCommandTest extends ImportCommandTestEnv {

    private ModelIgnoreJobDates expectedModel;

    @Before
    public void setup() throws Exception {
        Employee jim = new Employee(new Name("Jim"), new Phone("87654321"), new Email("jim@gmail.com"),
                Collections.emptySet());
        Person client = new Person(new Name("JD"), new Phone("91234567"), new Email("jd@gmail.com"));
        UniqueEmployeeList uniqueEmployeeList = new UniqueEmployeeList();
        uniqueEmployeeList.add(jim);
        RemarkList excelRemarkList = new RemarkList();
        excelRemarkList.add(new Remark("Haha"));
        excelRemarkList.add(new Remark("whew"));
        Job job = new Job(client, new VehicleNumber("SXX1234X"), new JobNumber("1"), new Date(),
                uniqueEmployeeList, new Status(Status.STATUS_ONGOING), excelRemarkList);

        Employee maya = new Employee(new Name("Maya"), new Phone("87654321"), new Email("maya@gmail.com"),
                Collections.emptySet());
        Person client2 = new Person(new Name("JS"), new Phone("91234567"), new Email("js@gmail.com"));

        UniqueEmployeeList uniqueEmployeeList2 = new UniqueEmployeeList();
        uniqueEmployeeList2.add(maya);
        RemarkList excelRemarkList2 = new RemarkList();
        excelRemarkList2.add(new Remark("first"));
        excelRemarkList2.add(new Remark("second"));
        excelRemarkList2.add(new Remark("last"));
        Job job2 = new Job(client2, new VehicleNumber("SXX1234X"), new JobNumber("2"), new Date(),
                uniqueEmployeeList2, new Status(Status.STATUS_ONGOING), excelRemarkList2);

        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        jobList.add(job2);
        expectedModel = new ModelIgnoreJobDates();
        expectedModel.addJobsAndNewEmployees(jobList);
    }

    @Test
    public void equals() throws Exception {
        String filePath = "CS2103-testsheet.xlsx";
        String altFilePath = "CS2103-testsheet-corrupt.xlsx";
        ImportAllCommand importCommand1 = prepareCommand(filePath);
        ImportAllCommand importCommand1Copy = prepareCommand(filePath);
        ImportAllCommand importCommand2 = prepareCommand(altFilePath);

        // same object -> returns true
        assertTrue(importCommand1.equals(importCommand1));

        // same values -> returns true
        assertTrue(importCommand1.equals(importCommand1Copy));

        // different types -> returns false
        assertFalse(importCommand1.equals(1));

        // different filepath -> return false
        assertFalse(importCommand1.equals(importCommand2));

        // null -> return false
        assertFalse(importCommand1.equals(null));
    }

    @Test
    public void execute_importValidExcelFile_success() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(ERROR_INPUT_FILE, ERROR_IMPORTED_FILE, ERROR_OUTPUT_FILE);

        ImportAllCommand command = prepareCommand(inputPath);
        command.execute();
        prepareOutputFiles();
        assertTrue(expectedModel.equals(command.model));
        assertOutputResultFilesEqual();
    }

    @Test
    public void execute_importInvalidExcelFile_failure() throws Exception {
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(NON_EXCEL_FILE, NON_EXCEL_FILE, NON_EXCEL_OUTPUT_FILE);
        ImportAllCommand command = prepareCommand(inputPath);
        try {
            command.execute();
        } catch (CommandException e) {
            assertEquals(ERROR_MESSAGE_FILE_FORMAT, e.getMessage());
        }
    }

    /**
     * Returns ImportAllCommand with {@code filePath}, with default data
     */
    protected ImportAllCommand prepareCommand(String filePath) throws Exception {
        JobNumber.initialize(0);
        ImportAllCommand command = new ImportAllCommand(filePath);
        command.setData(new ModelManager(), new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
