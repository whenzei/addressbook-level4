package seedu.carvicim.logic.commands;

import seedu.carvicim.model.person.TagContainsKeywordsPredicate;

//@@author charmaineleehc
/**
 * Finds and lists all employees in carvicim who has been tagged by any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindByTagCommand extends Command {

    public static final String COMMAND_WORD = "findt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all employees who has been tagged by any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " mechanic technician";

    private final TagContainsKeywordsPredicate predicate;

    public FindByTagCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindByTagCommand // instanceof handles nulls
                && this.predicate.equals(((FindByTagCommand) other).predicate)); // state check
    }
}
