package thathsarabandara.profile_service.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String userid;
    private String role;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dob;
    private String phone;
}
