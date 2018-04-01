package seedu.carvicim.model.person;

//@author yuhongherald

/**
 * Represents a customer in the servicing manager.
 */
public class Customer extends Person {
    /**
     * Every field must be present and not null.
     */
    public Customer(Name name, Phone phone, Email email, Address address) {
        super(name, phone, email);
    }

    /**
     * Generates a valid cutomer
     */
    public static Customer generateCustomer() {
        Name name = new Name("name");
        Phone phone = new Phone("91234567");
        Email email = new Email("name@example.com");
        Address address = new Address("randomAddress");
        return new Customer(name, phone, email, address);
    }
}
