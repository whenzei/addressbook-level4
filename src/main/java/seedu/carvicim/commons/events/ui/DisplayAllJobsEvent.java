package seedu.carvicim.commons.events.ui;

import javafx.collections.ObservableList;
import seedu.carvicim.commons.events.BaseEvent;
import seedu.carvicim.model.job.Job;

/**
 * Represents a selection change in the Employee List Panel
 */
//@@author yuhongherald
public class DisplayAllJobsEvent extends BaseEvent {


    private final ObservableList<Job> jobList;

    public DisplayAllJobsEvent(ObservableList<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ObservableList<Job> getJobList() {
        return jobList;
    }
}
