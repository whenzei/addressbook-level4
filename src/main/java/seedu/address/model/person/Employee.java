package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Employee in the address book.
 *
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Employee extends Person {
    private final UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Employee(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address);
        requireAllNonNull(name, phone, email, address, tags);
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Employee)) {
            return false;
        }

        Employee otherEmployee = (Employee) other;
        return otherEmployee.getName().equals(this.getName())
                && otherEmployee.getPhone().equals(this.getPhone())
                && otherEmployee.getEmail().equals(this.getEmail())
                && otherEmployee.getAddress().equals(this.getAddress());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
