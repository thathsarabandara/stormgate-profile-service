package thathsarabandara.profile_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.ProfileCustomField;

public interface ProfileCustomFeildRepository extends JpaRepository<ProfileCustomField, Long> {
    List<ProfileCustomField> findByProfile(Profile profile);
    Optional<ProfileCustomField> findByProfileAndFieldName(Profile profile, String fieldName);
}
