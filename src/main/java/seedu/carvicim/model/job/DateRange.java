package seedu.carvicim.model.job;

import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Calendar;
import java.util.Objects;

//@@author richardson0694
/**
 * Represents a date range in the car servicing manager
 */
public class DateRange {

    private final Date startDate;
    private final Date endDate;

    public DateRange(Date startDate, Date endDate) {
        requireAllNonNull(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    /**
     * Compare the startDate with endDate
     */
    public int compareTo(Date startDate, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(startDate.getYear(), startDate.getMonth(), startDate.getDay());
        cal2.set(endDate.getYear(), endDate.getMonth(), endDate.getDay());
        return cal1.compareTo(cal2);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DateRange)) {
            return false;
        }

        DateRange otherDateRange = (DateRange) other;
        return otherDateRange.getStartDate().equals(this.getStartDate())
                && otherDateRange.getEndDate().equals(this.getEndDate());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Starting Date: ")
                .append(getStartDate())
                .append(" Ending Date: ")
                .append(getEndDate());
        return builder.toString();
    }

}
