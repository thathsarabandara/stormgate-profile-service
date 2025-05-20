package thathsarabandara.profile_service.dtos;

import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO {
    private Long id;
    private String userid;
    private String role;
    private String avatarUrl;
    private String status;
    private Details details;
    private Map<String, Object> customFields;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Details {
        private String firstName;
        private String lastName;
        private String gender;
        private LocalDate dob;
        private String country;
        private String phone;

    }
}

