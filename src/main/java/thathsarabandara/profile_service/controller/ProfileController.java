package thathsarabandara.profile_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import thathsarabandara.profile_service.dtos.ProfilePhotoRequest;
import thathsarabandara.profile_service.dtos.ProfileRequest;
import thathsarabandara.profile_service.dtos.ProfileResponse;
import thathsarabandara.profile_service.service.ProfileService;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired ProfileService profileService;
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
}
