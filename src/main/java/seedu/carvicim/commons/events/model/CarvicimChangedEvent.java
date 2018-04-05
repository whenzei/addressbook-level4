package seedu.carvicim.commons.events.model;

import seedu.carvicim.commons.events.BaseEvent;
import seedu.carvicim.model.ReadOnlyCarvicim;

/** Indicates the Carvicim in the model has changed*/
public class CarvicimChangedEvent extends BaseEvent {

    public final ReadOnlyCarvicim data;

    public CarvicimChangedEvent(ReadOnlyCarvicim data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getEmployeeList().size() + ", number of tags "
                + data.getTagList().size() + ", number of jobs " + data.getJobList().size();
    }
}
