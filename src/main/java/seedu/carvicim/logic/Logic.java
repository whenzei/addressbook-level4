package seedu.carvicim.logic;

import javafx.collections.ObservableList;
import seedu.carvicim.logic.commands.CommandResult;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.person.Employee;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** releases resources in undoRedoStack */
    void cleanUndoRedoStack();

    /** returns the command words mapped as text representation */
    String getCommandWordMapping();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Employee> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered list of jobs */
    ObservableList<Job> getFilteredJobList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();
}
