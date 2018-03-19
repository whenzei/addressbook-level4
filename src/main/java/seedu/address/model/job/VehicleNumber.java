package seedu.address.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author whenzei
/**
 * Represents a Vehicle ID in the Job
 */
public class VehicleNumber {
    public static final String MESSAGE_VEHICLE_ID_CONSTRAINTS =
            "Vehicle ID should only contain alphanumeric characters and should not be blank";

    public static final String VEHICLE_ID_VALIDATION_REGEX = "[\\p{Alnum}]*";

    public final String vehicleNumber;

    public VehicleNumber(String vehicleNumber) {
        requireNonNull(vehicleNumber);
        checkArgument(isValidVehicleNumber(vehicleNumber), MESSAGE_VEHICLE_ID_CONSTRAINTS);
        this.vehicleNumber = vehicleNumber;
    }

    /**
     * Returns true if a given string is a valid vehicle ID
     */
    public static boolean isValidVehicleNumber(String test) {
        return test.matches(VEHICLE_ID_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return vehicleNumber;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof VehicleNumber // instanceof handles nulls
                && this.vehicleNumber.equals(((VehicleNumber) other).vehicleNumber)); // state check
    }

    @Override
    public int hashCode() {
        return vehicleNumber.hashCode();
    }


}
