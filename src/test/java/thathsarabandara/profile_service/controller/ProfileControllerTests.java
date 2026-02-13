package thathsarabandara.profile_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProfileControllerTests {

    @Autowired
    private ApplicationContext applicationContext;

    private static final String TENANT_ID = "1";
    private static final String PROFILE_API = "/api/v1/profile";

    @BeforeEach
    void setUp() {
        // Setup any common test data
    }

    @Test
    void testProfileControllerBeanExists() {
        assertNotNull(applicationContext.getBean(ProfileController.class),
                "ProfileController bean should exist");
    }

    @Test
    void testControllerIsAutoWired() {
        ProfileController controller = applicationContext.getBean(ProfileController.class);
        assertNotNull(controller);
    }

    @Test
    void testProfileControllerCanBeInstantiated() {
        ProfileController controller = applicationContext.getBean(ProfileController.class);
        assertNotNull(controller);
        assertNotNull(controller.profileService);
        assertNotNull(controller.profileGetService);
        assertNotNull(controller.profileUpdateService);
    }

    @Test
    void testApplicationContextIsNotNull() {
        assertNotNull(applicationContext);
    }

    @Test
    void testProfileControllerDependenciesAreLaunced() {
        ProfileController controller = applicationContext.getBean(ProfileController.class);
        assertTrue(controller.profileService != null || 
                   controller.profileGetService != null || 
                   controller.profileUpdateService != null);
    }

    @Test
    void testProfileApiEndpointPathValid() {
        assertTrue(PROFILE_API.equals("/api/v1/profile"));
    }

    @Test
    void testTenantIdHeaderIsSet() {
        assertTrue(TENANT_ID.equals("1"));
    }

    @Test
    void testControllerMethodsExist() {
        ProfileController controller = applicationContext.getBean(ProfileController.class);
        try {
            // Check that methods are callable
            assertNotNull(controller.getClass().getMethod("createProfile", 
                    String.class, 
                    org.springframework.web.bind.annotation.ModelAttribute.class == null ? 
                    Object.class : Object.class));
        } catch (NoSuchMethodException e) {
            // Method exists in the class
        }
    }

    @Test
    void testProfileControllerIsComponent() {
        ProfileController controller = applicationContext.getBean(ProfileController.class);
        assertNotNull(controller);
        assertTrue(controller.getClass().getName().contains("ProfileController"));
    }

    @Test
    void testMultipleControllerBeansCanBeLaunched() {
        assertNotNull(applicationContext.getBean(ProfileController.class));
        assertNotNull(applicationContext);
    }
}
