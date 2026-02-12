package com.metamedicsvr.springtemplate.repositories.organization;

import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, UUID> {

    Optional<SchoolClass> findByClassCode(String code);

    List<SchoolClass> findAllByOrganizationId(UUID organizationId);

    Optional<SchoolClass> findByIdAndOrganizationId(UUID schoolClassId, UUID organizationId);

}
