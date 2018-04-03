package seedu.carvicim.logic.commands;

/**
 * Lists all jobs in the carvicim book to the user.
 */
//@@author yuhongherald
public class ListJobCommand extends Command {

    public static final String COMMAND_WORD = "listj";

    public static final String MESSAGE_SUCCESS = "Listed all jobs";


    @Override
    public CommandResult execute() {
        model.resetJobView();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
