package com.metamedicsvr.springtemplate.controller.organization;

import com.metamedicsvr.springtemplate.dto.organization.CurrentClassResponseDto;
import com.metamedicsvr.springtemplate.dto.organization.OrganizationInfoResponseDto;
import com.metamedicsvr.springtemplate.dto.organization.OrganizationStatsResponseDto;
import com.metamedicsvr.springtemplate.dto.organization.RegenerateCodeRequestDto;
import com.metamedicsvr.springtemplate.dto.organization.SchoolClassesInfoResponseDto;
import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import com.metamedicsvr.springtemplate.entities.user.UserDetailsImpl;
import com.metamedicsvr.springtemplate.entities.user.teacher.Teacher;
import com.metamedicsvr.springtemplate.service.organization.OrganizationService;
import com.metamedicsvr.springtemplate.service.organization.SchoolClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("organization")
@RequiredArgsConstructor
@Tag(name = "Organization", description = "Operations related to organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final SchoolClassService schoolClassService;

    @Operation(
            summary = "Get information about the organization",
            description = "Returns information about the organization",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping
    public ResponseEntity<OrganizationInfoResponseDto> getOrganizationInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Teacher teacher = (Teacher) userDetails.getUser();

        return ResponseEntity.ok(
                organizationService.toOrganizationInfoResponseDto(
                        teacher.getOrganization()
                )
        );
    }

    @Operation(
            summary = "Get global statistics from the organization",
            description = "Returns a list of global statistics from the organization",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/stats")
    public ResponseEntity<OrganizationStatsResponseDto> getOrganizationStats(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Teacher teacher = (Teacher) userDetails.getUser();

        return ResponseEntity.ok(
                organizationService.getOrganizationStats(
                        teacher.getOrganization()
                )
        );
    }

    @Operation(
            summary = "Get all schoolClasses from the organization",
            description = "Returns a list of all schoolClasses from the organization",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/school-classes")
    public ResponseEntity<List<SchoolClassesInfoResponseDto>> getSchoolClasses(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Teacher teacher = (Teacher) userDetails.getUser();

        return ResponseEntity.ok(
                schoolClassService.toSchoolClassResponseDtoList(
                        schoolClassService.getSchoolClassesByOrganizationId(
                                teacher.getOrganization().getId()
                        )
                ));
    }

    @Operation(
            summary = "Get information about a schoolClass",
            description = "Returns information about a schoolClass",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/school-classes/{schoolClassId}")
    public ResponseEntity<CurrentClassResponseDto> getCurrentSchoolClass(
            @PathVariable UUID schoolClassId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Teacher teacher = (Teacher) userDetails.getUser();

        return ResponseEntity.ok(
                schoolClassService.toCurrentClassResponseDto(
                        schoolClassService.getSchoolClassFromIdAndOrganizationId(
                                schoolClassId,
                                organizationService.getOrganizationFromTeacher(teacher).getId()
                        )
                )
        );
    }

    @Operation(
            summary = "Regenerate the class code of a schoolClass",
            description = "Regenerate the class code of a schoolClass",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/school-classes/regenerate")
    public ResponseEntity<SchoolClassesInfoResponseDto> regenerateSchoolClassCode(
            @RequestBody RegenerateCodeRequestDto regenerateCodeRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Teacher teacher = (Teacher) userDetails.getUser();

        SchoolClass schoolClass = schoolClassService.getSchoolClassFromIdAndOrganizationId(
                regenerateCodeRequestDto.getSchoolClassId(), teacher.getOrganization().getId()
        );

        return ResponseEntity.ok(
                schoolClassService.regenerateSchoolClassCode(
                        schoolClass, regenerateCodeRequestDto.getClassCode()
                )
        );

    }

}
