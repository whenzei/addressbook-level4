package seedu.carvicim.testutil;

import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_TAG_MECHANIC;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_TAG_TECHNICIAN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;

/**
 * A utility class containing a list of {@code Employee} objects to be used in tests.
 */
public class TypicalEmployees {

    public static final Employee ALICE = new EmployeeBuilder().withName("Alice Pauline")
            .withPhone("85355255").withEmail("alice@example.com").withTags("mechanic").build();
    public static final Employee BENSON = new EmployeeBuilder().withName("Benson Meier")
            .withEmail("johnd@example.com").withPhone("98765432").withTags("technician", "mechanic").build();
    public static final Employee CARL = new EmployeeBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").build();
    public static final Employee DANIEL = new EmployeeBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").build();
    public static final Employee ELLE = new EmployeeBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").build();
    public static final Employee FIONA = new EmployeeBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").build();
    public static final Employee GEORGE = new EmployeeBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").build();

    // Manually added
    public static final Employee HOON = new EmployeeBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").build();
    public static final Employee IDA = new EmployeeBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").build();

    // Manually added - Employee's details found in {@code CommandTestUtil}
    public static final Employee AMY = new EmployeeBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_TECHNICIAN).build();
    public static final Employee BOB = new EmployeeBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_MECHANIC, VALID_TAG_TECHNICIAN)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalEmployees() {} // prevents instantiation

    /**
     * Returns an {@code Carvicim} with all the typical persons.
     */
    public static Carvicim getTypicalCarvicim() {
        Carvicim ab = new Carvicim();
        for (Employee employee : getTypicalEmployees()) {
            try {
                ab.addEmployee(employee);
            } catch (DuplicateEmployeeException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    //@@author richardson0694
    /**
     * Returns an {@code Carvicim} with all the typical employees' name non alphabetically.
     */
    public static Carvicim getCarvicimNonAlphabetically() {
        Carvicim ab = new Carvicim();
        for (Employee employee : getTypicalEmployeesNonAlphabetically()) {
            try {
                ab.addEmployee(employee);
            } catch (DuplicateEmployeeException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    //@@author
    public static List<Employee> getTypicalEmployees() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    public static List<Employee> getTypicalEmployeesNonAlphabetically() {
        return new ArrayList<>(Arrays.asList(GEORGE, FIONA, ELLE, DANIEL, CARL, BENSON, ALICE));
    }
}
