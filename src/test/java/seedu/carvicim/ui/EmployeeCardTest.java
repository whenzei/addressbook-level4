package seedu.carvicim.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.carvicim.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.testutil.EmployeeBuilder;

public class EmployeeCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Employee employeeWithNoTags = new EmployeeBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(employeeWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, employeeWithNoTags, 1);

        // with tags
        Employee employeeWithTags = new EmployeeBuilder().build();
        personCard = new PersonCard(employeeWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, employeeWithTags, 2);
    }

    @Test
    public void equals() {
        Employee employee = new EmployeeBuilder().build();
        PersonCard personCard = new PersonCard(employee, 0);

        // same employee, same index -> returns true
        PersonCard copy = new PersonCard(employee, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different employee, same index -> returns false
        Employee differentEmployee = new EmployeeBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentEmployee, 0)));

        // same employee, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(employee, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedEmployee} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Employee expectedEmployee, int expectedId) {
        guiRobot.pauseForHuman();

        PersonCardHandle personCardHandle = new PersonCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify employee details are displayed correctly
        assertCardDisplaysPerson(expectedEmployee, personCardHandle);
    }
}
