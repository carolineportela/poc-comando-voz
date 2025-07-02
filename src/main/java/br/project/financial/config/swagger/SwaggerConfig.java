package br.project.financial.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("Financial API")
                .version("1.0")
                .description("Financial Transactions API Documentation"));
    }

    @Bean
    public GroupedOpenApi transactionsGroup() {
        return GroupedOpenApi.builder()
                .group("Transactions")
                .pathsToMatch("/v1/transactions/**")
                .build();
    }
}
