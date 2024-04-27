package com.tsofnsalesforce.LoginandRegistration.service;

import com.tsofnsalesforce.LoginandRegistration.Repository.RoleRepository;
import com.tsofnsalesforce.LoginandRegistration.Repository.TokenRepository;
import com.tsofnsalesforce.LoginandRegistration.Repository.UserRepository;
import com.tsofnsalesforce.LoginandRegistration.Response.AddPermissionResponse;
import com.tsofnsalesforce.LoginandRegistration.Response.DeletePermissionResponse;
import com.tsofnsalesforce.LoginandRegistration.model.AppUser;
import com.tsofnsalesforce.LoginandRegistration.model.Role;
import com.tsofnsalesforce.LoginandRegistration.model.Token;
import com.tsofnsalesforce.LoginandRegistration.model.TokenType;
import com.tsofnsalesforce.LoginandRegistration.request.AddPermissionRequest;
import com.tsofnsalesforce.LoginandRegistration.request.DeletePermissionRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AddPermissionResponse AddPermission(AddPermissionRequest request) throws MessagingException {

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        List<Role>  addedRoles = new ArrayList<>();
        var userRoles = user.getRoles();
        for(int i=0;i<request.getRoles().size();i++){
            var role = roleRepository.findByName(request.getRoles().get(i).getName()).orElseThrow(()-> new IllegalArgumentException("THE ROLE DOESN'T EXISTS"));
            if(addedRoles.contains(role))
                throw new MessagingException("THE USER ALREADY HAVE THE ROLE");
            addedRoles.add(role);
        }
        userRoles.addAll(addedRoles);
        user.setRoles(userRoles);
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AddPermissionResponse.builder()
                .token(jwtToken)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public DeletePermissionResponse deletePermission(DeletePermissionRequest request) throws MessagingException {

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        List<Role>  deleteRoles = new ArrayList<>();
        var userRoles = user.getRoles();
        for(int i=0;i<request.getRoles().size();i++){
            var role = roleRepository.findByName(request.getRoles().get(i).getName()).orElseThrow(()-> new IllegalArgumentException("THE ROLE DOESN'T EXISTS"));
            deleteRoles.add(role);
        }
        userRoles.removeAll(deleteRoles);
        user.setRoles(userRoles);
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return DeletePermissionResponse.builder()
                .token(jwtToken)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        var token = Token.builder()
                .appUser(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void printRoles(){
        var role = roleRepository.findByName("ADMIN").orElseThrow(()-> new IllegalArgumentException("THE ROLE IS EXSESTS"));
        var user = userRepository.findByEmail("mahmoudzoabi3@gmail.com").orElseThrow();
        System.out.println(user.getAuthorities());
    }
}
