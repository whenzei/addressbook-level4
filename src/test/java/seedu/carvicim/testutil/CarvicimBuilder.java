package seedu.carvicim.testutil;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.tag.Tag;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code Carvicim ab = new CarvicimBuilder().withEmployee("John", "Doe").withTag("Friend").build();}
 */
public class CarvicimBuilder {

    private Carvicim carvicim;

    public CarvicimBuilder() {
        carvicim = new Carvicim();
    }

    public CarvicimBuilder(Carvicim carvicim) {
        this.carvicim = carvicim;
    }

    /**
     * Adds a new {@code Employee} to the {@code Carvicim} that we are building.
     */
    public CarvicimBuilder withEmployee(Employee employee) {
        try {
            carvicim.addEmployee(employee);
        } catch (DuplicateEmployeeException dpe) {
            throw new IllegalArgumentException("employee is expected to be unique.");
        }
        return this;
    }

    /**
     * Parses {@code tagName} into a {@code Tag} and adds it to the {@code Carvicim} that we are building.
     */
    public CarvicimBuilder withTag(String tagName) {
        try {
            carvicim.addTag(new Tag(tagName));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("tagName is expected to be valid.");
        }
        return this;
    }

    public Carvicim build() {
        return carvicim;
    }
}
