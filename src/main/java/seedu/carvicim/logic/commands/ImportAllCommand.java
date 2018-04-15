package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;
import seedu.carvicim.storage.session.exceptions.InvalidDataException;

//@@author yuhongherald
/**
 * Attempts to import all {@code JobEntry} into Servicing Manager
 */
public class ImportAllCommand extends Command {

    public static final String COMMAND_WORD = "importAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports job entries from from an excel file. "
            + "Parameters: FILEPATH\n"
            + "Example: " + COMMAND_WORD + " yourfile.xls";

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
    public CommandResult execute() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        try {
            importSession.initializeSession(filePath);
        } catch (FileAccessException | FileFormatException | InvalidDataException e) {
            throw new CommandException(e.getMessage());
        }
        List<Job> jobs = new ArrayList<>(importSession.getSessionData()
                .reviewAllRemainingJobEntries(true, ""));
        model.addJobsAndNewEmployees(jobs);
        importSession.closeSession();
        return new CommandResult(getMessageSuccess(jobs.size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportAllCommand // instanceof handles nulls
                && filePath.equals(((ImportAllCommand) other).filePath));
    }

}
