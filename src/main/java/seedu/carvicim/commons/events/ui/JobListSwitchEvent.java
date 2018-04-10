package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.events.BaseEvent;

// @@author yuhongherald
/**
 * Indicates that there is a change in job list.
 */
public class JobListSwitchEvent extends BaseEvent {

    public final String message;

    public JobListSwitchEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
