package com.epam.ecommerce.service;

import com.epam.ecommerce.entity.User;
import com.epam.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void whenRegisterUserThenUserIsSavedWithEncodedPassword() {
        User user = new User();
        user.setPassword("plainPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(user);

        assertNotNull(savedUser);
        assertNotEquals("plainPassword", savedUser.getPassword()); // Check that the password is encoded
        assertTrue(new BCryptPasswordEncoder().matches("plainPassword", savedUser.getPassword())); // Verify the password matches after encoding
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    void whenFindByUsernameWithExistingUserThenReturnUser() {
        User user = new User();
        user.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername("existingUser");

        assertNotNull(foundUser);
        assertEquals("existingUser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("existingUser");
    }

    @Test
    void whenFindByUsernameWithNonExistingUserThenReturnNull() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        User foundUser = userService.findByUsername("nonExistingUser");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername("nonExistingUser");
    }

    @Test
    void whenLoadUserByUsernameWithExistingUserThenReturnUserDetails() {
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("existingUser");

        assertNotNull(userDetails);
        assertEquals("existingUser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("existingUser");
    }

    @Test
    void whenLoadUserByUsernameWithNonExistingUserThenThrowException() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername("nonExistingUser"));
        assertEquals("User not found with username: nonExistingUser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonExistingUser");
    }
}
