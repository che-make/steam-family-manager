package com.metamedicsvr.springtemplate.repositories.user.teacher;

import com.metamedicsvr.springtemplate.entities.user.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
}
