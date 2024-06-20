package com.example.EShop.services;


import com.example.EShop.models.User;
import com.example.EShop.models.enums.Role;
import com.example.EShop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        boolean result = userService.createUser(user);

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_NewUser() {
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        boolean result = userService.createUser(user);

        assertTrue(result);
        assertTrue(user.isActive());
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testList() {
        List<User> expectedUsers = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.list();

        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testBanUser_UserExistsAndActive() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.banUser(1L);

        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testBanUser_UserExistsAndInactive() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setActive(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.banUser(1L);

        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testBanUser_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        userService.banUser(1L);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testChangeUserRoles() {
        User user = new User();
        Map<String, String> form = new HashMap<>();
        form.put("ROLE_ADMIN", "on");
        form.put("ROLE_USER", "on");

        userService.changeUserRoles(user, form);

        assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testReturnFirstLetter() {
        User user = new User();
        user.setName("Alice");

        char result = userService.returnFirstLetter(user);

        assertEquals('A', result);
    }
}
