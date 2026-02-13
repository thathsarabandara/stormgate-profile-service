package thathsarabandara.profile_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import thathsarabandara.profile_service.dtos.ProfileResponseDTO;
import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.ProfileCustomField;
import thathsarabandara.profile_service.model.ProfileDetail;
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

    private Profile profile;
    private ProfileDetail profileDetail;
    private List<ProfileCustomField> customFields;

    @BeforeEach
    void setUp() {
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
                .gender(ProfileDetail.Gender.MALE)
                .dob(LocalDate.of(1990, 1, 1))
                .country("USA")
                .phone("+1234567890")
                .build();

        customFields = new ArrayList<>();
        customFields.add(ProfileCustomField.builder()
                .id(1L)
                .profile(profile)
                .fieldName("company")
                .fieldValue("Tech Corp")
                .build());
    }

    @Test
    void testGetAllProfilesMissingTenantId() {
        ResponseEntity<?> response = profileGetService.getAllProfiles("", 0, 10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetAllProfilesSuccess() {
        Page<Profile> profilePage = new PageImpl<>(List.of(profile));

        when(profileRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(profilePage);
        when(profileDetailRepository.findByProfile(profile))
                .thenReturn(Optional.of(profileDetail));
        when(profileCustomFeildRepository.findByProfile(profile))
                .thenReturn(customFields);

        ResponseEntity<?> response = profileGetService.getAllProfiles("1", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Page);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetAllProfilesEmptyList() {
        Page<Profile> emptyPage = new PageImpl<>(new ArrayList<>());

        when(profileRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        ResponseEntity<?> response = profileGetService.getAllProfiles("1", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProfileByIdMissingTenantId() {
        ResponseEntity<?> response = profileGetService.getProfileByid("", 1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    void testGetProfileByIdProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileGetService.getProfileByid("1", 999L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("not found"));
    }

    @Test
    void testGetProfileByIdSuccess() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileDetailRepository.findByProfile(profile))
                .thenReturn(Optional.of(profileDetail));
        when(profileCustomFeildRepository.findByProfile(profile))
                .thenReturn(customFields);

        ResponseEntity<?> response = profileGetService.getProfileByid("1", 1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ProfileResponseDTO);
        ProfileResponseDTO dto = (ProfileResponseDTO) response.getBody();
        assertNotNull(dto);
        assertEquals("user123", dto.getUserid());
    }

    @Test
    void testGetProfileByIdUnauthorizedTenant() {
        Profile otherTenantProfile = Profile.builder()
                .id(1L)
                .tenantid(2L)
                .userid("user123")
                .build();

        when(profileRepository.findById(1L)).thenReturn(Optional.of(otherTenantProfile));

        ResponseEntity<?> response = profileGetService.getProfileByid("1", 1L, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetProfileByUserIdMissingTenantId() {
        ResponseEntity<?> response = profileGetService.getProfileByid("", null, "user123");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody());
    }

    @Test
    void testGetProfileByUserIdMissingBothIds() {
        ResponseEntity<?> response = profileGetService.getProfileByid("1", null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Either profileId or userId must be provided.", response.getBody());
    }

    @SuppressWarnings("null")
    @Test
    void testGetProfileByUserIdUserNotFound() {
        when(profileRepository.findByUserid("unknownUser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileGetService.getProfileByid("1", null, "unknownUser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("not found"));
    }

    @Test
    void testGetProfileByUserIdSuccess() {
        when(profileRepository.findByUserid("user123")).thenReturn(Optional.of(profile));
        when(profileDetailRepository.findByProfile(profile))
                .thenReturn(Optional.of(profileDetail));
        when(profileCustomFeildRepository.findByProfile(profile))
                .thenReturn(customFields);

        ResponseEntity<?> response = profileGetService.getProfileByid("1", null, "user123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ProfileResponseDTO);
    }

    @SuppressWarnings("null")
    @Test
    void testGetProfileWithoutProfileDetail() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileDetailRepository.findByProfile(profile))
                .thenReturn(Optional.empty());
        when(profileCustomFeildRepository.findByProfile(profile))
                .thenReturn(new ArrayList<>());

        ResponseEntity<?> response = profileGetService.getProfileByid("1", 1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ProfileResponseDTO);
        ProfileResponseDTO dto = (ProfileResponseDTO) response.getBody();
        assertNull(dto.getDetails());
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    void testGetAllProfilesException() {
        when(profileRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = profileGetService.getAllProfiles("1", 0, 10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Failed to get all profiles"));
    }

    @Test
    void testGetProfileWithCustomFields() {
        List<ProfileCustomField> fields = new ArrayList<>();
        fields.add(ProfileCustomField.builder()
                .id(1L)
                .fieldName("department")
                .fieldValue("Engineering")
                .build());
        fields.add(ProfileCustomField.builder()
                .id(2L)
                .fieldName("designation")
                .fieldValue("Senior Developer")
                .build());

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileDetailRepository.findByProfile(profile))
                .thenReturn(Optional.of(profileDetail));
        when(profileCustomFeildRepository.findByProfile(profile))
                .thenReturn(fields);

        ResponseEntity<?> response = profileGetService.getProfileByid("1", 1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ProfileResponseDTO);
        ProfileResponseDTO dto = (ProfileResponseDTO) response.getBody();
        assertNotNull(dto);
        assertEquals(2, dto.getCustomFields().size());
    }
}
