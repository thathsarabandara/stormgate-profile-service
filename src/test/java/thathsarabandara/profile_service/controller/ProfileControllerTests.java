package thathsarabandara.profile_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String TENANT_ID = "1";
    private static final String PROFILE_API = "/api/v1/profile";

    @BeforeEach
    void setUp() {
        // Setup any common test data
    }

    @Test
    void testCreateProfileEndpointExists() throws Exception {
        mockMvc.perform(post(PROFILE_API + "/")
                .header("Tenant-ID", TENANT_ID));
    }

    @Test
    void testGetAllProfilesEndpointExists() throws Exception {
        mockMvc.perform(get(PROFILE_API + "/")
                .header("Tenant-ID", TENANT_ID)
                .param("page", "0")
                .param("size", "10"));
    }

    @Test
    void testGetProfileByIdEndpointExists() throws Exception {
        mockMvc.perform(get(PROFILE_API + "/profile/1")
                .header("Tenant-ID", TENANT_ID));
    }

    @Test
    void testGetProfileByUserIdEndpointExists() throws Exception {
        mockMvc.perform(get(PROFILE_API + "/user/user123")
                .header("Tenant-ID", TENANT_ID));
    }

    @Test
    void testDeleteProfileEndpointExists() throws Exception {
        mockMvc.perform(delete(PROFILE_API + "/1")
                .header("Tenant-ID", TENANT_ID));
    }

    @Test
    void testUploadAvatarEndpointExists() throws Exception {
        mockMvc.perform(post(PROFILE_API + "/1/avatar")
                .header("Tenant-ID", TENANT_ID));
    }
}
