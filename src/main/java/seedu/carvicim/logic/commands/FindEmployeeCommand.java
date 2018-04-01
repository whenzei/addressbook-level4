package seedu.carvicim.logic.commands;

import seedu.carvicim.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in carvicim book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindEmployeeCommand extends Command {

    public static final String COMMAND_WORD = "finde";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public FindEmployeeCommand(NameContainsKeywordsPredicate predicate) {
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
                || (other instanceof FindEmployeeCommand // instanceof handles nulls
                && this.predicate.equals(((FindEmployeeCommand) other).predicate)); // state check
    }
}
