package seedu.carvicim.logic.commands;

/**
 * Lists all persons in the carvicim book to the user.
 */
//@@author yuhongherald
public class SwitchCommand extends Command {

    public static final String COMMAND_WORD = "switch";

    public static final String MESSAGE_SUCCESS = "Switched job lists.";


    @Override
    public CommandResult execute() {
        model.switchJobView();
        model.resetJobView();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
