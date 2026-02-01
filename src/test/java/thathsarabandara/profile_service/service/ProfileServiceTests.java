package thathsarabandara.profile_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import thathsarabandara.profile_service.dtos.ProfileRequest;
import thathsarabandara.profile_service.dtos.ProfileResponse;
import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.ProfileDetail;
import thathsarabandara.profile_service.model.ProfileDetail.Gender;
import thathsarabandara.profile_service.repository.ProfileDetailRepository;
import thathsarabandara.profile_service.repository.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTests {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileDetailRepository profileDetailRepository;

    @InjectMocks
    private ProfileService profileService;

    private ProfileRequest profileRequest;
    private Profile profile;
    private ProfileDetail profileDetail;

    @BeforeEach
    void setUp() {
        profileRequest = new ProfileRequest(
                "user123",
                "USER",
                "John",
                "Doe",
                "MALE",
                LocalDate.of(1990, 1, 1),
                "+1234567890",
                "USA"
        );

        profile = Profile.builder()
                .id(1L)
                .tenantid(1L)
                .userid("user123")
                .role("USER")
                .status(Profile.Status.ACTIVE)
                .avatarUrl(null)
                .isDeleted(false)
                .build();

        profileDetail = ProfileDetail.builder()
                .id(1L)
                .profile(profile)
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .dob(LocalDate.of(1990, 1, 1))
                .country("USA")
                .phone("+1234567890")
                .build();
    }

    @Test
    void testCreateProfileSuccess() {
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        when(profileDetailRepository.save(any(ProfileDetail.class))).thenReturn(profileDetail);

        ResponseEntity<ProfileResponse> response = profileService.create("1", profileRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getProfileId());
        assertEquals("user123", response.getBody().getUserId());

        verify(profileRepository, times(1)).save(any(Profile.class));
        verify(profileDetailRepository, times(1)).save(any(ProfileDetail.class));
    }

    @Test
    void testCreateProfileMissingTenantId() {
        ResponseEntity<ProfileResponse> response = profileService.create("", profileRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody().getMessage());
    }

    @Test
    void testCreateProfileMissingUserId() {
        profileRequest.setUserid(null);

        ResponseEntity<ProfileResponse> response = profileService.create("1", profileRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User ID is required.", response.getBody().getMessage());
    }

    @Test
    void testCreateProfileInvalidTenantId() {
        ResponseEntity<ProfileResponse> response = profileService.create("invalid", profileRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Invalid input"));
    }

    @Test
    void testDeleteProfileSuccess() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ResponseEntity<ProfileResponse> response = profileService.deleteProfile("1", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profileRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testDeleteProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ProfileResponse> response = profileService.deleteProfile("1", 999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Profile not found"));
    }

    @Test
    void testDeleteProfileMissingTenantId() {
        ResponseEntity<ProfileResponse> response = profileService.deleteProfile("", 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody().getMessage());
    }
}
