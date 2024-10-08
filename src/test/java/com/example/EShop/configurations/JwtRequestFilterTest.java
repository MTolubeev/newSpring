package com.example.EShop.configurations;

import com.example.EShop.models.CustomUserDetails;
import com.example.EShop.services.UserService;
import com.example.EShop.utils.JwtTokenUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.SignatureException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JwtRequestFilterTest {

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private UserService userDetailsService;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoFilterInternal_ValidToken() throws Exception {
        // Мокаем заголовок Authorization
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer valid-token");

        // Мокаем получение username из токена
        when(jwtTokenUtils.getUsername("valid-token")).thenReturn("test-user");

        // Мокаем получение пользователя из UserService
        CustomUserDetails userDetails = new CustomUserDetails();
        when(userDetailsService.loadUserByUsername("test-user")).thenReturn(userDetails);

        // Вызываем метод фильтра
        jwtRequestFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Проверяем, что пользователь был добавлен в SecurityContext
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
        assert SecurityContextHolder.getContext().getAuthentication() != null;
    }

    @Test
    public void testDoFilterInternal_InvalidToken() throws Exception {
        // Мокаем заголовок Authorization с неправильным токеном
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer invalid-token");

        // Мокаем выбрасывание исключения при валидации токена
        when(jwtTokenUtils.getUsername("invalid-token")).thenThrow(new SignatureException("Invalid token"));

        // Вызываем метод фильтра
        jwtRequestFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Проверяем, что фильтр был вызван, но пользователь не был добавлен в SecurityContext
        verify(filterChain, times(1)).doFilter(httpServletRequest, httpServletResponse);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
