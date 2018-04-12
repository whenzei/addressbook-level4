package seedu.carvicim.commons.events.model;

import seedu.carvicim.commons.events.BaseEvent;
import seedu.carvicim.model.ReadOnlyCarvicim;

//@@author richardson0694
/** Indicates the Carvicim in the model has changed*/
public class ArchiveEvent extends BaseEvent {
    public final ReadOnlyCarvicim data;

    public ArchiveEvent(ReadOnlyCarvicim data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getEmployeeList().size() + ", number of tags "
                + data.getTagList().size() + ", number of jobs " + data.getJobList().size()
                + ", number of archived jobs " + data.getArchiveJobList().size();
    }
}
