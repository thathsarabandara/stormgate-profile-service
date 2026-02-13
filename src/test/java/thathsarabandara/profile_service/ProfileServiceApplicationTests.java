package thathsarabandara.profile_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import thathsarabandara.profile_service.config.SecurityConfig;
import thathsarabandara.profile_service.controller.ProfileController;
import thathsarabandara.profile_service.middleware.TenantFilter;
import thathsarabandara.profile_service.repository.ProfileRepository;
import thathsarabandara.profile_service.service.ProfileGetService;
import thathsarabandara.profile_service.service.ProfileService;
import thathsarabandara.profile_service.service.ProfileUpdateService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ProfileServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void testProfileControllerBeanExists() {
        assertNotNull(applicationContext.getBean(ProfileController.class),
                "ProfileController bean should exist");
    }

    @Test
    void testProfileServiceBeanExists() {
        assertNotNull(applicationContext.getBean(ProfileService.class),
                "ProfileService bean should exist");
    }

    @Test
    void testProfileUpdateServiceBeanExists() {
        assertNotNull(applicationContext.getBean(ProfileUpdateService.class),
                "ProfileUpdateService bean should exist");
    }

    @Test
    void testProfileGetServiceBeanExists() {
        assertNotNull(applicationContext.getBean(ProfileGetService.class),
                "ProfileGetService bean should exist");
    }

    @Test
    void testTenantFilterBeanExists() {
        assertNotNull(applicationContext.getBean(TenantFilter.class),
                "TenantFilter bean should exist");
    }

    @Test
    void testSecurityConfigBeanExists() {
        assertNotNull(applicationContext.getBean(SecurityConfig.class),
                "SecurityConfig bean should exist");
    }

    @Test
    void testProfileRepositoryBeanExists() {
        assertNotNull(applicationContext.getBean(ProfileRepository.class),
                "ProfileRepository bean should exist");
    }
}
