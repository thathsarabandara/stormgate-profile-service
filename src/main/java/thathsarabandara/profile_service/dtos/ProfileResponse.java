package thathsarabandara.profile_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileResponse {
    private Long profileId;
    private String userId;
    private String message;
}
