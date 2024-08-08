package com.test.casino.repository;

import com.test.casino.model.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BetRepository extends JpaRepository<Bet, Integer> {
    Optional<List<Bet>> findAllByUserId(Long userId);
}
