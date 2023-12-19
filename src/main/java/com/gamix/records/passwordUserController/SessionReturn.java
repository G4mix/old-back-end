package com.gamix.records.passwordUserController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class SessionReturn {
    String username, icon, token;

    @JsonIgnore
    public String getToken() {
        return token;
    }
}