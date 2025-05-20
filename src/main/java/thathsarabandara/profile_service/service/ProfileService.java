package thathsarabandara.profile_service.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import thathsarabandara.profile_service.dtos.ProfilePhotoRequest;
import thathsarabandara.profile_service.dtos.ProfileRequest;
import thathsarabandara.profile_service.dtos.ProfileResponse;
import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.Profile.Status;
import thathsarabandara.profile_service.model.ProfileDetail;
import thathsarabandara.profile_service.model.ProfileDetail.Gender;
import thathsarabandara.profile_service.repository.ProfileDetailRepository;
import thathsarabandara.profile_service.repository.ProfileRepository;
@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileDetailRepository profileDetailRepository;

    @Transactional
    public ResponseEntity<ProfileResponse> create(String tenantId, ProfileRequest request) {
        try {
            if (tenantId == null || tenantId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfileResponse(null, null, "Tenant ID is required."));
            }

            if (request.getUserid() == null || request.getUserid().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfileResponse(null, null, "User ID is required."));
            }

            Profile profile = Profile.builder()
                    .tenantid(Long.getLong(tenantId))
                    .userid(request.getUserid())
                    .role(request.getRole() != null ? request.getRole() : "USER")
                    .avatarUrl(null)
                    .status(Status.ACTIVE)
                    .build();

            profile = profileRepository.save(profile);

            final ProfileDetail detail = ProfileDetail.builder()
                    .profile(profile)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .gender(request.getGender() != null ? Gender.valueOf(request.getGender().toUpperCase()) : null)
                    .dob(request.getDob())
                    .country(request.getCountry())
                    .phone(request.getPhone())
                    .build();

            profileDetailRepository.save(detail);

            final ProfileResponse response = new ProfileResponse(
                    profile.getId(),
                    profile.getUserid(),
                    "Profile created successfully."
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProfileResponse(null, null, "Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileResponse(null, null, "Failed to create profile: " + e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<ProfileResponse> uploadPhoto(String tenantid, Long profileid, ProfilePhotoRequest request) {
        try {
            if (tenantid == null || tenantid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfileResponse(null, null, "Tenant ID is required."));
            }

            if (request.getAvater() == null || request.getAvater().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfileResponse(null, null, "Photo file is required."));
            }

            final Profile profile = profileRepository.findById(profileid)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found for ID: " + profileid));

            final String uploadDir = "uploads/avatars/";
            final String fileName = "avatar_" + profileid + "_" + request.getAvater().getOriginalFilename();
            final Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            final Path filePath = uploadPath.resolve(fileName);
            Files.copy(request.getAvater().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            profile.setAvatarUrl(filePath.toString());
            profileRepository.save(profile);

            final ProfileResponse response = new ProfileResponse(
                    profile.getId(),
                    profile.getUserid(),
                    "Profile photo uploaded successfully."
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProfileResponse(null, null, "Invalid input: " + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileResponse(null, null, "Failed to save photo: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileResponse(null, null, "Failed to upload photo: " + e.getMessage()));
        }
    }
    @Transactional
    public ResponseEntity<ProfileResponse> deleteProfile(String tenantid, Long profileId) {
        try {
            if (tenantid == null || tenantid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ProfileResponse(null, null, "Tenant ID is required."));
            }
            
            final Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found for ID: " + profileId));

            profile.setIsDeleted(true);  
            profileRepository.save(profile);

            final ProfileResponse response = new ProfileResponse(
                    profile.getId(),
                    profile.getUserid(),
                    "Profile deactivated successfully."
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ProfileResponse(null, null, "Profile not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProfileResponse(null, null, "Failed to deactivate profile: " + e.getMessage()));
        }
    }

}
