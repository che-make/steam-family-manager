package com.metamedicsvr.springtemplate.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDataResponse {
    String name;
    String avatarUrl;
}
