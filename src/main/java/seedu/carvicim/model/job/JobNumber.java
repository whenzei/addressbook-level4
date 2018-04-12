package seedu.carvicim.model.job;

//@@author whenzei

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.AppUtil.checkArgument;

/**
 * Represent a job number in the servicing manager
 */
public class JobNumber {
    public static final String MESSAGE_JOB_NUMBER_CONSTRAINTS = "Job number should be a positive number (non-zero)";

    public static final String JOB_NUMBER_VALIDATION_REGEX = "[0-9]+";

    private static int nextJobNumber;

    public final String value;

    public JobNumber() {
        value = Integer.toString(nextJobNumber);
        incrementNextJobNumber();
    }

    public JobNumber(String jobNumber) {
        requireNonNull(jobNumber);
        checkArgument(isValidJobNumber(jobNumber), MESSAGE_JOB_NUMBER_CONSTRAINTS);
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

    public void incrementNextJobNumber() {
        nextJobNumber++;
    }

    public static String getNextJobNumber() {
        return nextJobNumber + "";
    }

    public static void setNextJobNumber(String arg) {
        nextJobNumber = Integer.parseInt(arg);
    }

    /**
     * Returns true if a given string is a valid job number.
     */
    public static boolean isValidJobNumber(String jobNumber) {
        return jobNumber.matches(JOB_NUMBER_VALIDATION_REGEX);
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
