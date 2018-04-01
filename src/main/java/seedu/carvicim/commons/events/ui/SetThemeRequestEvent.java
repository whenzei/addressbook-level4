package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.BaseEvent;

//@@author whenzei
/**
 * An event request to set a new theme
 */
public class SetThemeRequestEvent extends BaseEvent {

    private final Index selectedIndex;

    public SetThemeRequestEvent(Index selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public Index getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
