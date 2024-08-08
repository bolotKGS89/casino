package com.test.casino.service;

import com.test.casino.dtos.BetDto;

import java.util.List;

public interface BetService {
    BetDto placeBet(BetDto betDto);
    List<BetDto> getBets(Long id);
}
