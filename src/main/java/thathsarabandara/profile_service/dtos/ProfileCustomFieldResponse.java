package thathsarabandara.profile_service.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileCustomFieldResponse {
    private Long id;
    private String fieldName;
    private String fieldValue;
}