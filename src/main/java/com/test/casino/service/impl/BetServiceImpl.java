package com.test.casino.service.impl;

import com.test.casino.dtos.BetDto;
import com.test.casino.mapper.BetMapper;
import com.test.casino.model.entity.Bet;
import com.test.casino.repository.BetRepository;
import com.test.casino.repository.UserRepository;
import com.test.casino.service.BetService;
import com.test.casino.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BetServiceImpl implements BetService {

    private BetMapper betMapper;
    private BetRepository betRepository;
    private UserService userService;

    @Override
    @Transactional
    public BetDto placeBet(BetDto betDto) {

        Bet bet = betRepository.save(betMapper.toBet(betDto));
        userService.updateBalance(bet.getUser().getId(), bet.getBetAmount());

        return betMapper.toBetDto(bet);
    }

    @Override
    public List<BetDto> getBets(Integer id) {
        List<Bet> bets = betRepository.findAllByUserId(id).orElseThrow(() -> new RuntimeException("Bets not found by user Id"));

        return betMapper.getBetDtoList(
                bets
        );
    }
}
