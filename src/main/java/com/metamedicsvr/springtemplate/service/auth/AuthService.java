package com.metamedicsvr.springtemplate.service.auth;

import com.metamedicsvr.springtemplate.dto.auth.AuthResponse;
import com.metamedicsvr.springtemplate.dto.auth.LoginRequest;
import com.metamedicsvr.springtemplate.dto.auth.RegisterStudentRequest;
import com.metamedicsvr.springtemplate.dto.auth.RegisterTeacherRequest;
import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import com.metamedicsvr.springtemplate.jwt.JwtService;
import com.metamedicsvr.springtemplate.error.exception.NotFoundException;
import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.entities.user.student.Student;
import com.metamedicsvr.springtemplate.entities.user.teacher.Teacher;
import com.metamedicsvr.springtemplate.repositories.user.UserRepository;
import com.metamedicsvr.springtemplate.service.organization.SchoolClassService;
import com.metamedicsvr.springtemplate.service.user.student.StudentService;
import com.metamedicsvr.springtemplate.service.user.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final SchoolClassService schoolClassService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User: " + loginRequest.getEmail() + " not found"));

        updateLastLoginTimestamp(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse registerTeacher(RegisterTeacherRequest registerStudentRequest) {
        Teacher teacher = teacherService.createTeacher(registerStudentRequest);

        return AuthResponse.builder()
                .token(jwtService.getToken(teacher))
                .role(teacher.getRole().name())
                .build();
    }

    public AuthResponse registerStudent(RegisterStudentRequest registerStudentRequest) {
        SchoolClass currentSchoolClass = schoolClassService.getSchoolClassFromClassCode(registerStudentRequest.getSchoolClassCode());

        Student student = studentService.createStudent(registerStudentRequest, currentSchoolClass);

        return AuthResponse.builder()
                .token(jwtService.getToken(student))
                .role(student.getRole().name())
                .build();
    }


    private void updateLastLoginTimestamp(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

}
