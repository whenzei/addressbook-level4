package seedu.carvicim.logic.commands;

import seedu.carvicim.model.job.JobDetailsContainKeyWordsPredicate;

/**
 * Finds and lists all persons in carvicim book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindJobCommand extends Command {
    public static final String COMMAND_WORD = "findj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Find all jobs whose client name, date"
            + ", vehicle and job number contain any of the specified keywords (case-sensitive) and displays"
            + " them as a list.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " APR John";

    private final JobDetailsContainKeyWordsPredicate predicate;

    public FindJobCommand(JobDetailsContainKeyWordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredJobList(predicate);
        return new CommandResult(getMessageForJobListShownSummary(model.getFilteredJobList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindJobCommand // instanceof handles nulls
                && this.predicate.equals(((FindJobCommand) other).predicate)); // state check
    }
}
