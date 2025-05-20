package thathsarabandara.profile_service.dtos;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileCustomFieldResponse {
    private Long id;
    private String fieldName;
    private String fieldValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}