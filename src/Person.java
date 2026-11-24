package studentregistrationsystem;

public class Person {

    private String firstName;
    private String lastName;
    private String gender;

    public Person(String firstName, String lastName, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    /** Two-letter initials (e.g. "AC"), shown in the avatar circles in the UI. */
    public String getInitials() {

        String first = firstName == null ? "" : firstName.trim();
        String last = lastName == null ? "" : lastName.trim();

        String initials = "";

        if (!first.isEmpty()) {
            initials += first.substring(0, 1);
        }

        if (!last.isEmpty()) {
            initials += last.substring(0, 1);
        }

        return initials.toUpperCase();
    }
}