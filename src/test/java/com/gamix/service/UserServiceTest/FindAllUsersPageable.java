package com.gamix.service.UserServiceTest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindAllUsersPageable {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testFindAllUsers() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        User user3 = new User();
        user3.setUsername("user3");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(userList));

        List<User> foundUsers = userService.findAllUsers(0, 3);

        assertEquals(userList, foundUsers);
    }

    @Test
    public void testFindAllUsersEmptyList() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        List<User> foundUsers = userService.findAllUsers(0, 3);

        assertTrue(foundUsers.isEmpty());
    }
}