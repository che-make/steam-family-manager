package com.metamedicsvr.springtemplate.dto.organization;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RegenerateCodeRequestDto {

    private UUID schoolClassId;

    private String classCode;

}
