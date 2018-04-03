package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.exceptions.FileAccessException;
import seedu.carvicim.storage.session.exceptions.FileFormatException;

//@@author yuhongherald

/**
 * Attempts to import specified file into Servicing Manager
 */
public class ImportCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports an excel file for reviewing. "
            + "Parameters: FILEPATH\n"
            + "Example: " + COMMAND_WORD + "yourfile.xls";

    public static final String MESSAGE_SUCCESS = "%s has been imported, with %d job entries!";

    private final String filePath;

    public ImportCommand(String filePath) {
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
            throw new CommandException(e.getMessage());
        } catch (FileFormatException e) {
            throw new CommandException("Excel file first row headers are not defined properly. "
                    + "Type 'help' to read more.");
        }

        if (!model.isViewingImportedJobs()) {
            model.switchJobView();
        }
        model.resetJobView();
        return new CommandResult(getMessageSuccess(importSession.getSessionData()
                .getUnreviewedJobEntries().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && filePath.equals(((ImportCommand) other).filePath));
    }

}
