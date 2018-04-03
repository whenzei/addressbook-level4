package seedu.carvicim.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.UnitializedException;

//@@author yuhongherald

/**
 * Attempts to write reviewed jobs with feedback into an excel file
 */
public class SaveCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "save";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Saves your reviewed job entries as an excel file.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Your reviewed job entries have been saved to %s!";

    public String getMessageSuccess(String filePath) {
        return String.format(MESSAGE_SUCCESS, filePath);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        String message;
        if (!importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("Please review all remaining job entries before saving!");
        }
        try {
            importSession.reviewAllRemainingJobEntries(true);
            List<Job> jobs = new ArrayList<>(importSession.getSessionData().getReviewedJobEntries());
            model.addJobs(jobs);
            message = importSession.closeSession();
        } catch (IOException e) {
            throw new CommandException("Unable to export file. Please close the application and try again.");
        } catch (UnitializedException e) {
            throw new CommandException(e.getMessage());
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException((e.getMessage()));
        }
        ObservableList<Job> jobList = model.getFilteredJobList();
        if (model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess(message));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SaveCommand); // instanceof handles nulls
    }

}
