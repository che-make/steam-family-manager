package com.metamedicsvr.springtemplate.controller.user;

import com.metamedicsvr.springtemplate.dto.user.UserDataResponse;
import com.metamedicsvr.springtemplate.entities.user.User;
import com.metamedicsvr.springtemplate.entities.user.UserDetailsImpl;
import com.metamedicsvr.springtemplate.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Operations related to users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Check if a user exists by email",
            description = "Returns whether a user exists with the specified email."
    )
    @PermitAll
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkUserExists(@RequestParam String email) {
        boolean exists = userService.userExistByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @Operation(
            summary = "Get user data by token",
            description = "Returns user data by token.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/data")
    public ResponseEntity<UserDataResponse> getUserData(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.getUserDataResponse(userDetails.getUserId()));
    }

    @Operation(
            summary = "Get user photo",
            description = "Returns the user photo",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/photo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserPhotoResponse> getUserPhoto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                UserPhotoResponse.builder().
                        avatarUrl(userService.getUserPhoto(userDetails.getUser()))
                        .build()
        );
    }


    @Operation(
            summary = "Upload user photo",
            description = "Uploads the user photo",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(value = "photo", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserPhotoResponse> uploadUserPhoto(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart MultipartFile file
    ) {
        return ResponseEntity.ok(
                UserPhotoResponse.builder()
                        .avatarUrl(userService.uploadProfilePhoto(file, userDetails.getUser())
                        )
                        .build());
    }

    @Operation(
            summary = "Get user photo from a specific user id",
            description = "Returns the user photo",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("{userId}/photo")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<UserPhotoResponse> getUserPhoto(@PathVariable UUID userId) {
        User user = userService.getUserByUUID(userId);
        return ResponseEntity.ok(
                UserPhotoResponse.builder()
                        .avatarUrl(userService.getUserPhoto(user))
                        .build()
        );
    }


}