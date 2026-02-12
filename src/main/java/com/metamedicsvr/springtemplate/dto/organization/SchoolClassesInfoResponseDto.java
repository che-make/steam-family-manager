package com.metamedicsvr.springtemplate.dto.organization;

import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SchoolClassesInfoResponseDto {

    private UUID id;

    private String name;

    private String classCode;

    public static SchoolClassesInfoResponseDto toSchoolClassesInfoResponseDto(SchoolClass schoolClass) {
        return SchoolClassesInfoResponseDto.builder()
                .id(schoolClass.getId())
                .name(schoolClass.getName())
                .classCode(schoolClass.getClassCode())
                .build();
    }
}
