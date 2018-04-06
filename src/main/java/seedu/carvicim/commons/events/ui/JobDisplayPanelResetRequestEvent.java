package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.events.BaseEvent;

/**
 * Indicates a request to reset the job display panel view
 */
public class JobDisplayPanelResetRequestEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
