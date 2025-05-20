package thathsarabandara.profile_service.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import thathsarabandara.profile_service.dtos.ProfileResponseDTO;
import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.ProfileCustomField;
import thathsarabandara.profile_service.model.ProfileDetail;
import thathsarabandara.profile_service.repository.ProfileCustomFeildRepository;
import thathsarabandara.profile_service.repository.ProfileDetailRepository;
import thathsarabandara.profile_service.repository.ProfileRepository;

@Service
public class ProfileGetService {

    @Autowired 
    ProfileRepository profileRepository;

    @Autowired
    ProfileDetailRepository profileDetailRepository;

    @Autowired
    ProfileCustomFeildRepository profileCustomFeildRepository;

    @Transactional
    public ResponseEntity<?> getAllProfiles(String tenantId, int page, int size) {
        try {
            if (tenantId == null || tenantId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Tenant ID is required.");
            }
            final Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            final Specification<Profile> spec = (root, query, cb) -> cb.and(
                    cb.equal(root.get("tenantid"), tenantId),
                    cb.equal(root.get("isDeleted"), false)
            );

            final Page<Profile> profiles = profileRepository.findAll(spec, pageable);

            final Page<ProfileResponseDTO> dtoPage = profiles.map(profile -> {
            final ProfileDetail detail = profileDetailRepository.findByProfile(profile).orElse(null);

            ProfileResponseDTO.Details detailsDto = null;
            if (detail != null) {
                detailsDto = new ProfileResponseDTO.Details(
                    detail.getFirstName(),
                    detail.getLastName(),
                    detail.getGender() != null ? detail.getGender().name().toLowerCase() : null,
                    detail.getDob(),
                    detail.getCountry(),
                    detail.getPhone()
                );
            }

            final List<ProfileCustomField> customFieldList = profileCustomFeildRepository.findByProfile(profile);
            final Map<String, Object> customFields = customFieldList.stream()
                .collect(Collectors.toMap(
                    ProfileCustomField::getFieldName,
                    ProfileCustomField::getFieldValue
                ));

            return new ProfileResponseDTO(
                profile.getId(),
                profile.getUserid(),
                profile.getRole(),
                profile.getAvatarUrl(),
                profile.getStatus().name().toLowerCase(),
                detailsDto,
                customFields
            );
        });

            return ResponseEntity.ok(dtoPage);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get all profiles: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> getProfileByid(String tenantId, Long profileId, String userId) {
        try {
            if (tenantId == null || tenantId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Tenant ID is required.");
            }

            if (profileId == null && (userId == null || userId.isEmpty())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Either profileId or userId must be provided.");
            }

            final Profile profile;

            if (profileId != null) {
                profile = profileRepository.findById(profileId)
                        .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));
            } else {
                profile = profileRepository.findByUserid(userId)
                        .orElseThrow(() -> new RuntimeException("Profile not found with userId: " + userId));
            }

            if (!Long.valueOf(tenantId).equals(profile.getTenantid())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Unauthorized access to this profile.");
            }

            final ProfileDetail detail = profileDetailRepository.findByProfile(profile).orElse(null);

            ProfileResponseDTO.Details detailsDto = null;
            if (detail != null) {
                detailsDto = new ProfileResponseDTO.Details(
                        detail.getFirstName(),
                        detail.getLastName(),
                        detail.getGender() != null ? detail.getGender().name().toLowerCase() : null,
                        detail.getDob(),
                        detail.getCountry(),
                        detail.getPhone()
                );
            }

            final List<ProfileCustomField> customFieldList = profileCustomFeildRepository.findByProfile(profile);
            final Map<String, Object> customFields = customFieldList.stream()
                    .collect(Collectors.toMap(
                            ProfileCustomField::getFieldName,
                            ProfileCustomField::getFieldValue
                    ));

            // Map to response DTO
            final ProfileResponseDTO responseDto = new ProfileResponseDTO(
                    profile.getId(),
                    profile.getUserid(),
                    profile.getRole(),
                    profile.getAvatarUrl(),
                    profile.getStatus().name().toLowerCase(),
                    detailsDto,
                    customFields
            );

            return ResponseEntity.ok(responseDto);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get profile detail: " + e.getMessage());
        }
    }
}