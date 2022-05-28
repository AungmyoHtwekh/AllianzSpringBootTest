package com.aungmyohtwe.AllianzTest.payload;

import com.aungmyohtwe.AllianzTest.model.UserRoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    @Enumerated(EnumType.STRING)
    UserRoleName role;
}
