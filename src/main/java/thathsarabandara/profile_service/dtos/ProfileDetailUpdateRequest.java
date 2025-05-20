package thathsarabandara.profile_service.dtos;

import java.time.LocalDate;
import thathsarabandara.profile_service.model.ProfileDetail.Gender;
import lombok.Data;

@Data
public class ProfileDetailUpdateRequest {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dob;
    private String country;
    private String phone;
}
