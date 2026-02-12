package com.metamedicsvr.springtemplate.service.organization;

import com.metamedicsvr.springtemplate.dto.organization.CurrentClassResponseDto;
import com.metamedicsvr.springtemplate.dto.organization.SchoolClassesInfoResponseDto;
import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import com.metamedicsvr.springtemplate.entities.user.student.Student;
import com.metamedicsvr.springtemplate.error.exception.NotFoundException;
import com.metamedicsvr.springtemplate.repositories.organization.SchoolClassRepository;
import com.metamedicsvr.springtemplate.service.user.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    private final StudentService studentService;

    public SchoolClass getSchoolClassFromClassCode(String code) {
        return schoolClassRepository.findByClassCode(code)
                .orElseThrow(() -> new NotFoundException("SchoolClass: " + code + " not found"));
    }

    public List<SchoolClass> getSchoolClassesByOrganizationId(UUID organizationId) {
        return schoolClassRepository.findAllByOrganizationId(organizationId);
    }

    public List<SchoolClassesInfoResponseDto> toSchoolClassResponseDtoList(List<SchoolClass> schoolClasses) {
        return schoolClasses.stream()
                .map(SchoolClassesInfoResponseDto::toSchoolClassesInfoResponseDto)
                .toList();
    }

    public SchoolClass getSchoolClassFromIdAndOrganizationId(UUID schoolClassId, UUID organizationId) {
        return schoolClassRepository.findByIdAndOrganizationId(schoolClassId, organizationId)
                .orElseThrow(() -> new NotFoundException("SchoolClass with id: " + schoolClassId + "on organization: " + organizationId + " not found"));
    }

    public SchoolClassesInfoResponseDto toSchoolClassResponseDto(SchoolClass schoolClass) {
        return SchoolClassesInfoResponseDto.toSchoolClassesInfoResponseDto(schoolClass);
    }

    @Transactional
    public CurrentClassResponseDto toCurrentClassResponseDto(SchoolClass schoolClass) {
        List<Student> students = studentService.getStudentsBySchoolClassId(schoolClass.getId());
        return CurrentClassResponseDto.toCurrentClassResponseDto(schoolClass, students);
    }

    public SchoolClassesInfoResponseDto regenerateSchoolClassCode(SchoolClass schoolClass, String newCode) {
        schoolClass.setClassCode(newCode);
        schoolClassRepository.save(schoolClass);
        return toSchoolClassResponseDto(schoolClass);
    }

}
