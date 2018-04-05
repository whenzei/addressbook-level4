package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.events.BaseEvent;
import seedu.carvicim.model.job.Job;

//@@author whenzei
/**
 * Indicates a request to update the job display panel
 */
public class JobDisplayPanelUpdateRequestEvent extends BaseEvent {

    private final Job job;

    public JobDisplayPanelUpdateRequestEvent(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Job getJob() {
        return this.job;
    }
}
