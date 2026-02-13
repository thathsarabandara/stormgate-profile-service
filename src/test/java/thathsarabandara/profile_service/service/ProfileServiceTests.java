package thathsarabandara.profile_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;

import thathsarabandara.profile_service.dtos.ProfilePhotoRequest;
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
    @SuppressWarnings("null")
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

    @SuppressWarnings("null")
    @Test
    void testCreateProfileMissingTenantId() {
        ResponseEntity<ProfileResponse> response = profileService.create("", profileRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tenant ID is required.", response.getBody().getMessage());
    }

    @SuppressWarnings("null")
    @Test
    void testCreateProfileMissingUserId() {
        profileRequest.setUserid(null);

        ResponseEntity<ProfileResponse> response = profileService.create("1", profileRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User ID is required.", response.getBody().getMessage());
    }

    @SuppressWarnings("null")
    @Test
    void testCreateProfileInvalidTenantId() {
        ResponseEntity<ProfileResponse> response = profileService.create("invalid", profileRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Invalid input"));
    }

    @Test
    void testCreateProfileWithoutGenderAndDob() {
        ProfileRequest request = new ProfileRequest(
                "user123",
                "USER",
                "John",
                "Doe",
                null,
                null,
                "+1234567890",
                "USA"
        );

        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        when(profileDetailRepository.save(any(ProfileDetail.class))).thenReturn(profileDetail);

        ResponseEntity<ProfileResponse> response = profileService.create("1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testCreateProfileWithDefaultRole() {
        ProfileRequest request = new ProfileRequest(
                "user123",
                null,
                "John",
                "Doe",
                null,
                null,
                "+1234567890",
                "USA"
        );

        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        when(profileDetailRepository.save(any(ProfileDetail.class))).thenReturn(profileDetail);

        ResponseEntity<ProfileResponse> response = profileService.create("1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
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
    @SuppressWarnings("null")
    void testDeleteProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ProfileResponse> response = profileService.deleteProfile("1", 999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Profile not found"));
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteProfileMissingTenantId() {
        ResponseEntity<ProfileResponse> response = profileService.deleteProfile("", 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());        assertNotNull(response.getBody());        assertEquals("Tenant ID is required.", response.getBody().getMessage());
    }

    @Test
    @SuppressWarnings("null")
    void testUploadPhotoSuccess() throws IOException {
        byte[] content = "photo content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "test.jpg",
                "image/jpeg",
                content
        );

        ProfilePhotoRequest request = new ProfilePhotoRequest();
        request.setAvater(file);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ResponseEntity<ProfileResponse> response = profileService.uploadPhoto("1", 1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile photo uploaded successfully.", response.getBody().getMessage());
    }

    @SuppressWarnings("null")
    @Test
    void testUploadPhotoMissingTenantId() throws IOException {
        byte[] content = "photo content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "test.jpg",
                "image/jpeg",
                content
        );

        ProfilePhotoRequest request = new ProfilePhotoRequest();
        request.setAvater(file);

        ResponseEntity<ProfileResponse> response = profileService.uploadPhoto("", 1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody().getMessage());
    }

    @Test
    @SuppressWarnings("null")
    void testUploadPhotoMissingFile() {
        ProfilePhotoRequest request = new ProfilePhotoRequest();
        request.setAvater(null);

        ResponseEntity<ProfileResponse> response = profileService.uploadPhoto("1", 1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Photo file is required.", response.getBody().getMessage());
    }

    @Test
    void testUploadPhotoProfileNotFound() throws IOException {
        byte[] content = "photo content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "test.jpg",
                "image/jpeg",
                content
        );

        ProfilePhotoRequest request = new ProfilePhotoRequest();
        request.setAvater(file);

        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ProfileResponse> response = profileService.uploadPhoto("1", 999L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @SuppressWarnings("null")
    void testCreateProfileDatabaseException() {
        when(profileRepository.save(any(Profile.class)))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ProfileResponse> response = profileService.create("1", profileRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Failed to create profile"));
    }

    @Test
    void testDeleteProfileException() {
        when(profileRepository.findById(1L))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ProfileResponse> response = profileService.deleteProfile("1", 1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testCreateProfileWithEmptyUserId() {
        profileRequest.setUserid("");

        ResponseEntity<ProfileResponse> response = profileService.create("1", profileRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateProfileWithDifferentGenders() {
        ProfileRequest maleRequest = new ProfileRequest(
                "user1", "USER", "John", "Doe", "MALE", LocalDate.of(1990, 1, 1), "+1234567890", "USA"
        );
        ProfileRequest femaleRequest = new ProfileRequest(
                "user2", "USER", "Jane", "Smith", "FEMALE", LocalDate.of(1992, 5, 5), "+0987654321", "UK"
        );

        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        when(profileDetailRepository.save(any(ProfileDetail.class))).thenReturn(profileDetail);

        ResponseEntity<ProfileResponse> response1 = profileService.create("1", maleRequest);
        ResponseEntity<ProfileResponse> response2 = profileService.create("1", femaleRequest);

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }
}
