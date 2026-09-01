package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.LoginRequest;
import com.codecool.solarwatch.dto.LoginResponse;
import com.codecool.solarwatch.dto.RegisterRequest;
import com.codecool.solarwatch.exception.UsernameAlreadyExistsException;
import com.codecool.solarwatch.model.AppUser;
import com.codecool.solarwatch.model.Role;
import com.codecool.solarwatch.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        String password = passwordEncoder.encode(registerRequest.password());
        AppUser user = new AppUser(registerRequest.username(), password);
        user.addRole(Role.USER);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new LoginResponse(jwtService.generateToken(userDetails));
    }
}
