package seedu.carvicim.model.remark;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.AppUtil.checkArgument;

//@@author whenzei
/**
 * Represents a remark for a job in the car servicing manager
 */
public class Remark {
    public static final String MESSAGE_REMARKS_CONSTRAINTS =
            "Remark can take any values, and it should not be blank";

    /*
     * Remark argument should be anything, except just whitespace or newline
     */
    public static final String REMARK_VALIDATION_REGEX = "(?!^ +$)^.+$";

    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        checkArgument(isValidRemark(remark), MESSAGE_REMARKS_CONSTRAINTS);
        this.value = remark;
    }

    /**
     * Returns true if a given string is a valid remark.
     */
    public static boolean isValidRemark(String test) {
        return test.matches(REMARK_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
