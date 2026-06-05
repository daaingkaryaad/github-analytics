package com.githubanalytics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI githubAnalyticsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("GitHub Analytics API")
                        .description("Analyze GitHub users, repositories, and language activity. "
                                + "All GitHub API communication is proxied server-side.")
                        .version("v1")
                        .contact(new Contact().name("GitHub Analytics"))
                        .license(new License().name("MIT")));
    }
}