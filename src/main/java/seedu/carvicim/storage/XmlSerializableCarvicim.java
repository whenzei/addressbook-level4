package seedu.carvicim.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;

/**
 * An Immutable Carvicim that is serializable to XML format
 */
@XmlRootElement(name = "carvicim")
public class XmlSerializableCarvicim {

    @XmlElement
    private List<XmlAdaptedEmployee> employees;
    @XmlElement
    private List<XmlAdaptedTag> tags;
    @XmlElement
    private List<XmlAdaptedJob> jobs;

    /**
     * Creates an empty XmlSerializableCarvicim.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableCarvicim() {
        employees = new ArrayList<>();
        tags = new ArrayList<>();
        jobs = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableCarvicim(ReadOnlyCarvicim src) {
        this();
        employees.addAll(src.getEmployeeList().stream().map(XmlAdaptedEmployee::new).collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
        jobs.addAll(src.getJobList().stream().map(XmlAdaptedJob::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code Carvicim} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedEmployee} or {@code XmlAdaptedTag}.
     */
    public Carvicim toModelType() throws IllegalValueException {
        Carvicim carvicim = new Carvicim();
        for (XmlAdaptedTag t : tags) {
            carvicim.addTag(t.toModelType());
        }
        for (XmlAdaptedEmployee p : employees) {
            carvicim.addEmployee(p.toModelType());
        }
        for (XmlAdaptedJob j : jobs) {
            carvicim.addJob(j.toModelType());
        }
        return carvicim;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableCarvicim)) {
            return false;
        }

        XmlSerializableCarvicim otherAb = (XmlSerializableCarvicim) other;
        return employees.equals(otherAb.employees) && tags.equals(otherAb.tags) && jobs.equals(otherAb.jobs);
    }
}
