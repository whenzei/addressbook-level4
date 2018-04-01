package seedu.carvicim.model.job;

//@@author whenzei
/**
 * Represents the status of a car servicing job
 */
public class Status {
    public static final String STATUS_ONGOING = "ongoing";
    public static final String STATUS_CLOSED = "closed";

    public final String value;

    public Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                && this.value.equals(((Status) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
