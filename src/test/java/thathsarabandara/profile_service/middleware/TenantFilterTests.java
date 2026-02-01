package thathsarabandara.profile_service.middleware;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
class TenantFilterTests {

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TenantFilter tenantFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testFilterWithValidTenantId() throws ServletException, IOException {
        request.addHeader("Tenant-ID", "1");

        tenantFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilterWithMissingTenantId() throws ServletException, IOException {
        tenantFilter.doFilterInternal(request, response, filterChain);

        assertEquals(400, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void testFilterWithInvalidTenantId() throws ServletException, IOException {
        request.addHeader("Tenant-ID", "invalid");

        tenantFilter.doFilterInternal(request, response, filterChain);

        assertEquals(400, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }
}
