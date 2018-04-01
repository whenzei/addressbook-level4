package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.events.BaseEvent;
import seedu.carvicim.ui.PersonCard;

/**
 * Represents a selection change in the Employee List Panel
 */
public class PersonPanelSelectionChangedEvent extends BaseEvent {


    private final PersonCard newSelection;

    public PersonPanelSelectionChangedEvent(PersonCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public PersonCard getNewSelection() {
        return newSelection;
    }
}
