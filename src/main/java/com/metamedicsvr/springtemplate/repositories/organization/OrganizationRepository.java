package com.metamedicsvr.springtemplate.repositories.organization;

import com.metamedicsvr.springtemplate.entities.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

}
