package com.LibreriaApi.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "ReviúBook",
                description = "API para reseñas de libros",
                termsOfService = "https://www.terminos.com",
                version = "1.0.0",
                contact = @Contact(
                        name = "GETMAN",
                        url = "https://github.com/GETMAN",
                        email = "default@gmail.com"
                ),
                license = @License(
                        name = "Sin licencia, usala tranqui"
                )
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8080"
                )
        },
        security = @SecurityRequirement(
                name = "SecurityToken"
        )
)
@SecurityScheme(
        name = "SecurityToken",
        description = "Access token for the API",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
