package contacts.Entity;


import contacts.Main;

import java.util.List;

public class Person extends Contact{
    private String surname;
    private String birth;
    private String gender;

    public Person(String name, String surname, String number, String birth, String gender) {
        super(name, number, true);
        this.surname = surname;
        this.birth = birth;
        this.gender = gender;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        updateTime();
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
        updateTime();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
    public void setFieldValue(String field, String value) {
        switch (field) {
            case "name" -> this.setName(value);
            case "surname" -> this.setSurname(value);
            case "number" -> this.setNumber(Main.checkNumber(value));
            case "birth" -> this.setBirth(value);
            case "gender" -> this.setGender(value);
            default -> {return;}
        }
    }

    @Override
    public String getFieldValue(String field) {
        return switch (field) {
            case "name" -> this.getName();
            case "surname" -> this.getSurname();
            case "number" -> this.getNumber();
            case "birth" -> this.getBirth();
            case "gender" -> this.getGender();
            default -> "Bad field!";
        };
    }

    public String getDetailedInfo() {
        return "Name: " + getName() + "\n" +
                "Surname: " + this.surname + "\n" +
                "Birth date: " + this.birth + "\n" +
                "Gender: " + this.gender + "\n" +
                "Number: " + getNumber() + "\n" +
                "Time created: " + formatTime(getTimeCreated()) + "\n" +
                "Time last edit: " + formatTime(getTimeUpdated());
    }

}
