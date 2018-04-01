package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.carvicim.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.carvicim.logic.commands.CommandTestUtil.TAG_DESC_MECHANIC;
import static seedu.carvicim.logic.commands.CommandTestUtil.TAG_DESC_TECHNICIAN;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_TAG_MECHANIC;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_TAG_TECHNICIAN;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.carvicim.logic.commands.AddEmployeeCommand;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.tag.Tag;
import seedu.carvicim.testutil.EmployeeBuilder;

public class AddEmployeeCommandParserTest {
    private AddEmployeeCommandParser parser = new AddEmployeeCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Employee expectedEmployee = new EmployeeBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_TECHNICIAN).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_TECHNICIAN, new AddEmployeeCommand(expectedEmployee));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_TECHNICIAN, new AddEmployeeCommand(expectedEmployee));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_TECHNICIAN, new AddEmployeeCommand(expectedEmployee));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + TAG_DESC_TECHNICIAN, new AddEmployeeCommand(expectedEmployee));

        // multiple tags - all accepted
        Employee expectedEmployeeMultipleTags = new EmployeeBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withTags(VALID_TAG_TECHNICIAN, VALID_TAG_MECHANIC).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_MECHANIC + TAG_DESC_TECHNICIAN, new AddEmployeeCommand(expectedEmployeeMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Employee expectedEmployee = new EmployeeBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY,
                new AddEmployeeCommand(expectedEmployee));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_MECHANIC + TAG_DESC_TECHNICIAN, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB
                + TAG_DESC_MECHANIC + TAG_DESC_TECHNICIAN, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC
                + TAG_DESC_MECHANIC + TAG_DESC_TECHNICIAN, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_TECHNICIAN, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_PHONE_DESC + EMAIL_DESC_BOB,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_MECHANIC + TAG_DESC_TECHNICIAN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE));
    }
}
