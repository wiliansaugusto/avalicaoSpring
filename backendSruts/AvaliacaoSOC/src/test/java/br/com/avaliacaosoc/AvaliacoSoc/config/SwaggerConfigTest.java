package br.com.avaliacaosoc.AvaliacoSoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SwaggerConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testCustomOpenAPICreation() {
        OpenAPI openAPI = applicationContext.getBean(OpenAPI.class);
        assertThat(openAPI).isNotNull();

        Info info = openAPI.getInfo();
        assertThat(info).isNotNull();
        assertThat(info.getTitle()).isEqualTo("Avaliaco API");
        assertThat(info.getVersion()).isEqualTo("1.0.1");
        assertThat(info.getDescription()).isEqualTo("Avaliacao de processo seletivo de empresa");

        Contact contact = info.getContact();
        assertThat(contact).isNotNull();
        assertThat(contact.getName()).isEqualTo("Willians Augusto");
        assertThat(contact.getEmail()).isEqualTo("wiliansaugusto@gmail.com");
        assertThat(contact.getUrl()).isEqualTo("https://www.linkedin.com/in/williansaugusto");
    }
}
