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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<GrantedAuthority> grantedAuthorities = appUser.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
