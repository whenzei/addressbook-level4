package seedu.carvicim.logic.commands;

import java.io.File;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.storage.ImportSessionTestEnv;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

/**
 * Used to setup the environment for testing commands that uses sessionData in importSession.
 */
public abstract class ImportCommandTestEnv extends ImportSessionTestEnv {
    /**
     * Model used to check if expected model contains the jobs and employees that are added. Ignores date
     * as it is dependent on time of command execution.
     */
    protected class ModelIgnoreJobDates extends ModelManager {
        public ModelIgnoreJobDates() {
            super();
        }

        public ModelIgnoreJobDates(Employee employee, Job job) throws DuplicateEmployeeException {
            super();
            addPerson(employee);
            addJob(job);
        }

        @Override
        public boolean equals(Object obj) {
            // short circuit if same object
            if (obj == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(obj instanceof ModelManager)) {
                return false;
            }

            // state check
            ModelManager other = (ModelManager) obj;
            ObservableList<Job> jobList = getFilteredJobList();
            ObservableList<Job> otherJobList = other.getFilteredJobList();
            return isJobListsEqualIgnoreDates(jobList, otherJobList)
                    && getCarvicim().equals(other.getCarvicim())
                    && getFilteredPersonList().equals(other.getFilteredPersonList())
                    && getCommandWords().equals(other.getCommandWords());
        }

        /**
         * Checks if the job entries in both lists are equal, ignoring their date field
         */
        private boolean isJobListsEqualIgnoreDates(List<Job> jobList, List<Job> otherJobList) {
            if (jobList.size() != otherJobList.size()) {
                return false;
            }
            Job job;
            Job otherJob;
            for (int i = 0; i < jobList.size(); i++) {
                job = jobList.get(i);
                otherJob = otherJobList.get(i);
                if (job != otherJob && job == null || otherJob == null) {
                    return false;
                }
                if (!(otherJob.getClient().equals(job.getClient())
                        && otherJob.getVehicleNumber().equals(job.getVehicleNumber())
                        && otherJob.getJobNumber().equals(job.getJobNumber())
                        && otherJob.getAssignedEmployeesAsSet().equals(job.getAssignedEmployeesAsSet())
                        && otherJob.getStatus().equals(job.getStatus())
                        && otherJob.getRemarkList().equals(job.getRemarkList()))) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Clears old -comment files and initializes sessionData with file at (@code inputPath)
     */
    protected void prepareInputFiles() throws Exception {
        ImportSession.getInstance().getSessionData().freeResources();
        ImportSession.getInstance().setSessionData(new SessionData());
        setup(ERROR_INPUT_FILE, ERROR_RESULT_FILE, ERROR_OUTPUT_FILE);
        ImportSession.getInstance().initializeSession(inputPath);
    }

    /**
     * Creates output files using paths initialized in setup
     */
    protected void prepareOutputFiles() {
        ClassLoader classLoader = getClass().getClassLoader();
        outputPath = classLoader.getResource(expectedOutputPath).getPath();
        outputFilePath = outputPath;
        testFile = new File(resultPath);
        outputFile = new File(outputFilePath);
        expectedOutputFile = new File(outputPath);
    }

    /**
     * Attempts to release all the resources used for the test, okay to have constant sized residue.
     */
    protected void commandCleanup(UndoableCommand command) throws Exception {
        command.releaseResources();
        ImportSession.getInstance().getSessionData().freeResources();
        ImportSession.cleanCache();
        cleanup();
    }
}
