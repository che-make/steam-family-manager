package com.metamedicsvr.springtemplate.service.user.student;

import com.metamedicsvr.springtemplate.dto.auth.RegisterStudentRequest;
import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import com.metamedicsvr.springtemplate.entities.user.student.Student;
import com.metamedicsvr.springtemplate.error.exception.NotFoundException;
import com.metamedicsvr.springtemplate.repositories.user.student.StudentRepository;
import com.metamedicsvr.springtemplate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserService userService;

    private final StudentRepository studentRepository;

    public Student findStudentById(UUID studentId) {
        return studentRepository.findById(studentId).orElseThrow(
                () -> new NotFoundException("Student not found with id: " + studentId));
    }

    public List<Student> getStudentsBySchoolClassId(UUID schoolClassId) {
        return studentRepository.findAllBySchoolClass_Id(schoolClassId);
    }

    public Student createStudent(RegisterStudentRequest registerStudentRequest, SchoolClass schoolClass) {

        Student student = Student.builder()
                .schoolClass(schoolClass)
                .build();

        userService.setCommonFields(student, registerStudentRequest);
        return (Student) userService.saveUser(student);
    }

    public Student getStudentByIdAndOrganizationId(UUID studentId, UUID organizationId) {
        return studentRepository.findByIdAndSchoolClass_Organization_Id(studentId, organizationId).orElseThrow(
                () -> new NotFoundException("Student not found with id: " + studentId + " and organizationId: " + organizationId));
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public long countStudentsByOrganizationId(UUID organizationId) {
        return studentRepository.countBySchoolClass_Organization_Id(organizationId);
    }

}
