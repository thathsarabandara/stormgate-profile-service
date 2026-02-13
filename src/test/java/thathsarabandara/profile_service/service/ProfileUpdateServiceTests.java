package thathsarabandara.profile_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import thathsarabandara.profile_service.dtos.ProfileCustomFieldListRequest;
import thathsarabandara.profile_service.dtos.ProfileCustomFieldRequest;
import thathsarabandara.profile_service.dtos.ProfileDetailUpdateRequest;
import thathsarabandara.profile_service.dtos.ProfileResponse;
import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.ProfileCustomField;
import thathsarabandara.profile_service.model.ProfileDetail;
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

    private Profile profile;
    private ProfileDetail profileDetail;

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
    void testUpdateProfileDetailSuccess() {
        ProfileDetailUpdateRequest request = new ProfileDetailUpdateRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPhone("+9876543210");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileDetailRepository.findByProfile(profile)).thenReturn(Optional.of(profileDetail));
        when(profileDetailRepository.save(any(ProfileDetail.class))).thenReturn(profileDetail);

        ResponseEntity<ProfileResponse> response = profileUpdateService.updateProfileDetail("1", 1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile details updated successfully.", response.getBody().getMessage());
        verify(profileDetailRepository, times(1)).save(any(ProfileDetail.class));
    }

    @Test
    void testUpdateProfileDetailPartialUpdate() {
        ProfileDetailUpdateRequest request = new ProfileDetailUpdateRequest();
        request.setFirstName("Jane");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileDetailRepository.findByProfile(profile)).thenReturn(Optional.of(profileDetail));
        when(profileDetailRepository.save(any(ProfileDetail.class))).thenReturn(profileDetail);

        ResponseEntity<ProfileResponse> response = profileUpdateService.updateProfileDetail("1", 1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profileDetailRepository, times(1)).save(any(ProfileDetail.class));
    }

    @Test
    void testUpdateProfileDetailProfileDetailsNotFound() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileDetailRepository.findByProfile(profile)).thenReturn(Optional.empty());

        ResponseEntity<ProfileResponse> response = profileUpdateService.updateProfileDetail("1", 1L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("not found"));
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

    @Test
    void testAddNewCustomFields() {
        ProfileCustomFieldListRequest request = new ProfileCustomFieldListRequest();
        List<ProfileCustomFieldRequest> fields = new ArrayList<>();
        fields.add(new ProfileCustomFieldRequest());
        fields.get(0).setFieldName("department");
        fields.get(0).setFieldValue("Engineering");
        request.setFields(fields);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileCustomFeildRepository.findByProfileAndFieldName(profile, "department"))
                .thenReturn(Optional.empty());
        when(profileCustomFeildRepository.save(any(ProfileCustomField.class)))
                .thenReturn(ProfileCustomField.builder().id(1L).build());

        ResponseEntity<ProfileResponse> response = profileUpdateService.addOrUpdateCustomFields("1", 1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Custom fields added or updated successfully.", response.getBody().getMessage());
        verify(profileCustomFeildRepository, times(1)).save(any(ProfileCustomField.class));
    }

    @Test
    void testUpdateExistingCustomFields() {
        ProfileCustomFieldListRequest request = new ProfileCustomFieldListRequest();
        List<ProfileCustomFieldRequest> fields = new ArrayList<>();
        fields.add(new ProfileCustomFieldRequest());
        fields.get(0).setFieldName("department");
        fields.get(0).setFieldValue("Management");
        request.setFields(fields);

        ProfileCustomField existingField = ProfileCustomField.builder()
                .id(1L)
                .profile(profile)
                .fieldName("department")
                .fieldValue("Engineering")
                .build();

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileCustomFeildRepository.findByProfileAndFieldName(profile, "department"))
                .thenReturn(Optional.of(existingField));
        when(profileCustomFeildRepository.save(any(ProfileCustomField.class)))
                .thenReturn(existingField);

        ResponseEntity<ProfileResponse> response = profileUpdateService.addOrUpdateCustomFields("1", 1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profileCustomFeildRepository, times(1)).save(any(ProfileCustomField.class));
    }

    @Test
    void testAddOrUpdateMultipleCustomFields() {
        ProfileCustomFieldListRequest request = new ProfileCustomFieldListRequest();
        List<ProfileCustomFieldRequest> fields = new ArrayList<>();
        
        ProfileCustomFieldRequest field1 = new ProfileCustomFieldRequest();
        field1.setFieldName("department");
        field1.setFieldValue("Engineering");
        fields.add(field1);
        
        ProfileCustomFieldRequest field2 = new ProfileCustomFieldRequest();
        field2.setFieldName("designation");
        field2.setFieldValue("Senior Dev");
        fields.add(field2);
        
        request.setFields(fields);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileCustomFeildRepository.findByProfileAndFieldName(any(), anyString()))
                .thenReturn(Optional.empty());
        when(profileCustomFeildRepository.save(any(ProfileCustomField.class)))
                .thenReturn(ProfileCustomField.builder().id(1L).build());

        ResponseEntity<ProfileResponse> response = profileUpdateService.addOrUpdateCustomFields("1", 1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profileCustomFeildRepository, times(2)).save(any(ProfileCustomField.class));
    }

    @Test
    void testGetCustomFieldsByProfileIdMissingTenantId() {
        ResponseEntity<?> response = profileUpdateService.getCustomFieldsByProfileId("", 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tenant ID is required.", response.getBody());
    }

    @Test
    void testGetCustomFieldsByProfileIdProfileNotFound() {
        when(profileRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileUpdateService.getCustomFieldsByProfileId("1", 999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("not found"));
    }

    @Test
    void testGetCustomFieldsByProfileIdSuccess() {
        List<ProfileCustomField> fields = Arrays.asList(
                ProfileCustomField.builder().id(1L).fieldName("department").fieldValue("Engineering").build(),
                ProfileCustomField.builder().id(2L).fieldName("designation").fieldValue("Senior Dev").build()
        );

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileCustomFeildRepository.findByProfile(profile)).thenReturn(fields);

        ResponseEntity<?> response = profileUpdateService.getCustomFieldsByProfileId("1", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        List<?> responseList = (List<?>) response.getBody();
        assertEquals(2, responseList.size());
    }

    @Test
    void testGetCustomFieldsByProfileIdEmpty() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileCustomFeildRepository.findByProfile(profile)).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = profileUpdateService.getCustomFieldsByProfileId("1", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        List<?> responseList = (List<?>) response.getBody();
        assertEquals(0, responseList.size());
    }

    @Test
    void testUpdateProfileDetailException() {
        when(profileRepository.findById(1L))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ProfileResponse> response = profileUpdateService.updateProfileDetail("1", 1L, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testAddOrUpdateCustomFieldsException() {
        ProfileCustomFieldListRequest request = new ProfileCustomFieldListRequest();
        request.setFields(new ArrayList<>());

        when(profileRepository.findById(1L))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ProfileResponse> response = profileUpdateService.addOrUpdateCustomFields("1", 1L, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
