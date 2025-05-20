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
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            Specification<Profile> spec = (root, query, cb) -> cb.and(
                    cb.equal(root.get("tenantid"), tenantId),
                    cb.equal(root.get("isDeleted"), false)
            );

            Page<Profile> profiles = profileRepository.findAll(spec, pageable);

            Page<ProfileResponseDTO> dtoPage = profiles.map(profile -> {
            ProfileDetail detail = profileDetailRepository.findByProfile(profile).orElse(null);

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

            List<ProfileCustomField> customFieldList = profileCustomFeildRepository.findByProfile(profile);
            Map<String, Object> customFields = customFieldList.stream()
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
}