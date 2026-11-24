package studentregistrationsystem;

public class Student extends Person {

    private int id;
    private String admissionNumber;
    private String course;
    private String email;
    private String phone;

    public Student(int id, String admissionNumber,
            String firstName, String lastName,
            String gender, String course,
            String email, String phone) {

        super(firstName, lastName, gender);

        this.id = id;
        this.admissionNumber = admissionNumber;
        this.course = course;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return getFullName() + " - " + admissionNumber;
    }
}