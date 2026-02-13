package thathsarabandara.profile_service.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testSecurityConfigBeanExists() {
        assertNotNull(applicationContext.getBean(SecurityConfig.class),
                "SecurityConfig bean should exist");
    }

    @Test
    void testSecurityFilterChainBeanExists() {
        SecurityFilterChain filterChain = applicationContext.getBean(SecurityFilterChain.class);
        assertNotNull(filterChain, "SecurityFilterChain bean should exist");
    }

    @Test
    void testSecurityConfigNotNull() {
        SecurityConfig config = applicationContext.getBean(SecurityConfig.class);
        assertNotNull(config);
    }

    @Test
    void testApplicationContextLoads() {
        assertNotNull(applicationContext);
    }
}
