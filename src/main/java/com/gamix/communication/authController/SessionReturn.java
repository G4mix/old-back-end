package com.gamix.communication.authController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamix.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class SessionReturn {
    User user;
    String token;

    @JsonIgnore
    public String getToken() {
        return token;
    }
}