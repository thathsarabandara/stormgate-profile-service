package thathsarabandara.profile_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import thathsarabandara.profile_service.repository.ProfileCustomFeildRepository;
import thathsarabandara.profile_service.repository.ProfileDetailRepository;
import thathsarabandara.profile_service.repository.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class ProfileGetServiceTests {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileDetailRepository profileDetailRepository;

    @Mock
    private ProfileCustomFeildRepository profileCustomFeildRepository;

    @InjectMocks
    private ProfileGetService profileGetService;

    @BeforeEach
    void setUp() {
        // Setup for tests
    }

    @Test
    void testGetAllProfilesMissingTenantId() {
        ResponseEntity<?> response = profileGetService.getAllProfiles("", 0, 10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody());
    }

    @Test
    void testGetProfileByIdMissingTenantId() {
        ResponseEntity<?> response = profileGetService.getProfileByid("", 1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody());
    }

    @Test
    void testGetProfileByIdProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileGetService.getProfileByid("1", 999L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("not found"));
    }

    @Test
    void testGetProfileByUserIdMissingTenantId() {
        ResponseEntity<?> response = profileGetService.getProfileByid("", null, "user123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody());
    }

    @Test
    void testGetProfileByUserIdUserNotFound() {
        when(profileRepository.findByUserid("unknownUser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileGetService.getProfileByid("1", null, "unknownUser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("not found"));
    }
}
