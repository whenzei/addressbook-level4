package seedu.carvicim.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.tag.Tag;

/**
 * Contains utility methods for populating {@code Carvicim} with sample data.
 */
public class SampleDataUtil {
    public static Employee[] getSamplePersons() {
        return new Employee[] {
            new Employee(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                getTagSet("mechanic")),
            new Employee(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                getTagSet("technician", "mechanic")),
            new Employee(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                getTagSet("technician")),
            new Employee(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                getTagSet("technician")),
            new Employee(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                getTagSet("technician")),
            new Employee(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                getTagSet("technician"))
        };
    }

    public static ReadOnlyCarvicim getSampleAddressBook() {
        try {
            Carvicim sampleAb = new Carvicim();
            for (Employee sampleEmployee : getSamplePersons()) {
                sampleAb.addEmployee(sampleEmployee);
            }
            return sampleAb;
        } catch (DuplicateEmployeeException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
