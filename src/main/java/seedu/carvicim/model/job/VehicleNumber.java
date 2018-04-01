package seedu.carvicim.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.AppUtil.checkArgument;

//@@author whenzei
/**
 * Represents a Vehicle ID in the Job
 */
public class VehicleNumber {
    public static final String DEFAULT_VEHICLE_NUMBER = "SAS123J";
    public static final String MESSAGE_VEHICLE_ID_CONSTRAINTS =
            "Vehicle ID should only contain alphanumeric characters and should not be blank";

    public static final String VEHICLE_ID_VALIDATION_REGEX = "[a-zA-Z]+[0-9]+[a-zA-Z0-9]*|[0-9]+[a-zA-Z][a-zA-Z0-9]*";

    public final String value;

    public VehicleNumber(String value) {
        requireNonNull(value);
        checkArgument(isValidVehicleNumber(value), MESSAGE_VEHICLE_ID_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid vehicle ID
     */
    public static boolean isValidVehicleNumber(String test) {
        return test.matches(VEHICLE_ID_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof VehicleNumber // instanceof handles nulls
                && this.value.equals(((VehicleNumber) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }


}
