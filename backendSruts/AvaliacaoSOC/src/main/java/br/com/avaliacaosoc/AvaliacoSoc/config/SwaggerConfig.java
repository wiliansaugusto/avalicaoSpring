package br.com.avaliacaosoc.AvaliacoSoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Avaliaco API")
                        .version("1.0.1")
                        .description("Avaliacao de processo seletivo de empresa")
                        .contact(new Contact()
                                .name("Willians Augusto")
                                .email("wiliansaugusto@gmail.com")
                                .url("https://www.linkedin.com/in/williansaugusto"))
                );
    }
}
