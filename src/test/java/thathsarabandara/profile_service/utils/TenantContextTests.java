package thathsarabandara.profile_service.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantContextTests {

    @BeforeEach
    void setUp() {
        TenantContext.clear();
    }

    @Test
    void testSetAndGetTenantId() {
        TenantContext.setTenantID(1L);
        assertEquals(1L, TenantContext.getTenantId());
    }

    @Test
    void testSetMultipleTenantIds() {
        TenantContext.setTenantID(1L);
        assertEquals(1L, TenantContext.getTenantId());
        
        TenantContext.setTenantID(2L);
        assertEquals(2L, TenantContext.getTenantId());
    }

    @Test
    void testClearTenantContext() {
        TenantContext.setTenantID(1L);
        assertEquals(1L, TenantContext.getTenantId());
        
        TenantContext.clear();
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void testGetTenantIdWhenNotSet() {
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void testMultipleOperations() {
        assertNull(TenantContext.getTenantId());
        
        TenantContext.setTenantID(5L);
        assertEquals(5L, TenantContext.getTenantId());
        
        TenantContext.setTenantID(10L);
        assertEquals(10L, TenantContext.getTenantId());
        
        TenantContext.clear();
        assertNull(TenantContext.getTenantId());
        
        TenantContext.setTenantID(15L);
        assertEquals(15L, TenantContext.getTenantId());
    }

    @Test
    void testNegativeTenantId() {
        TenantContext.setTenantID(-1L);
        assertEquals(-1L, TenantContext.getTenantId());
    }

    @Test
    void testZeroTenantId() {
        TenantContext.setTenantID(0L);
        assertEquals(0L, TenantContext.getTenantId());
    }

    @Test
    void testLargeTenantId() {
        long largeTenantId = Long.MAX_VALUE;
        TenantContext.setTenantID(largeTenantId);
        assertEquals(largeTenantId, TenantContext.getTenantId());
    }
}
