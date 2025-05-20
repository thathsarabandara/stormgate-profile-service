package thathsarabandara.profile_service.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProfilePhotoRequest {
    private MultipartFile avater;
}
