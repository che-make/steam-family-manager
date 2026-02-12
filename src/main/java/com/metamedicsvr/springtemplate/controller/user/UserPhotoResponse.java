package com.metamedicsvr.springtemplate.controller.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPhotoResponse {

    private String avatarUrl;

}
