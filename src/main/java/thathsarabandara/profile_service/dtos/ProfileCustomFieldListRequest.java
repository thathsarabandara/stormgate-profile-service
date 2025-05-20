package thathsarabandara.profile_service.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ProfileCustomFieldListRequest {
    private List<ProfileCustomFieldRequest> fields;
}
