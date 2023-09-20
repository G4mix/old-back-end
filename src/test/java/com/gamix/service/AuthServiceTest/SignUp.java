// package com.gamix.service.AuthServiceTest;

// import static org.junit.Assert.assertNotEquals;
// import static org.junit.Assert.assertNull;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.Mockito.when;

// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.test.context.junit4.SpringRunner;

// import com.gamix.models.PasswordUser;
// import com.gamix.models.User;
// import com.gamix.records.UserRecords.UserInput;
// import com.gamix.repositories.UserRepository;
// import com.gamix.security.JwtManager;
// import com.gamix.service.AuthService;

// @RunWith(SpringRunner.class)
// @SpringBootTest
// public class SignUp {

//     @MockBean
//     private UserRepository userRepository;

//     @MockBean
//     private JwtManager jwtManager;

//     @MockBean
//     private AuthService authService;

//     private UserInput userInput;

//     @Before
//     public void setUp() {
//         userInput = new UserInput("user1", "user1@example.com", "password1", "icon1", true);
//     }

//     @Test
//     public void signUpPasswordUser_UserDoesNotExist() {
//         configureUserDoesNotExist();

//         PasswordUser createdPasswordUser = authService.signUpPasswordUser(userInput);

//         assertNotNull(createdPasswordUser);
//     }

//     @Test
//     public void signUpPasswordUser_UserExistsWithoutPasswordUser() {
//         configureUserExistsWithoutPasswordUser();

//         PasswordUser createdPasswordUser = authService.signUpPasswordUser(userInput);

//         assertNotEquals(userInput.password(), createdPasswordUser.getPassword());
//     }

//     @Test
//     public void signUpPasswordUser_UserExistsWithPasswordUser() {
//         configureUserExistsWithPasswordUser();

//         PasswordUser createdPasswordUser = authService.signUpPasswordUser(userInput);

//         assertNull(createdPasswordUser);
//     }

//     private User createNewUser() {
//         User user = new User();
//         user.setUsername(userInput.username());
//         user.setEmail(userInput.email());
//         user.setIcon(userInput.icon());
//         return user;
//     }

//     private PasswordUser createNewPasswordUser() {
//         PasswordUser passwordUser = new PasswordUser();
//         passwordUser.setUser(createNewUser());
//         passwordUser.setPassword(new BCryptPasswordEncoder().encode(userInput.password()));
//         passwordUser.setVerifiedEmail(false);
//         return passwordUser;
//     }

//     private void configureUserDoesNotExist() {
//         User newUser = createNewUser();
//         when(userRepository.findByEmail(userInput.email())).thenReturn(null);
//         when(authService.createUser(userInput)).thenReturn(newUser);
    
//         PasswordUser newPasswordUser = createNewPasswordUser();
//         String token = "token_here";
//         when(jwtManager.generateAccessToken(newPasswordUser, userInput.rememberMe())).thenReturn(token);
    
//         when(authService.signUpPasswordUser(userInput)).thenReturn(newPasswordUser);
//     }
    
    
//     private void configureUserExistsWithoutPasswordUser() {
//         User existingUser = createNewUser();
//         when(userRepository.findByEmail(userInput.email())).thenReturn(existingUser);
        
//         when(authService.createUser(userInput)).thenReturn(existingUser);
//         PasswordUser passwordUser = createNewPasswordUser();
//         existingUser.setPasswordUser(passwordUser);
//         when(authService.signUpPasswordUser(userInput)).thenReturn(passwordUser);
//     }

//     private void configureUserExistsWithPasswordUser() {
//         User existingUser = createNewUser();
//         PasswordUser existingPasswordUser = createNewPasswordUser();
//         existingUser.setPasswordUser(existingPasswordUser);
//         when(userRepository.findByEmail(userInput.email())).thenReturn(existingUser);
//         when(authService.signUpPasswordUser(userInput)).thenReturn(null);
//     }
// }