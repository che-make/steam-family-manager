package com.metamedicsvr.springtemplate.repositories.user.student;

import com.metamedicsvr.springtemplate.entities.user.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    long countBySchoolClass_Organization_Id(UUID organizationId);

    Optional<Student> findByIdAndSchoolClass_Organization_Id(UUID studentId, UUID organizationId);

    List<Student> findAllBySchoolClass_Id(UUID schoolClassId);
}
