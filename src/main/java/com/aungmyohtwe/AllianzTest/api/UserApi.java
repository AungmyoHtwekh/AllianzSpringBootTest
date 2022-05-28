package com.aungmyohtwe.AllianzTest.api;

import com.aungmyohtwe.AllianzTest.model.User;
import com.aungmyohtwe.AllianzTest.payload.LoginRequest;
import com.aungmyohtwe.AllianzTest.payload.LoginResponse;
import com.aungmyohtwe.AllianzTest.security.TokenHelper;
import com.aungmyohtwe.AllianzTest.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@Api(value = "User Information", description = "User Information")
public class UserApi {

    private static final Logger logger = LoggerFactory.getLogger(UserApi.class);

    @Autowired
    private UserService userService;

    @Autowired
    TokenHelper tokenHelper;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @ApiOperation(value = "Login User with email and password  ")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AuthenticationException, IOException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jws = tokenHelper.generateToken(user.getEmail(), user.getRole().name());
        int expiresIn = tokenHelper.getExpiredIn();
        return ResponseEntity.ok(new LoginResponse(jws, expiresIn));
    }

    @PostMapping("/user")
    @ApiOperation(value = "Create User By User Request Body ", response = User.class)
    public ResponseEntity<User> createUser(@RequestBody User user){
       try {
           User resUser = userService.createUser(user);
           resUser.setPassword("");
           return new ResponseEntity<>(resUser, HttpStatus.CREATED);
       } catch (DataIntegrityViolationException e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already exist");
       }
       catch (Exception e){
           logger.error("Error ", e);
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }

    @PutMapping("/user/{id}")
    @ApiOperation(value = "Update User By id path variable ", response = User.class)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user){
        try {
            User user1 = userService.updateUser(id,user);
            user1.setPassword("");
            return new ResponseEntity<>(user1, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get User By id path variable ", response = User.class)
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        try {
            User user = userService.searchUser(id);
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "Delete User By id path variable ", response = User.class)
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id){
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public void optionLogin() {
    }
}
