package thathsarabandara.profile_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileResponse {
    private String message;
    private Long profileId;
    private String userId;
}
