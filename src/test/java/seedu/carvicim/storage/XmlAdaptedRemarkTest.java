package seedu.carvicim.storage;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.testutil.Assert;

//@@author whenzei
public class XmlAdaptedRemarkTest {
    private static final String INVALID_REMARK = "";

    private static final String VALID_REMARK = "asd";

    @Test
    public void toModelType_validRemark_returnsRemark() throws Exception {
        XmlAdaptedRemark xmlAdaptedRemark = new XmlAdaptedRemark(VALID_REMARK);
        Remark remark = new Remark(VALID_REMARK);

        assertEquals(remark, xmlAdaptedRemark.toModelType());
    }

    @Test
    public void toModelType_invalidRemark_throwsIllegalValueException() {
        XmlAdaptedRemark xmlAdaptedRemark = new XmlAdaptedRemark(INVALID_REMARK);

        String expectedMessage = Remark.MESSAGE_REMARKS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, xmlAdaptedRemark::toModelType);
    }

}
