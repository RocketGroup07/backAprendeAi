package br.com.aprendeai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
 
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aprende Aí API")
                        .version("1.0")
                        .description("API para o sistema Aprende Aí")
                        .contact(new Contact()
                                .name("Aprende Aí Team")
                                .email("contato@aprendeai.com")));
    }
}
