package seedu.carvicim.logic.parser;

import static seedu.carvicim.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.carvicim.logic.commands.CommandTestUtil.ASSIGNED_EMPLOYEE_INDEX_DESC_ONE;
import static seedu.carvicim.logic.commands.CommandTestUtil.ASSIGNED_EMPLOYEE_INDEX_DESC_TWO;
import static seedu.carvicim.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_ASSIGNED_EMPLOYEE_INDEX_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.INVALID_VEHICLE_NUM_DESC;
import static seedu.carvicim.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_ASSIGNED_EMPLOYEE_INDEX_A;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.carvicim.logic.commands.CommandTestUtil.VALID_VEHICLE_NUMBER_A;
import static seedu.carvicim.logic.commands.CommandTestUtil.VEHICLE_NUMBER_DESC_ONE;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.carvicim.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.carvicim.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.ArrayList;

import org.junit.Test;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.logic.commands.AddJobCommand;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.testutil.ClientBuilder;

public class AddJobCommandParserTest {

    private AddJobCommandParser parser = new AddJobCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedClient = new ClientBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).build();

        // one assigned employee
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE,
                new AddJobCommand(expectedClient, new VehicleNumber(VALID_VEHICLE_NUMBER_A),
                        generateOneValidEmployeeIndex()));

        // two assigned employees
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_TWO,
                new AddJobCommand(expectedClient, new VehicleNumber(VALID_VEHICLE_NUMBER_A),
                        generateTwoValidEmployeeIndices()));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);
        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB
                + VEHICLE_NUMBER_DESC_ONE + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);

        // missing vehicle number prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB
                + VALID_VEHICLE_NUMBER_A + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE, expectedMessage);

        // missing assigned employee prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB
                + VALID_VEHICLE_NUMBER_A + VALID_ASSIGNED_EMPLOYEE_INDEX_A, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid vehicle number
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_VEHICLE_NUM_DESC + ASSIGNED_EMPLOYEE_INDEX_DESC_ONE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));

        //invalid assigned employee indices
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + VEHICLE_NUMBER_DESC_ONE + INVALID_ASSIGNED_EMPLOYEE_INDEX_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
    }

    /**
     * Generates an Arraylist of valid first assigned employee index
     */
    private ArrayList<Index> generateOneValidEmployeeIndex() {
        ArrayList<Index> indices = new ArrayList<Index>();
        indices.add(INDEX_FIRST_PERSON);
        return indices;
    }

    /**
     * Generates an Arraylist of valid first and second assigned employee index
     */
    private ArrayList<Index> generateTwoValidEmployeeIndices() {
        ArrayList<Index> indices = new ArrayList<Index>();
        indices.add(INDEX_FIRST_PERSON);
        indices.add(INDEX_SECOND_PERSON);
        return indices;
    }
}
