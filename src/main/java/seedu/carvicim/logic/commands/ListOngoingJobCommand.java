package seedu.carvicim.logic.commands;

//@@author whenzei
/**
 * Lists all ongoing job in CarviciM
 */
public class ListOngoingJobCommand extends Command {

    public static final String COMMAND_WORD = "listoj";

    public static final String MESSAGE_SUCCESS = "Listed all ongoing jobs";

    @Override
    public CommandResult execute() {
        model.showOngoingJobs();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
