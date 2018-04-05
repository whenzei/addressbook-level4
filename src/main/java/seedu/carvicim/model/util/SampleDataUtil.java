package seedu.carvicim.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.Status;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Email;
import seedu.carvicim.model.person.Employee;
import seedu.carvicim.model.person.Name;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.Phone;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.remark.RemarkList;
import seedu.carvicim.model.tag.Tag;

/**
 * Contains utility methods for populating {@code Carvicim} with sample data.
 */
public class SampleDataUtil {
    public static Employee[] getSampleEmployees() {
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

    public static Job[] getSampleJobs() {
        Name name1 = new Name("Ell Oel");
        Name name2 = new Name("Sam Lee");

        Phone phone1 = new Phone("89898989");
        Phone phone2 = new Phone("85858585");

        Email email1 = new Email("lol@example.com");
        Email email2 = new Email("samelee@example.com");

        Employee[] employees = SampleDataUtil.getSampleEmployees();

        UniqueEmployeeList assignedEmployees1 = new UniqueEmployeeList();
        UniqueEmployeeList assignedEmployees2 = new UniqueEmployeeList();
        try {
            assignedEmployees1.add(employees[0]);
            assignedEmployees1.add(employees[1]);
            assignedEmployees2.add(employees[2]);
            assignedEmployees2.add(employees[3]);
        } catch (DuplicateEmployeeException e) {
            new AssertionError("Should not fail");
        }

        return new Job[] {
            new Job(new Person(name1, phone1, email1), new VehicleNumber("KHG123A"), new JobNumber("1"),
                     new Date(), assignedEmployees1, new Status(Status.STATUS_ONGOING), new RemarkList()),
            new Job(new Person(name2, phone2, email2), new VehicleNumber("GHA978Z"), new JobNumber("2"),
                    new Date(), assignedEmployees2, new Status(Status.STATUS_ONGOING), new RemarkList())

        };
    }

    public static ReadOnlyCarvicim getSampleCarvicim() {
        try {
            Carvicim sampleAb = new Carvicim();
            for (Employee sampleEmployee : getSampleEmployees()) {

                sampleAb.addEmployee(sampleEmployee);
            }

            for (Job sampleJob : getSampleJobs()) {

                sampleAb.addJob(sampleJob);
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
