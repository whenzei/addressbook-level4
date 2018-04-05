package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of employees
 */
public class JumpToEmployeeListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToEmployeeListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
