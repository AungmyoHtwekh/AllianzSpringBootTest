package com.aungmyohtwe.AllianzTest.service.impl;

import com.aungmyohtwe.AllianzTest.model.User;
import com.aungmyohtwe.AllianzTest.payload.UserResponse;
import com.aungmyohtwe.AllianzTest.repository.UserRepository;
import com.aungmyohtwe.AllianzTest.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String EMAIL_PATTERN = "^(\\S.+\\S)@gmail.com$";
    private final String PASSWORD_PATTERN = "(?=^.{8,16}$)(?=(.*\\d){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})(?=(.*[!@#$%^&*-?]){2})^.*";
    @Override
    public User createUser(User user) {

        if(emailValidator(user.getEmail())) {
            if (passwordValidation(user.getPassword())) {
                User newUser = new User();
                newUser.setId(user.getId());
                newUser.setUsername(user.getUsername());
                newUser.setPassword(passwordEncoder.encode(user.getPassword()));
                newUser.setEmail(user.getEmail());
                newUser.setRole(user.getRole());
                return userRepository.save(newUser);
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Password length should have between 8 to 16 ");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Email format invalid");
        }

    }

    public boolean passwordValidation(String password) {

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean emailValidator(String email) {

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public User updateUser(long id, User user) {
        Optional<User> userEntityOptional = userRepository.findById(id);
        if (userEntityOptional.isPresent()) {
            User userEntity = userEntityOptional.get();
            userEntity.setId(id);
            userEntity.setUsername(user.getUsername());
            if (!StringUtils.isEmpty(user.getPassword())) {
                userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userEntity.setEmail(user.getEmail());
            userEntity.setRole(user.getRole());
            userRepository.save(userEntity);
            return userEntity;
        } else {
            return null;
        }
    }
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> userEntities = userRepository.findAll();

        List<UserResponse> userResponseList = new ArrayList<>();

        for (User userEntity : userEntities) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(userEntity.getId());
            userResponse.setEmail(userEntity.getEmail());
            userResponse.setRole(userEntity.getRole());
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    @Override
    public User searchUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userData = user.get();
            return userData;
        } else {
            return null;
        }
    }


    @Override
    public void deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {

            User securityUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!securityUser.getEmail().equals(user.get().getEmail())) {
                userRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Current logged in user cannot be deleted");
            }
        }
    }

    @Override
    public User searchByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            User userData = new User();
            userData.setId(user.getId());
            userData.setUsername(user.getUsername());
            userData.setPassword(passwordEncoder.encode(user.getPassword()));
            userData.setEmail(user.getEmail());
            userData.setRole(user.getRole());
            return userData;
        } else {
            return null;
        }
    }
}

