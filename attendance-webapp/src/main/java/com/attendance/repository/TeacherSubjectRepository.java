package com.attendance.repository;

import com.attendance.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    List<TeacherSubject> findByTeacher(Teacher teacher);

    List<TeacherSubject> findBySubject(Subject subject);

    List<TeacherSubject> findBySection(Section section);
}
