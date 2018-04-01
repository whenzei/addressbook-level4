package seedu.carvicim.testutil;

import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.carvicim.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.carvicim.logic.commands.AddEmployeeCommand;
import seedu.carvicim.model.person.Employee;

/**
 * A utility class for Employee.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code employee}.
     */
    public static String getAddCommand(Employee employee) {
        return AddEmployeeCommand.COMMAND_WORD + " " + getPersonDetails(employee);
    }

    /**
     * Returns the part of command string for the given {@code employee}'s details.
     */
    public static String getPersonDetails(Employee employee) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + employee.getName().fullName + " ");
        sb.append(PREFIX_PHONE + employee.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + employee.getEmail().value + " ");
        employee.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
