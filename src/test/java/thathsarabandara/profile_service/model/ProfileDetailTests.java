package thathsarabandara.profile_service.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ProfileDetailTests {

    @Test
    void testProfileDetailBuilder() {
        Profile profile = Profile.builder()
                .id(1L)
                .tenantid(1L)
                .userid("user123")
                .build();

        ProfileDetail detail = ProfileDetail.builder()
                .id(1L)
                .profile(profile)
                .firstName("John")
                .lastName("Doe")
                .gender(ProfileDetail.Gender.MALE)
                .dob(LocalDate.of(1990, 1, 1))
                .country("USA")
                .phone("+1234567890")
                .build();

        assertEquals(1L, detail.getId());
        assertEquals("John", detail.getFirstName());
        assertEquals("Doe", detail.getLastName());
        assertEquals(ProfileDetail.Gender.MALE, detail.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), detail.getDob());
        assertEquals("USA", detail.getCountry());
        assertEquals("+1234567890", detail.getPhone());
    }

    @Test
    void testProfileDetailGenderEnum() {
        assertEquals("MALE", ProfileDetail.Gender.MALE.name());
        assertEquals("FEMALE", ProfileDetail.Gender.FEMALE.name());
        assertEquals("OTHER", ProfileDetail.Gender.OTHER.name());
    }

    @Test
    void testProfileDetailNullableFields() {
        ProfileDetail detail = new ProfileDetail();
        detail.setId(1L);

        assertNull(detail.getFirstName());
        assertNull(detail.getLastName());
        assertNull(detail.getGender());
        assertNull(detail.getDob());
        assertNull(detail.getCountry());
        assertNull(detail.getPhone());
    }
}
