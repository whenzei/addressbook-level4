package seedu.carvicim.model.job;

//@@author whenzei
/**
 * Represent a job number in the servicing manager
 */
public class JobNumber {
    private static int nextJobNumber;

    public final String value;

    public JobNumber() {
        value = Integer.toString(nextJobNumber);
        incrementNextJobNumber();
    }

    public JobNumber(String jobNumber) {
        value = jobNumber;
    }

    /**
     * Initialize the next job number of the car servicing manager
     */
    public static void initialize(String arg) {
        nextJobNumber = Integer.parseInt(arg);
    }

    public static void initialize(int arg) {
        nextJobNumber = arg;
    }

    public static void incrementNextJobNumber() {
        nextJobNumber++;
    }

    public int asInteger() {
        return Integer.parseInt(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof JobNumber // instanceof handles nulls
                && this.value.equals(((JobNumber) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
