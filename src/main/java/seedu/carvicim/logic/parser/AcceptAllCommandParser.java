package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.util.AppUtil.checkArgument;
import static seedu.carvicim.model.remark.Remark.MESSAGE_REMARKS_CONSTRAINTS;
import static seedu.carvicim.model.remark.Remark.isValidRemark;

import seedu.carvicim.logic.commands.AcceptAllCommand;

//@@author yuhongherald

/**
 * Parses input arguments and creates a new AcceptAllCommand object
 */
public class AcceptAllCommandParser implements Parser<AcceptAllCommand> {

    /**
     * Parses the given {@code String} of arg
     * uments in the context of the AcceptAllCommand
     * and returns an AcceptAllCommand object for execution.
     */
    public AcceptAllCommand parse(String args) {
        String comment = args.trim();
        if (!comment.equals("")) {
            checkArgument(isValidRemark(comment), MESSAGE_REMARKS_CONSTRAINTS);
        }
        return new AcceptAllCommand(comment);
    }

}
