package com.attendance.dao;

import com.attendance.model.AttendanceRecord;
import com.attendance.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    public void markAttendance(int studentId, LocalDate attendanceDate, String status) throws SQLException {
        // First try to update existing record
        String updateSql = "UPDATE attendance_records SET status = ? WHERE student_id = ? AND attendance_date = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSql)) {
            statement.setString(1, status);
            statement.setInt(2, studentId);
            statement.setDate(3, Date.valueOf(attendanceDate));
            int updated = statement.executeUpdate();
            
            // If no rows updated, insert new record
            if (updated == 0) {
                String insertSql = "INSERT INTO attendance_records(student_id, attendance_date, status) VALUES (?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    insertStatement.setInt(1, studentId);
                    insertStatement.setDate(2, Date.valueOf(attendanceDate));
                    insertStatement.setString(3, status);
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    public List<AttendanceRecord> getAttendanceByStudent(int studentId) throws SQLException {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = """
                SELECT ar.id, ar.student_id, s.name, s.usn, ar.attendance_date, ar.status
                FROM attendance_records ar
                JOIN students s ON s.id = ar.student_id
                WHERE ar.student_id = ?
                ORDER BY ar.attendance_date DESC
                """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    records.add(mapAttendanceRecord(resultSet));
                }
            }
        }
        return records;
    }

    public List<AttendanceRecord> getAllAttendanceRecords() throws SQLException {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = """
                SELECT ar.id, ar.student_id, s.name, s.usn, ar.attendance_date, ar.status
                FROM attendance_records ar
                JOIN students s ON s.id = ar.student_id
                ORDER BY ar.attendance_date DESC, s.name
                """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                records.add(mapAttendanceRecord(resultSet));
            }
        }
        return records;
    }

    private AttendanceRecord mapAttendanceRecord(ResultSet resultSet) throws SQLException {
        AttendanceRecord record = new AttendanceRecord();
        record.setId(resultSet.getInt("id"));
        record.setStudentId(resultSet.getInt("student_id"));
        record.setStudentName(resultSet.getString("name"));
        record.setUsn(resultSet.getString("usn"));
        record.setAttendanceDate(resultSet.getDate("attendance_date").toLocalDate());
        record.setStatus(resultSet.getString("status"));
        return record;
    }
}
