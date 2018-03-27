package seedu.address.testutil;

import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

//@@author whenzei
/**
 * A utility class to help with building Client objects.
 */
public class ClientBuilder {
    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";

    private Name name;
    private Phone phone;
    private Email email;

    public ClientBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
    }

    public ClientBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    public ClientBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    public ClientBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email);
    }
}
