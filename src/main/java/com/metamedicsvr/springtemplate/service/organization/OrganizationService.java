package com.metamedicsvr.springtemplate.service.organization;

import com.metamedicsvr.springtemplate.dto.organization.OrganizationInfoResponseDto;
import com.metamedicsvr.springtemplate.dto.organization.OrganizationStatsResponseDto;
import com.metamedicsvr.springtemplate.entities.organization.Organization;
import com.metamedicsvr.springtemplate.entities.user.teacher.Teacher;
import com.metamedicsvr.springtemplate.error.exception.NotFoundException;
import com.metamedicsvr.springtemplate.repositories.organization.OrganizationRepository;
import com.metamedicsvr.springtemplate.service.user.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final StudentService studentService;

    public Organization getOrganization(UUID organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization: " + organizationId + " not found"));
    }

    @Transactional
    public Organization getOrganizationFromTeacher(Teacher teacher) {
        return teacher.getOrganization();
    }

    public OrganizationInfoResponseDto toOrganizationInfoResponseDto(Organization organization) {
        return OrganizationInfoResponseDto.toOrganizationInfoResponseDto(organization);
    }

    public OrganizationStatsResponseDto getOrganizationStats(Organization organization){
        long totalStudents = studentService.countStudentsByOrganizationId(organization.getId());

        return OrganizationStatsResponseDto.builder()
                .totalStudents(totalStudents)
                .build();

    }

}
