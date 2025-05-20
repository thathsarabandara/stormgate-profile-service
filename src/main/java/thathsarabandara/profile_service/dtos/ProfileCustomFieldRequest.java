package thathsarabandara.profile_service.dtos;

import lombok.Data;

@Data
public class ProfileCustomFieldRequest {
    private String fieldName;
    private String fieldValue;
}

