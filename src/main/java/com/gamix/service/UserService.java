package com.gamix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.interfaces.services.UserServiceInterface;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtManager jwtManager;
    
    @Override
    public List<User> findAllUsers(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit);
        Page<User> users = userRepository.findAll(page);

        return users.getContent();
    }

    @Override
    public User findUserByToken(String accessToken) throws TokenClaimsException {
        System.out.println(">>>> TOKEN IN SERVICE: "+accessToken);
        Claims claims = jwtManager.getTokenClaims(accessToken);
        return userRepository.findByUsername(claims.getSubject());
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(Integer id, PartialUserInput userInput) {
        return userRepository.findById(id)
        		.map(user -> {
	                user.setUsername(userInput.getUsername() != null ? userInput.getUsername() : user.getUsername());
	                user.setIcon(userInput.getIcon() != null ? userInput.getIcon() : user.getIcon());
	                return userRepository.save(user);
	            })
	            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }
    
    @Override
    public void deleteAccount(Integer id) {
        if(!userRepository.existsById(id)) throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        userRepository.deleteById(id);
    }

    @Override
    public User createUser(String username, String email, String icon) {
        User user = new User()
            .setUsername(username)
            .setEmail(email)
            .setIcon(icon);
    
        return userRepository.save(user);
    }
}
