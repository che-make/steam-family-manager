package com.metamedicsvr.springtemplate.dto.user.student;

import com.metamedicsvr.springtemplate.entities.user.student.Student;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class ClassStudentInfoResponseDto {

    private UUID studentId;

    private String studentName;

    public static List<ClassStudentInfoResponseDto> toClassStudentInfoResponseDtoList(List<Student> students) {
        return students.stream()
                .map(ClassStudentInfoResponseDto::toClassStudentInfoResponseDto)
                .toList();
    }

    private static ClassStudentInfoResponseDto toClassStudentInfoResponseDto(Student student) {
        return ClassStudentInfoResponseDto.builder()
                .studentId(student.getId())
                .studentName(student.getName())
                .build();
    }
}
