
package ru.glebdos.usermicroservice.controller;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.usermicroservice.config.JwtCore;
import ru.glebdos.usermicroservice.dto.AuthDto;


@Slf4j
@Setter
@RestController
@RequestMapping("/auth")
public class AuthController {


    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtCore jwtCore) {
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody AuthDto authDto) {
        log.info("Authenticating user {}", authDto.getUsername());
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
        }catch (BadCredentialsException e) {
            return new ResponseEntity<>("Аутентификация неуспешна",HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(token);
    }

}

