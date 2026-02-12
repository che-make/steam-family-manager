package com.metamedicsvr.springtemplate.entities.user.teacher;

import com.metamedicsvr.springtemplate.entities.organization.Organization;
import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }

}
