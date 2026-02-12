package com.metamedicsvr.springtemplate.dto.organization;

import com.metamedicsvr.springtemplate.entities.organization.Organization;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrganizationInfoResponseDto {

    private UUID id;

    private String name;

    public static OrganizationInfoResponseDto toOrganizationInfoResponseDto(Organization organization) {
        return OrganizationInfoResponseDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .build();
    }

}
