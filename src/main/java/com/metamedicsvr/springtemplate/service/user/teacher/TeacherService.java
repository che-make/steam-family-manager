package com.metamedicsvr.springtemplate.service.user.teacher;

import com.metamedicsvr.springtemplate.dto.auth.RegisterTeacherRequest;
import com.metamedicsvr.springtemplate.entities.organization.Organization;
import com.metamedicsvr.springtemplate.entities.user.teacher.Teacher;
import com.metamedicsvr.springtemplate.service.organization.OrganizationService;
import com.metamedicsvr.springtemplate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserService userService;
    private final OrganizationService organizationService;

    public Teacher createTeacher(RegisterTeacherRequest registerTeacherRequest) {
        Organization organization = organizationService.getOrganization(registerTeacherRequest.getOrganizationId());

        Teacher teacher = Teacher.builder()
                .organization(organization)
                .build();

        userService.setCommonFields(teacher, registerTeacherRequest);
        return (Teacher) userService.saveUser(teacher);
    }

}
