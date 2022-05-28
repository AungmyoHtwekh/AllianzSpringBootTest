package com.aungmyohtwe.AllianzTest.service;


import com.aungmyohtwe.AllianzTest.model.User;
import com.aungmyohtwe.AllianzTest.payload.UserResponse;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User updateUser(long id, User user);
    User searchUser(long id);
    void deleteUser (long id);
    User searchByEmail(String email);

    List<UserResponse> getAllUsers();

}
