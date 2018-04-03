package seedu.carvicim.testutil;

import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.DateRange;

//@@author richardson0694
/**
 * A utility class to help with building DateRange objects.
 */
public class DateRangeBuilder {

    public static final String DEFAULT_START_DATE = "Mar 01 2018";
    public static final String DEFAULT_END_DATE = "Mar 25 2018";

    private Date startDate;
    private Date endDate;

    public DateRangeBuilder() {
        startDate = new Date(DEFAULT_START_DATE);
        endDate = new Date(DEFAULT_END_DATE);
    }

    /**
     * Initializes the DateRangeBuilder with the data of {@code dateRangeToCopy}.
     */
    public DateRangeBuilder(DateRange dateRangeToCopy) {
        startDate = dateRangeToCopy.getStartDate();
        endDate = dateRangeToCopy.getEndDate();
    }
    /**
     * Sets the {@code Date} of the {@code DateRange} that we are building.
     */
    public DateRangeBuilder withDateRange(String startDate, String endDate) {
        this.startDate = new Date(startDate);
        this.endDate = new Date(endDate);
        return this;
    }

    public DateRange build() {
        return new DateRange(startDate, endDate);
    }

}
