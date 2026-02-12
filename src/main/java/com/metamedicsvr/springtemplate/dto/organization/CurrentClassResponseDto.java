package com.metamedicsvr.springtemplate.dto.organization;

import com.metamedicsvr.springtemplate.dto.user.student.ClassStudentInfoResponseDto;
import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import com.metamedicsvr.springtemplate.entities.user.student.Student;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CurrentClassResponseDto {

    private String name;

    private String classCode;

    List<ClassStudentInfoResponseDto> students;

    public static CurrentClassResponseDto toCurrentClassResponseDto(SchoolClass schoolClass, List<Student> students) {
        return CurrentClassResponseDto.builder()
                .name(schoolClass.getName())
                .classCode(schoolClass.getClassCode())
                .students(ClassStudentInfoResponseDto.toClassStudentInfoResponseDtoList(students))
                .build();
    }

}
