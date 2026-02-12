package com.metamedicsvr.springtemplate.entities.user.admin;

import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.enums.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Entity
public class Admin extends User {

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

}
