package contacts.Entity;

import contacts.Main;

import java.util.List;

public class Organization extends Contact{
    private String address;

    public Organization(String name, String number, String address) {
        super(name, number, false);
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
        updateTime();
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return this.getName();
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
    public void setFieldValue(String field, String value) {
        switch (field) {
            case "name" -> this.setName(value);
            case "number" -> this.setNumber(Main.checkNumber(value));
            case "address" -> this.setAddress(value);
            default -> {return;}
        }
    }

    @Override
    public String getFieldValue(String field) {
        return switch (field) {
            case "name" -> this.getName();
            case "number" -> this.getNumber();
            case "address" -> this.getAddress();
            default -> "Bad field!";
        };
    }

    @Override
    public String getDetailedInfo() {
        return "Organization name: " + getName() + "\n" +
                "Address: " + this.address + "\n" +
                "Number: " + getNumber() + "\n" +
                "Time created: " + formatTime(getTimeCreated()) + "\n" +
                "Time last edit: " + formatTime(getTimeUpdated());
    }
}
