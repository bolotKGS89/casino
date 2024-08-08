package com.test.casino.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.casino.dtos.UserDto;
import com.test.casino.model.entity.User;
import com.test.casino.repository.BetRepository;
import com.test.casino.repository.UserRepository;
import com.test.casino.service.JwtService;
import com.test.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BetRepository betRepository;

    @Autowired
    JwtService jwtService;

    private User user1;
    private UserDto userDto;
    private String token;

    @BeforeEach
    public void setup() {
        // given
        user1 = new User(1L, "test", "passwd", BigDecimal.valueOf(100.00));
        userDto = new UserDto(1L, "test", "passwd", BigDecimal.valueOf(100.00));

        token = jwtService.generateToken(user1);
        // Mock authentication context
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user1, null, Collections.emptyList())
        );
    }

    @Test
    public void testLoginUser_Exception() throws Exception {

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUser_Exception() throws Exception {
        // Mock the userService to throw an exception when save is called
        userDto.setBalance(BigDecimal.valueOf(-1));

        // Perform the POST request and expect an INTERNAL_SERVER_ERROR status
        mockMvc.perform(post("/api/users/register")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void testUserRetrieval() throws Exception {
        given(userRepository.findById(Math.toIntExact(user1.getId()))).willReturn(Optional.of(user1));

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.balance").value(100.00));
    }

    @Test
    public void testUserRetrieval_Exception() throws Exception {

        given(userRepository.findById(Math.toIntExact(user1.getId()))).willThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
