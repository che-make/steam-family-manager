package com.metamedicsvr.springtemplate.controller.user;

import com.metamedicsvr.springtemplate.service.user.student.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Tag(name = "Student", description = "Operations related to students")
public class StudentController {

    private final StudentService studentService;



}
