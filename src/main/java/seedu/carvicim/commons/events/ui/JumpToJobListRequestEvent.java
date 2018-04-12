package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of jobs
 */
public class JumpToJobListRequestEvent extends BaseEvent {
    public final int targetIndex;

    public JumpToJobListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
