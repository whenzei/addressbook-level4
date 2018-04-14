package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.carvicim.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.carvicim.model.Model.PREDICATE_SHOW_ONGOING_JOBS;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.SessionData;

/**
 * Represents a command which can be undone and redone.
 */
public abstract class UndoableCommand extends Command {
    protected String prevJobNumber;

    private ReadOnlyCarvicim previousAddressBook;
    private CommandWords previousCommandWords;
    private SessionData sessionData;
    private boolean isViewingImportedJobs;
    private String currentJobNumber;


    protected abstract CommandResult executeUndoableCommand() throws CommandException;

    /**
     * Stores the current state of {@code model#carvicim}.
     */
    private void saveAddressBookSnapshot() throws CommandException {
        requireNonNull(model);
        this.previousAddressBook = new Carvicim(model.getCarvicim());
        this.previousCommandWords = new CommandWords(model.getCommandWords());
        this.sessionData = ImportSession.getInstance().getSessionData().createCopy();
        isViewingImportedJobs = model.isViewingImportedJobs();
        currentJobNumber = JobNumber.getNextJobNumber();
    }

    /**
     * This method is called before the execution of {@code UndoableCommand}.
     * {@code UndoableCommand}s that require this preprocessing step should override this method.
     */
    protected void preprocessUndoableCommand() throws CommandException {}

    /**
     * Reverts the Carvicim to the state before this command
     * was executed and updates the filtered employee list to
     * show all persons.
     */
    protected final void undo() throws CommandException {
        JobNumber.setNextJobNumber(currentJobNumber);
        requireAllNonNull(model, previousAddressBook);
        model.resetData(previousAddressBook, previousCommandWords);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredJobList(PREDICATE_SHOW_ONGOING_JOBS);
        ImportSession.getInstance().setSessionData(sessionData);
        sessionData = sessionData.createCopy();
        if (model.isViewingImportedJobs() != isViewingImportedJobs) {
            model.switchJobView();
        }
        model.resetJobView();
        model.resetJobDisplayPanel();

        if (prevJobNumber != null) {
            JobNumber.setNextJobNumber(prevJobNumber);
        }

    }

    /**
     * Executes the command and updates the filtered employee
     * list to show all persons.
     */
    protected final void redo() throws CommandException {
        requireNonNull(model);
        executeUndoableCommand();

        if (prevJobNumber != null) {
            JobNumber.setNextJobNumber(Integer.valueOf(prevJobNumber) + 1 + "");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    public void releaseResources() {
        sessionData.freeResources();
    }

    @Override
    public final CommandResult execute() throws CommandException {
        saveAddressBookSnapshot();
        preprocessUndoableCommand();
        return executeUndoableCommand();
    }
}
