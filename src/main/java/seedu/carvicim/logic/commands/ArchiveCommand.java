package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_START_DATE;

import seedu.carvicim.model.job.DateRange;

//@@author richardson0694
/**
 * Archives job entries within selected date range.
 */
public class ArchiveCommand extends Command {
    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives job entries within selected date range. "
            + "Parameters: "
            + PREFIX_START_DATE + "DATE "
            + PREFIX_END_DATE + "DATE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_DATE + "01/03/2018 "
            + PREFIX_END_DATE + "25/03/2018 ";

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
    public CommandResult execute() {
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
