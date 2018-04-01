package seedu.carvicim.storage;

//@@author whenzei

import javax.xml.bind.annotation.XmlValue;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.model.remark.Remark;

/**
 * JAXB-friendly adapted version of the Remark.
 */
public class XmlAdaptedRemark {

    @XmlValue
    private String remark;

    /**
     * Constructs an XmlAdaptedRemark.
     * This is the no-arg constructor that is required by JAXB
     */
    public XmlAdaptedRemark() {}

    /**
     * Construct a {@code XmlAdaptedRemark} with the given {@code remark}.
     */
    public XmlAdaptedRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Converts a given Tag into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedRemark(Remark source) {
        remark = source.value;
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Remark object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted job
     */
    public Remark toModelType() throws IllegalValueException {
        if (!Remark.isValidRemark(remark)) {
            throw new IllegalValueException(Remark.MESSAGE_REMARKS_CONSTRAINTS);
        }
        return new Remark(remark);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof  XmlAdaptedRemark)) {
            return false;
        }

        return remark.equals(((XmlAdaptedRemark) other).remark);
    }
}
