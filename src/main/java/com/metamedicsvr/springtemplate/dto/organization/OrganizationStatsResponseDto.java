package com.metamedicsvr.springtemplate.dto.organization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationStatsResponseDto {

    private long totalStudents;

}
