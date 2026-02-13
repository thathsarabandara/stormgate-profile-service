package thathsarabandara.profile_service.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProfileTests {

    @Test
    void testProfileBuilder() {
        Profile profile = Profile.builder()
                .id(1L)
                .tenantid(1L)
                .userid("user123")
                .role("USER")
                .status(Profile.Status.ACTIVE)
                .avatarUrl(null)
                .isDeleted(false)
                .build();

        assertEquals(1L, profile.getId());
        assertEquals(1L, profile.getTenantid());
        assertEquals("user123", profile.getUserid());
        assertEquals("USER", profile.getRole());
        assertEquals(Profile.Status.ACTIVE, profile.getStatus());
        assertNull(profile.getAvatarUrl());
        assertFalse(profile.getIsDeleted());
    }

    @Test
    void testProfileStatusEnum() {
        assertEquals("ACTIVE", Profile.Status.ACTIVE.name());
        assertEquals("INACTIVE", Profile.Status.INACTIVE.name());
        assertEquals("PENDING", Profile.Status.PENDING.name());
        assertEquals("SUSPENDED", Profile.Status.SUSPENDED.name());
    }

    @Test
    void testProfileDefaultValues() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setTenantid(1L);
        profile.setUserid("test");

        assertFalse(profile.getIsDeleted());
        assertNotNull(profile.getIsDeleted());
    }

    @Test
    void testProfileSetter() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setTenantid(1L);
        profile.setUserid("user123");
        profile.setRole("ADMIN");
        profile.setStatus(Profile.Status.INACTIVE);
        profile.setAvatarUrl("http://example.com/avatar.jpg");
        profile.setIsDeleted(true);

        assertEquals(1L, profile.getId());
        assertEquals(1L, profile.getTenantid());
        assertEquals("user123", profile.getUserid());
        assertEquals("ADMIN", profile.getRole());
        assertEquals(Profile.Status.INACTIVE, profile.getStatus());
        assertEquals("http://example.com/avatar.jpg", profile.getAvatarUrl());
        assertTrue(profile.getIsDeleted());
    }
}
