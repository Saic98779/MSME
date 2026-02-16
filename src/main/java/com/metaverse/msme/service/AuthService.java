package com.metaverse.msme.service;

import com.metaverse.msme.common.AuthResponse;
import com.metaverse.msme.common.LoginRequest;
import com.metaverse.msme.common.RegisterRequest;
import com.metaverse.msme.model.User;
import com.metaverse.msme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) throws Exception {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Email already registered");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("Username already taken");
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobileNo(request.getMobileNo())
                .userRole(request.getUserRole() != null ? request.getUserRole() : "USER")
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getUserRole()
        );

        return AuthResponse.builder()
                .token(token)
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .userRole(savedUser.getUserRole())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .message("Registration successful")
                .build();
    }

    public AuthResponse login(LoginRequest request) throws Exception {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (!userOptional.isPresent()) {
            throw new Exception("User not found");
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        // Check user status
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new Exception("User account is not active");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getUserRole()
        );

        return AuthResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .message("Login successful")
                .build();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}

