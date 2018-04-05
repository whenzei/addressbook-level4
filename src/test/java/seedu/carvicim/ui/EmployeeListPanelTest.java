package seedu.carvicim.ui;

import static org.junit.Assert.assertEquals;
import static seedu.carvicim.testutil.EventsUtil.postNow;
import static seedu.carvicim.testutil.TypicalEmployees.getTypicalEmployees;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.carvicim.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.carvicim.ui.testutil.GuiTestAssert.assertCardEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.carvicim.commons.events.ui.JumpToEmployeeListRequestEvent;
import seedu.carvicim.model.person.Employee;

public class EmployeeListPanelTest extends GuiUnitTest {
    private static final ObservableList<Employee> TYPICAL_EMPLOYEES =
            FXCollections.observableList(getTypicalEmployees());

    private static final JumpToEmployeeListRequestEvent JUMP_TO_SECOND_EVENT =
            new JumpToEmployeeListRequestEvent(INDEX_SECOND_PERSON);

    private PersonListPanelHandle personListPanelHandle;

    @Before
    public void setUp() {
        PersonListPanel personListPanel = new PersonListPanel(TYPICAL_EMPLOYEES);
        uiPartRule.setUiPart(personListPanel);

        personListPanelHandle = new PersonListPanelHandle(getChildNode(personListPanel.getRoot(),
                PersonListPanelHandle.PERSON_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_EMPLOYEES.size(); i++) {
            personListPanelHandle.navigateToCard(TYPICAL_EMPLOYEES.get(i));
            Employee expectedEmployee = TYPICAL_EMPLOYEES.get(i);
            PersonCardHandle actualCard = personListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysPerson(expectedEmployee, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        PersonCardHandle expectedCard = personListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        PersonCardHandle selectedCard = personListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedCard, selectedCard);
    }
}
