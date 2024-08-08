package com.test.casino.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.casino.dtos.BetDto;
import com.test.casino.mapper.BetMapper;
import com.test.casino.model.entity.Bet;
import com.test.casino.model.entity.GameType;
import com.test.casino.model.entity.User;
import com.test.casino.repository.BetRepository;
import com.test.casino.repository.UserRepository;
import com.test.casino.service.BetService;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

@WebMvcTest(BetController.class)
public class BetControllerIntegrationTest {
    @Autowired
    JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BetRepository betRepository;

    @MockBean
    private BetMapper betMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private BetService betService;


    private String token;
    private User user1;
    private Bet bet1;
    private Bet bet2;
    private Bet bet3;

    private BetDto betDto1;
    private BetDto betDto2;
    private BetDto betDto3;


    @BeforeEach
    public void setup() {
        // given
        user1 = new User(1L, "test", "passwd", BigDecimal.valueOf(100.00));
        bet1 = new Bet(1L, user1, GameType.SLOT_MACHINE, BigDecimal.valueOf(20.11), 33, null);
        bet2 = new Bet(2L, user1, GameType.SLOT_MACHINE, BigDecimal.valueOf(11.00), 56, null);
        bet3 = new Bet(3L, user1, GameType.SLOT_MACHINE, BigDecimal.valueOf(21.00), 23, null);

        betDto1 = new BetDto(1L, user1.getId(), GameType.SLOT_MACHINE, BigDecimal.valueOf(20.11), 33, null);
        betDto2 = new BetDto(2L, user1.getId(), GameType.SLOT_MACHINE, BigDecimal.valueOf(11.00), 56, null);
        betDto3 = new BetDto(3L, user1.getId(), GameType.SLOT_MACHINE, BigDecimal.valueOf(21.00), 23, null);

        token = jwtService.generateToken(user1);

        // Mock authentication context
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user1, null, Collections.emptyList())
        );
    }

    @Test
    public void testBets_Exception() throws Exception {
        // Perform the GET request and expect an INTERNAL_SERVER_ERROR status
        mockMvc.perform(get("/api/bet/bets")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testBets() throws Exception {
        given(betRepository.findAllByUserId(user1.getId())).willReturn(
                Optional.of(List.of(bet1, bet2, bet3))
        );

        // Perform the GET request and expect an OK status
        mockMvc.perform(get("/api/bet/bets")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testPlaceBet_Exception() throws Exception {
        mockMvc.perform(post("/api/bet/slot-machine")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(betDto1)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testPlaceBet() throws Exception {
        given(betMapper.toBet(betDto1)).willReturn(bet1);
        given(betRepository.save(bet1)).willReturn(bet1);
        given(userService.updateBalance(bet1.getUser().getId(), bet1.getBetAmount())).willReturn(null);
        given(betMapper.toBetDto(bet1)).willReturn(betDto1);

        mockMvc.perform(post("/api/bet/slot-machine")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(betDto1)))
                .andExpect(status().isOk());
    }
}
