package contacts.Entity;

import contacts.Main;
import java.util.List;

/**
 * Represents a Person contact.
 */
public final class Person extends Contact {
    /** The surname of the person. */
    private String surname;
    /** The birthdate of the person. */
    private String birth;
    /** The gender of the person. */
    private String gender;

    /**
     * Constructs a new Person contact.
     *
     * @param name          the first name of the person
     * @param personSurname the surname of the person
     * @param number        the phone number
     * @param birthDate     the birthdate
     * @param personGender  the gender
     */
    public Person(final String name, final String personSurname,
                  final String number, final String birthDate,
                  final String personGender) {
        super(name, number, true);
        this.surname = personSurname;
        this.birth = birthDate;
        this.gender = personGender;
    }

    /**
     * Gets the person's surname.
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the person's surname.
     *
     * @param personSurname the new surname
     */
    public void setSurname(final String personSurname) {
        this.surname = personSurname;
        updateTime();
    }

    /**
     * Gets the person's birthdate.
     *
     * @return the birthdate
     */
    public String getBirth() {
        return birth;
    }

    /**
     * Sets the person's birthdate.
     *
     * @param birthDate the new birthdate
     */
    public void setBirth(final String birthDate) {
        this.birth = birthDate;
        updateTime();
    }

    /**
     * Gets the person's gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the person's gender.
     *
     * @param personGender the new gender
     */
    public void setGender(final String personGender) {
        this.gender = personGender;
        updateTime();
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getSurname();
    }

    @Override
    public String getSearchableString() {
        return String.format("%s %s %s %s %s",
                getName(), getSurname(), getNumber(), getBirth(), getGender());
    }

    @Override
    public List<String> getEditableFields() {
        return List.of("name", "surname", "number", "birth", "gender");
    }

    @Override
    public void setFieldValue(final String field, final String value) {
        switch (field) {
            case "name" -> this.setName(value);
            case "surname" -> this.setSurname(value);
            case "number" -> this.setNumber(Main.checkNumber(value));
            case "birth" -> this.setBirth(value);
            case "gender" -> this.setGender(value);
            default -> System.out.println("Bad field!");
        }
    }

    @Override
    public String getFieldValue(final String field) {
        return switch (field) {
            case "name" -> this.getName();
            case "surname" -> this.getSurname();
            case "number" -> this.getNumber();
            case "birth" -> this.getBirth();
            case "gender" -> this.getGender();
            default -> "Bad field!";
        };
    }

    @Override
    public String getDetailedInfo() {
        return "Name: " + getName() + "\n"
                + "Surname: " + this.surname + "\n"
                + "Birth date: " + this.birth + "\n"
                + "Gender: " + this.gender + "\n"
                + "Number: " + getNumber() + "\n"
                + "Time created: " + formatTime(getTimeCreated()) + "\n"
                + "Time last edit: " + formatTime(getTimeUpdated());
    }
}
