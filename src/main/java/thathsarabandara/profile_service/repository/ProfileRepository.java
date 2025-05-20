package thathsarabandara.profile_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import thathsarabandara.profile_service.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findbyUserId(String userID);
}
