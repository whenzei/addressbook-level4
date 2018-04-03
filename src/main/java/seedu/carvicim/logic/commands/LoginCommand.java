package seedu.carvicim.logic.commands;

import java.io.IOException;

import seedu.carvicim.commons.GmailAuthenticator;

//@@author charmaineleehc
/**
 * Directs user to the login page of Gmail for user to log in.
 */
public class LoginCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "login";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Logs in user into Gmail account.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "You have successfully logged into your Gmail account!";

    private boolean isLoggedIn;

    @Override
    public CommandResult executeUndoableCommand() {
        try {
            new GmailAuthenticator();
        } catch (IOException ioe) {
            System.exit(1);
        }

        isLoggedIn = true;
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
