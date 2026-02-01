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

import thathsarabandara.profile_service.dtos.ProfileResponse;
import thathsarabandara.profile_service.repository.ProfileCustomFeildRepository;
import thathsarabandara.profile_service.repository.ProfileDetailRepository;
import thathsarabandara.profile_service.repository.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class ProfileUpdateServiceTests {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileDetailRepository profileDetailRepository;

    @Mock
    private ProfileCustomFeildRepository profileCustomFeildRepository;

    @InjectMocks
    private ProfileUpdateService profileUpdateService;

    @BeforeEach
    void setUp() {
        // Setup for tests
    }

    @Test
    void testUpdateProfileDetailMissingTenantId() {
        ResponseEntity<ProfileResponse> response = profileUpdateService.updateProfileDetail("", 1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody().getMessage());
    }

    @Test
    void testUpdateProfileDetailProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ProfileResponse> response = profileUpdateService.updateProfileDetail("1", 999L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().toLowerCase().contains("not found"));
    }

    @Test
    void testAddOrUpdateCustomFieldsMissingTenantId() {
        ResponseEntity<ProfileResponse> response = profileUpdateService.addOrUpdateCustomFields("", 1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody().getMessage());
    }

    @Test
    void testAddOrUpdateCustomFieldsProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ProfileResponse> response = profileUpdateService.addOrUpdateCustomFields("1", 999L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().toLowerCase().contains("not found"));
    }
}
