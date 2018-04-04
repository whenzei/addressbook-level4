package seedu.carvicim.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.carvicim.model.job.JobList;

//@@author richardson0694
/**
 * Analyse job entries
 */
public class AnalyseCommand extends Command {

    public static final String COMMAND_WORD = "analyse";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Analyse job entries within the month.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Result: %1$s";

    private JobList toAnalyse;

    /**
     * Creates an ArchiveCommand to archive the job entries within the specified {@code DateRange}
     */
    public AnalyseCommand() {
        toAnalyse = new JobList();
    }

    @Override
    public CommandResult execute() {
        requireNonNull(model);
        toAnalyse = model.analyseJob(toAnalyse);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAnalyse.getAnalyseResult()));
    }

}
