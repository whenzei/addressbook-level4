package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_DATERANGE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_START_DATE;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.DateRange;

//@@author richardson0694
/**
 * Archives job entries within selected date range.
 */
public class ArchiveCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives job entries within selected date range. "
            + "Parameters: "
            + PREFIX_START_DATE + "START_DATE "
            + PREFIX_END_DATE + "END_DATE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_DATE + "Mar 03 2018 "
            + PREFIX_END_DATE + "Mar 25 2018";

    public static final String MESSAGE_SUCCESS = "Archived successfully";

    private final DateRange toArchive;

    /**
     * Creates an ArchiveCommand to archive the job entries within the specified {@code DateRange}
     */
    public ArchiveCommand(DateRange dateRange) {
        requireNonNull(dateRange);
        toArchive = dateRange;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (toArchive.compareTo(toArchive.getStartDate(), toArchive.getEndDate()) > 0) {
            throw new CommandException(MESSAGE_INVALID_DATERANGE);
        }
        requireNonNull(model);
        model.archiveJob(toArchive);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ArchiveCommand // instanceof handles nulls
                && toArchive.equals(((ArchiveCommand) other).toArchive));
    }
}
