package thathsarabandara.profile_service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import thathsarabandara.profile_service.dtos.ProfileRequest;
import thathsarabandara.profile_service.dtos.ProfileResponse;

@Service
public class ProfileService {

    @Transactional
    public ResponseEntity<ProfileResponse> create(String tenantid, ProfileRequest request) {
        return ResponseEntity.ok(new ProfileResponse(null, null, null));
    }

}
