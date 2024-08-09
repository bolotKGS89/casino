package com.test.casino.service.impl;

import com.test.casino.dtos.BetDto;
import com.test.casino.mapper.BetMapper;
import com.test.casino.model.entity.Bet;
import com.test.casino.repository.BetRepository;

import com.test.casino.service.BetService;
import com.test.casino.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetServiceImpl implements BetService {

    @Autowired
    private BetMapper betMapper;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public BetDto placeBet(BetDto betDto) {

        Bet bet = betRepository.save(betMapper.toBet(betDto));
        userService.updateBalance(bet.getUser().getId(), bet.getBetAmount());

        return betMapper.toBetDto(bet);
    }

    @Override
    public List<BetDto> getBets(Long id) {
        List<Bet> bets = betRepository.findAllByUserId(id).orElseThrow(() -> new RuntimeException("Bets not found by user Id"));

        return betMapper.getBetDtoList(
                bets
        );
    }
}
