package com.attendance.repository;

import com.attendance.model.Student;
import com.attendance.model.Section;
import com.attendance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findBySection(Section section);

    Optional<Student> findByUser(User user);
}
