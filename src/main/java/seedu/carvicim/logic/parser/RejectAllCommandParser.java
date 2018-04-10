package seedu.carvicim.logic.parser;

import seedu.carvicim.logic.commands.RejectAllCommand;


//@@author yuhongherald

/**
 * Parses input arguments and creates a new RejectAllCommand object
 */
public class RejectAllCommandParser implements Parser<RejectAllCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the RejectAllCommand
     * and returns a RejectAllCommand object for execution.
     */
    public RejectAllCommand parse(String args) {
        String comment = args.trim();
        return new RejectAllCommand(comment);
    }

}
