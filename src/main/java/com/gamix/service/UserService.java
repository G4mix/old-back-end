package com.gamix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gamix.models.User;
import com.gamix.records.UserRecords.PartialUserInput;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;

import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtManager jwtManager;
    
    public List<User> findAllUsers(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit);
        Page<User> users = userRepository.findAll(page);

        return users.getContent();
    }

    public User findUserByToken(String token) {
        String accessToken = token.substring(7);
        Claims claims = jwtManager.getTokenClaims(accessToken);
        return userRepository.findByUsername(claims.getSubject());
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Integer id, PartialUserInput userInput) {
        return userRepository.findById(id)
        		.map(user -> {
	                user.setUsername(userInput.username() != null ? userInput.username() : user.getUsername());
	                user.setIcon(userInput.icon() != null ? userInput.icon() : user.getIcon());
	                return userRepository.save(user);
	            })
	            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }
    
    public void deleteAccount(Integer id) {
        if(!userRepository.existsById(id)) throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        userRepository.deleteById(id);
    }
}
