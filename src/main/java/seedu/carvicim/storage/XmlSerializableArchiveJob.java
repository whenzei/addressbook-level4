package seedu.carvicim.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.ReadOnlyCarvicim;

//@@author richardson0694
/**
 * An Immutable Archive that is serializable to XML format
 */
@XmlRootElement(name = "archive")
public class XmlSerializableArchiveJob {

    @XmlElement
    private List<XmlAdaptedJob> jobs;

    /**
     * Creates an empty XmlSerializableArchiveJob.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableArchiveJob() {
        jobs = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableArchiveJob(ReadOnlyCarvicim src) {
        this();
        jobs.addAll(src.getArchiveJobList().stream().map(XmlAdaptedJob::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code Carvicim} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedJob}.
     */
    public Carvicim toModelType() throws IllegalValueException {
        Carvicim carvicim = new Carvicim();
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

        if (!(other instanceof XmlSerializableArchiveJob)) {
            return false;
        }

        XmlSerializableArchiveJob otherAb = (XmlSerializableArchiveJob) other;
        return jobs.equals(otherAb.jobs);
    }
}
