package thathsarabandara.profile_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import thathsarabandara.profile_service.dtos.ProfileCustomFieldListRequest;
import thathsarabandara.profile_service.dtos.ProfilePhotoRequest;
import thathsarabandara.profile_service.dtos.ProfileRequest;
import thathsarabandara.profile_service.dtos.ProfileResponse;
import thathsarabandara.profile_service.service.ProfileGetService;
import thathsarabandara.profile_service.service.ProfileService;
import thathsarabandara.profile_service.service.ProfileUpdateService;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired 
    ProfileService profileService;

    @Autowired
    ProfileUpdateService profileUpdateService;

    @Autowired
    ProfileGetService profileGetService;

    @PostMapping("/")
    public ResponseEntity<ProfileResponse> createProfile(
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @ModelAttribute ProfileRequest request) {
        return profileService.create(Tenantid, request);
    }

    @PostMapping("/{profileid}/avatar")
    public ResponseEntity<ProfileResponse> uploadAvatar(
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @PathVariable Long profileid,
        @ModelAttribute ProfilePhotoRequest request
    ) {
        return profileService.uploadPhoto(Tenantid, profileid, request);
    }

    @DeleteMapping("/{profileid}")
    public ResponseEntity<ProfileResponse> deleteProfile(
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @PathVariable Long profileid
    ) {
        return profileService.deleteProfile(Tenantid, profileid);
    }

    @PostMapping("/{profileId}/custom-fields")
    public ResponseEntity<ProfileResponse> addOrUpdateCustomFields(
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @PathVariable Long profileId,
        @ModelAttribute ProfileCustomFieldListRequest request
    ) {
        return profileUpdateService.addOrUpdateCustomFields(Tenantid, profileId, request);
    }

    @GetMapping("/{profileId}/custom-fields")
    public ResponseEntity<?> getCustomFieldsByProfileId(
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @PathVariable Long profileId
        ) {
        return profileUpdateService.getCustomFieldsByProfileId(Tenantid, profileId);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllProfiles (
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return profileGetService.getAllProfiles(Tenantid, page, size);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<?> getByProfileId (
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @PathVariable Long profileId
    ) {
        return profileGetService.getProfileByid(Tenantid, profileId, null);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserid (
        @RequestHeader(value = "Tenant-ID", required = true) String Tenantid,
        @PathVariable String userId
    ) {
        return profileGetService.getProfileByid(Tenantid, null, userId);
    }
}
