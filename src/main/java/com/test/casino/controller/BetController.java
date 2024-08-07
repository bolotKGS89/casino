package com.test.casino.controller;

import com.test.casino.dtos.BetDto;
import com.test.casino.model.entity.User;
import com.test.casino.service.BetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bet")
@AllArgsConstructor
public class BetController {

    @Autowired
    private BetService betService;

    @PostMapping(path = "/slot-machine")
    public ResponseEntity<Object> placeBet(@RequestBody BetDto betDto) {
        try {
            return ResponseEntity.ok(betService.placeBet(betDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "/bets")
    public ResponseEntity<Object> getBets() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            return ResponseEntity.ok(betService.getBets(currentUser.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
