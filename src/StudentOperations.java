package studentregistrationsystem;

import java.util.List;

public interface StudentOperations {

    boolean addStudent(Student student);

    List<Student> getAllStudents();

    boolean updateStudent(Student student);

    boolean deleteStudent(int id);

    /**
     * Returns every student whose admission number, name or email
     * matches the keyword (newest first). The list is empty if none match.
     */
    List<Student> searchStudents(String keyword);
}