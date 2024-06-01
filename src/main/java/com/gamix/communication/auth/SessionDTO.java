package com.gamix.communication.auth;

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
public class SessionDTO {
    User user;
    String token;

    @JsonIgnore
    public String getToken() {
        return token;
    }
}