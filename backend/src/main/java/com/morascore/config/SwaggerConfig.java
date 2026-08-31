package com.morascore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger / OpenAPI 3.
 *
 * Agrega el esquema de seguridad Bearer JWT para que todos los endpoints
 * protegidos puedan ser probados directamente desde la UI de Swagger
 * usando el botón "Authorize".
 *
 * Acceso: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MoraScore API")
                        .description("API REST para el sistema de evaluación de riesgo crediticio MoraScore")
                        .version("1.0.0"))
                // Registrar el esquema de seguridad Bearer JWT
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa el token JWT obtenido en /api/auth/login. Formato: Bearer <token>")))
                // Aplicar el esquema de seguridad a todos los endpoints por defecto
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

}
