package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;

//@@author yuhongherald
/**
 * Attempts to import all (@code JobEntry) into Servicing Manager
 */
public class ImportAllCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "importAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports job entries from from an excel file. "
            + "Parameters: FILEPATH\n"
            + "Example: " + COMMAND_WORD + "yourfile.xls";

    public static final String MESSAGE_SUCCESS = "%s has been imported, with %d job entries!";

    private final String filePath;

    public ImportAllCommand(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, filePath, entries);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        try {
            importSession.initializeSession(filePath);
        } catch (FileAccessException e) {
            e.printStackTrace();
        } catch (FileFormatException e) {
            throw new CommandException("Excel file first row headers are not defined properly. "
                    + "Type 'help' to read more.");
        }
        try {
            importSession.reviewAllRemainingJobEntries(true);
            List<Job> jobs = new ArrayList<>(importSession.getSessionData().getReviewedJobEntries());
            model.addJobs(jobs);
            importSession.closeSession();
            return new CommandResult(getMessageSuccess(jobs.size()));
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (IOException e) {
            throw new CommandException("Unable to export file. Please close the application and try again.");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportAllCommand // instanceof handles nulls
                && filePath.equals(((ImportAllCommand) other).filePath));
    }

}
