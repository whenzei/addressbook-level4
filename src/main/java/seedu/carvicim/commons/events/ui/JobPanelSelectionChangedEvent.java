package seedu.carvicim.commons.events.ui;

import seedu.carvicim.commons.events.BaseEvent;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.ui.JobCard;

//@@author whenzei
/**
 * Represents a selection change in the Job List Panel
 */
public class JobPanelSelectionChangedEvent extends BaseEvent {
    private final JobCard newSelection;

    public JobPanelSelectionChangedEvent(JobCard newSelection) {
        this.newSelection = newSelection;
    }

    public Job getJob() {
        return newSelection.job;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public JobCard getNewSelection() {
        return newSelection;
    }
}
