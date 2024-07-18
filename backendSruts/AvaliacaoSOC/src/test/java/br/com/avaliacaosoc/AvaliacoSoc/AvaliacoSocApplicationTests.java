package br.com.avaliacaosoc.AvaliacoSoc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AvaliacoSocApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;
	@Test
	void contextLoads() {

	}
    @Test
    void testBeansPresence() {
        assertThat(applicationContext.containsBean("avaliacoSocApplication")).isTrue();
    }

    @Test
    void main() {
        AvaliacoSocApplication.main(new String[] {});
        assertThat(applicationContext).isNotNull();
    }
}
