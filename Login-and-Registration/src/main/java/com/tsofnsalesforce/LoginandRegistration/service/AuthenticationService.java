package com.tsofnsalesforce.LoginandRegistration.service;

import com.tsofnsalesforce.LoginandRegistration.Repository.TokenRepository;
import com.tsofnsalesforce.LoginandRegistration.Repository.UserRepository;
import com.tsofnsalesforce.LoginandRegistration.Response.AuthenticationResponse;
import com.tsofnsalesforce.LoginandRegistration.model.AppUser;
import com.tsofnsalesforce.LoginandRegistration.model.Role;
import com.tsofnsalesforce.LoginandRegistration.model.Token;
import com.tsofnsalesforce.LoginandRegistration.model.TokenType;
import com.tsofnsalesforce.LoginandRegistration.request.AuthenticationRequest;
import com.tsofnsalesforce.LoginandRegistration.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
//        if(repository.findByEmail(request.getEmail()).isEmpty())
//            return;
        var user = AppUser.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .Password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveAppUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    private void saveAppUserToken(AppUser appuser, String jwtToken) {
        var token = Token.builder()
                .appUser(appuser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        saveAppUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }
}
