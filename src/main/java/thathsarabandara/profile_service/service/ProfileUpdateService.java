package thathsarabandara.profile_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import thathsarabandara.profile_service.dtos.ProfileCustomFieldListRequest;
import thathsarabandara.profile_service.dtos.ProfileCustomFieldRequest;
import thathsarabandara.profile_service.dtos.ProfileCustomFieldResponse;
import thathsarabandara.profile_service.dtos.ProfileDetailUpdateRequest;
import thathsarabandara.profile_service.dtos.ProfileResponse;
import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.ProfileCustomField;
import thathsarabandara.profile_service.model.ProfileDetail;
import thathsarabandara.profile_service.repository.ProfileCustomFeildRepository;
import thathsarabandara.profile_service.repository.ProfileDetailRepository;
import thathsarabandara.profile_service.repository.ProfileRepository;

@Service
public class ProfileUpdateService {
    @Autowired 
    ProfileRepository profileRepository;

    @Autowired 
    ProfileDetailRepository profileDetailRepository;

    @Autowired
    ProfileCustomFeildRepository profileCustomFeildRepository;

    @Transactional
    public ResponseEntity<ProfileResponse> updateProfileDetail(String tenantid, Long profileId, ProfileDetailUpdateRequest request) {
        try {
            if (tenantid == null || tenantid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfileResponse(null, null, "Tenant ID is required."));
            }
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found for ID: " + profileId));

            ProfileDetail profileDetail = profileDetailRepository.findByProfile(profile)
                    .orElseThrow(() -> new IllegalArgumentException("Profile details not found for this profile."));

            if (request.getFirstName() != null) profileDetail.setFirstName(request.getFirstName());
            if (request.getLastName() != null) profileDetail.setLastName(request.getLastName());
            if (request.getGender() != null) profileDetail.setGender(request.getGender());
            if (request.getDob() != null) profileDetail.setDob(request.getDob());
            if (request.getCountry() != null) profileDetail.setCountry(request.getCountry());
            if (request.getPhone() != null) profileDetail.setPhone(request.getPhone());

            profileDetailRepository.save(profileDetail);

            ProfileResponse response = new ProfileResponse(
                    profile.getId(),
                    profile.getUserid(),
                    "Profile details updated successfully."
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ProfileResponse(null, null, "Not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileResponse(null, null, "Failed to update profile details: " + e.getMessage()));
        }
    }
    @Transactional
    public ResponseEntity<ProfileResponse> addOrUpdateCustomFields(String tenantid, Long profileId, ProfileCustomFieldListRequest request) {
        try {
            if (tenantid == null || tenantid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfileResponse(null, null, "Tenant ID is required."));
            }
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found for ID: " + profileId));

            for (ProfileCustomFieldRequest fieldRequest : request.getFields()) {
                Optional<ProfileCustomField> existingFieldOpt = profileCustomFeildRepository.findByProfileAndFieldName(profile, fieldRequest.getFieldName());

                if (existingFieldOpt.isPresent()) {

                    ProfileCustomField existingField = existingFieldOpt.get();
                    existingField.setFieldValue(fieldRequest.getFieldValue());
                    profileCustomFeildRepository.save(existingField);
                } else {

                    ProfileCustomField newField = ProfileCustomField.builder()
                            .profile(profile)
                            .fieldName(fieldRequest.getFieldName())
                            .fieldValue(fieldRequest.getFieldValue())
                            .build();
                    profileCustomFeildRepository.save(newField);
                }
            }

            return ResponseEntity.ok(new ProfileResponse(profile.getId(), profile.getUserid(), "Custom fields added or updated successfully."));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ProfileResponse(null, null, "Not Found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileResponse(null, null, "Failed to update profile custom details: " + e.getMessage()));
        }
    }
    @Transactional
    public ResponseEntity<?> getCustomFieldsByProfileId(String tenantid, Long profileId) {
        try {
            if (tenantid == null || tenantid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Tenant ID is required.");
            }
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found for ID: " + profileId));

            List<ProfileCustomField> customFields = profileCustomFeildRepository.findByProfile(profile);

            List<ProfileCustomFieldResponse> responseList = customFields.stream()
                    .map(field -> ProfileCustomFieldResponse.builder()
                            .id(field.getId())
                            .fieldName(field.getFieldName())
                            .fieldValue(field.getFieldValue())
                            .build())
                    .toList();

            return ResponseEntity.ok(responseList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch custom fields: " + e.getMessage());
        }
    }
    
}
