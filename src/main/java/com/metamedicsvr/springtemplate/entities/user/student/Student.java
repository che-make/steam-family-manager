package com.metamedicsvr.springtemplate.entities.user.student;

import com.metamedicsvr.springtemplate.entities.organization.schoolclasses.SchoolClass;
import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "schoolClassId")
    private SchoolClass schoolClass;

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

}
