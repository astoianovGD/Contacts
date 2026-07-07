package contacts.Entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Contact implements Serializable {
    private String name;
    private String number;
    private final LocalDateTime timeCreated;
    private LocalDateTime timeUpdated;
    private boolean isPerson;

    public Contact(String name, String number, boolean isPerson) {
        this.name = name;
        this.number = number;
        this.timeCreated = LocalDateTime.now().withNano(0).withSecond(0);
        this.timeUpdated = LocalDateTime.now().withNano(0).withSecond(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        updateTime();
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        updateTime();
        this.number = number;
    }

    public void updateTime(){
        this.timeUpdated = LocalDateTime.now().withNano(0).withSecond(0);
    }

    public boolean isPerson() {
        return isPerson;
    }

    public LocalDateTime getTimeUpdated() {
        return timeUpdated;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public abstract String getSearchableString();

    public boolean isContainsASearchWord(String keyword) {
        try {
            Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
            String fullText = getSearchableString();
            Matcher matcher = pattern.matcher(fullText);
            return matcher.find();
        } catch (Exception e) {
            return false;
        }
    }

    public abstract List<String> getEditableFields();

    public abstract void setFieldValue(String field, String value);

    public abstract String getFieldValue(String field);

    protected String formatTime(LocalDateTime time) {
        if (time == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return time.format(formatter);
    }

    public abstract String getDetailedInfo();
}
