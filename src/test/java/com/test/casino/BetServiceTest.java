package com.test.casino;

import com.test.casino.dtos.BetDto;
import com.test.casino.mapper.BetMapper;
import com.test.casino.model.entity.Bet;
import com.test.casino.model.entity.GameType;
import com.test.casino.model.entity.User;
import com.test.casino.repository.BetRepository;
import com.test.casino.service.impl.BetServiceImpl;
import com.test.casino.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BetServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private BetMapper betMapper;

    @InjectMocks
    private BetServiceImpl betService;

    @Mock
    private UserServiceImpl userService;

    private User user;
    private Bet bet1;
    private Bet bet2;
    private Bet bet3;

    private BetDto betDto1;
    private BetDto betDto2;
    private BetDto betDto3;

    @BeforeEach
    public void setup(){
        //Given
        user = new User(14L, "test", "passwd", BigDecimal.valueOf(100.00));
        bet1 = new Bet(1L, user, GameType.SLOT_MACHINE, BigDecimal.valueOf(20.11), 33, LocalDateTime.now());
        bet2 = new Bet(2L, user, GameType.SLOT_MACHINE, BigDecimal.valueOf(11.00), 56, LocalDateTime.now());
        bet3 = new Bet(3L, user, GameType.SLOT_MACHINE, BigDecimal.valueOf(21.00), 23, LocalDateTime.now());

        betDto1 = new BetDto(1L, user.getId(), GameType.SLOT_MACHINE, BigDecimal.valueOf(20.11), 33, LocalDateTime.now());
        betDto2 = new BetDto(2L, user.getId(), GameType.SLOT_MACHINE, BigDecimal.valueOf(11.00), 56, LocalDateTime.now());
        betDto3 = new BetDto(3L, user.getId(), GameType.SLOT_MACHINE, BigDecimal.valueOf(21.00), 23, LocalDateTime.now());
    }

    @Test
    void testGetBets() {
        //when
        given(betRepository.findAllByUserId(user.getId())).willReturn(
                Optional.of(List.of(bet1, bet2, bet3))
        );
        given(betMapper.getBetDtoList(List.of(bet1, bet2, bet3)))
                .willReturn(
                        List.of(betDto1, betDto2, betDto3)
                );

        //then
        assertThat(
                betService.getBets(user.getId())
        ).isNotNull();

        assertThat(
                betService.getBets(user.getId()).size()
        ).isEqualTo(3);

        assertThat(
                betService.getBets(user.getId()).get(0).getResult()
        ).isEqualTo(33);
    }

    @Test
    void testGetBets_RuntimeException() {
        //when
        given(betRepository.findAllByUserId(user.getId())).willReturn(
                Optional.of(List.of(bet1, bet2, bet3))
        );

        //then
        Exception exception = assertThrows(RuntimeException.class,
                () -> betService.getBets(33L));
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    void testPlaceBet() {
        //when
        given(betMapper.toBet(betDto1)).willReturn(bet1);
        given(betRepository.save(bet1)).willReturn(bet1);
        given(userService.updateBalance(bet1.getUser().getId(), bet1.getBetAmount())).willReturn(null);
        given(betMapper.toBetDto(bet1)).willReturn(betDto1);

        //then
        assertThat(
                betService.placeBet(betDto1)
        ).isNotNull();
        assertThat(
                betService.placeBet(betDto1).getResult()
        ).isEqualTo(33);
    }


}
