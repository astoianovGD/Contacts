package contacts.Entity;

import contacts.Main;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base abstract class representing a contact.
 */
public abstract class Contact implements Serializable {
    /** The name of the contact. */
    private String name;
    /** The phone number of the contact. */
    private String number;
    /** The creation timestamp of the contact. */
    private final LocalDateTime timeCreated;
    /** The last update timestamp of the contact. */
    private LocalDateTime timeUpdated;
    /** Flag indicating if the contact is a person. */
    private final boolean isPerson;

    /**
     * Constructs a new Contact instance.
     *
     * @param contactName   the name of the contact
     * @param contactNumber the phone number
     * @param personFlag    true if it is a person, false otherwise
     */
    public Contact(final String contactName, final String contactNumber,
                   final boolean personFlag) {
        this.name = contactName;
        this.number = Main.checkNumber(contactNumber);
        this.isPerson = personFlag;
        this.timeCreated = LocalDateTime.now().withNano(0).withSecond(0);
        this.timeUpdated = LocalDateTime.now().withNano(0).withSecond(0);
    }

    /**
     * Gets the contact name.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets a new contact name.
     *
     * @param contactName the new name
     */
    public final void setName(final String contactName) {
        updateTime();
        this.name = contactName;
    }

    /**
     * Gets the contact phone number.
     *
     * @return the phone number
     */
    public final String getNumber() {
        return number;
    }

    /**
     * Sets a new contact phone number.
     *
     * @param contactNumber the new phone number
     */
    public final void setNumber(final String contactNumber) {
        updateTime();
        this.number = Main.checkNumber(contactNumber);
    }

    /**
     * Updates the last modification timestamp.
     */
    public final void updateTime() {
        this.timeUpdated = LocalDateTime.now().withNano(0).withSecond(0);
    }

    /**
     * Checks if this contact represents a person.
     *
     * @return true if it is a person, false otherwise
     */
    public final boolean isPerson() {
        return isPerson;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return the update date and time
     */
    public final LocalDateTime getTimeUpdated() {
        return timeUpdated;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return the creation date and time
     */
    public final LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    /**
     * Gets a single string containing all searchable data.
     *
     * @return the searchable string
     */
    public abstract String getSearchableString();

    /**
     * Checks if the contact details contain the specified keyword.
     *
     * @param keyword the search keyword
     * @return true if found, false otherwise
     */
    public final boolean isContainsASearchWord(final String keyword) {
        try {
            Pattern pattern = Pattern.compile(keyword,
                    Pattern.CASE_INSENSITIVE);
            String fullText = getSearchableString();
            Matcher matcher = pattern.matcher(fullText);
            return matcher.find();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the list of fields that can be edited.
     *
     * @return the list of field names
     */
    public abstract List<String> getEditableFields();

    /**
     * Sets the value for a specific field by its name.
     *
     * @param field the name of the field
     * @param value the new value
     */
    public abstract void setFieldValue(String field, String value);

    /**
     * Gets the value of a specific field by its name.
     *
     * @param field the name of the field
     * @return the field value
     */
    public abstract String getFieldValue(String field);

    /**
     * Formats a LocalDateTime object into a string.
     *
     * @param time the LocalDateTime object
     * @return the formatted string
     */
    protected final String formatTime(final LocalDateTime time) {
        if (time == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm");
        return time.format(formatter);
    }

    /**
     * Gets detailed information about the contact.
     *
     * @return the detailed info string
     */
    public abstract String getDetailedInfo();
}
