package com.metamedicsvr.springtemplate.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Spring Template API",
                version = "1.0",
                description = "Spring Template API Documentation",
                contact = @Contact(
                        name = "MetamedicsVR",
                        email = "info@metamedicsvr.com"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Authentication based on JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
