package studentregistrationsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class StudentRepository implements StudentOperations {

    @Override
    public boolean addStudent(Student student) {

        String sql = "INSERT INTO students "
                + "(admission_number, first_name, last_name, gender, course, email, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getAdmissionNumber());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getCourse());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhone());

            return pstmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            showError("Registration failed.\n"
                    + "A student with this admission number or email already exists.");
            return false;

        } catch (SQLException e) {
            showError("Registration failed.\n" + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapStudent(rs));
            }

        } catch (SQLException e) {
            showError("Failed to load student records.\n" + e.getMessage());
        }

        return students;
    }

    @Override
    public List<Student> searchStudents(String keyword) {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students "
                + "WHERE admission_number LIKE ? "
                + "OR first_name LIKE ? "
                + "OR last_name LIKE ? "
                + "OR CONCAT(first_name, ' ', last_name) LIKE ? "
                + "OR email LIKE ? "
                + "ORDER BY id ASC";

        String pattern = "%" + keyword + "%";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, pattern);
            }

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }

        } catch (SQLException e) {
            showError("Search failed.\n" + e.getMessage());
        }

        return students;
    }

    @Override
    public boolean updateStudent(Student student) {

        String sql = "UPDATE students SET "
                + "admission_number = ?, "
                + "first_name = ?, "
                + "last_name = ?, "
                + "gender = ?, "
                + "course = ?, "
                + "email = ?, "
                + "phone = ? "
                + "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getAdmissionNumber());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getCourse());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhone());
            pstmt.setInt(8, student.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            showError("Update failed.\n"
                    + "Another student already uses this admission number or email.");
            return false;

        } catch (SQLException e) {
            showError("Update failed.\n" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteStudent(int id) {

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            showError("Deletion failed.\n" + e.getMessage());
            return false;
        }
    }

    // ================= HELPERS =================

    private Student mapStudent(ResultSet rs) throws SQLException {

        return new Student(
                rs.getInt("id"),
                rs.getString("admission_number"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("gender"),
                rs.getString("course"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(null,
                message,
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}