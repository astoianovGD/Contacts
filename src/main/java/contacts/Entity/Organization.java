package contacts.Entity;

import java.util.List;

/**
 * Represents an Organization contact.
 */
public final class Organization extends Contact {

    /** The address of the organization. */
    private String address;

    /**
     * Constructs a new Organization.
     *
     * @param name    the name of the organization
     * @param number  the phone number of the organization
     * @param addr    the physical address of the organization
     */
    public Organization(final String name,
                        final String number,
                        final String addr) {
        super(name, number, false);
        this.address = addr;
    }

    /**
     * Sets the organization's address and updates the modification time.
     *
     * @param addr the new address
     */
    public void setAddress(final String addr) {
        this.address = addr;
        updateTime();
    }

    /**
     * Gets the organization's address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    @Override
    public String getSearchableString() {
        return String.format("%s %s %s",
                getName(), address, getNumber());
    }

    @Override
    public List<String> getEditableFields() {
        return List.of("name", "number", "address");
    }

    @Override
    public void setFieldValue(final String field, final String value) {
        switch (field) {
            case "name" -> this.setName(value);
            case "number" -> this.setNumber(value);
            case "address" -> this.setAddress(value);
            default -> System.out.println("Bad field!");
        }
    }

    @Override
    public String getFieldValue(final String field) {
        return switch (field) {
            case "name" -> this.getName();
            case "number" -> this.getNumber();
            case "address" -> this.getAddress();
            default -> "Bad field!";
        };
    }

    @Override
    public String getDetailedInfo() {
        return "Organization name: " + getName() + "\n"
                + "Address: " + this.address + "\n"
                + "Number: " + getNumber() + "\n"
                + "Time created: " + formatTime(getTimeCreated()) + "\n"
                + "Time last edit: " + formatTime(getTimeUpdated());
    }
}
