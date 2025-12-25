package com.attendance.repository;

import com.attendance.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);

    List<Attendance> findBySubject(Subject subject);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByStudentAndSubject(Student student, Subject subject);

    List<Attendance> findByStudentAndDateBetween(Student student, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.student.section = :section AND a.date = :date")
    List<Attendance> findBySectionAndDate(Section section, LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student = :student AND a.status = :status")
    long countByStudentAndStatus(Student student, AttendanceStatus status);

    List<Attendance> findByStudentAndSubjectAndDate(Student student, Subject subject, LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.subject = :subject AND a.date = :date AND a.student.section = :section")
    List<Attendance> findBySubjectAndDateAndSection(Subject subject, LocalDate date, Section section);
}
