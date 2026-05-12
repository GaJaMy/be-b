package com.liveclass.be_b.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SpringDoc OpenAPI Config
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        // 기본 문서 설정
        Info info = new Info().version("0.0.0")
                .title("Be-B 과제 API 스웨거")
                .description(
                        ""
                );

        // jwt를 사용하기 위한 문서 설정
        // 헤더에 토큰을 포함시켜 준다.
        String jwtSchemaName = "bearerAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemaName);

        Components components = new Components().addSecuritySchemes(jwtSchemaName, new SecurityScheme()
                .name(jwtSchemaName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
        );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components)
                ;
    }
}
