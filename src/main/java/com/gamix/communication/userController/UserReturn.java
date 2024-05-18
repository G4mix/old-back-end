package com.gamix.communication.userController;

import com.gamix.models.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserReturn {
    String username, icon;

    public UserReturn(User user) {
        this.username = user.getUsername();
        this.icon = user.getUserProfile().getIcon();
    }
}