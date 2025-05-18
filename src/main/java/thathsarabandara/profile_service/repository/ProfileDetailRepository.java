package thathsarabandara.profile_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import thathsarabandara.profile_service.model.Profile;
import thathsarabandara.profile_service.model.ProfileDetail;

public interface ProfileDetailRepository extends JpaRepository<ProfileDetail, Long> {
    Optional<ProfileDetail> findByProfile(Profile profile);
}
