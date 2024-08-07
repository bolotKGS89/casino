package com.test.casino.dtos;

import com.test.casino.model.entity.GameType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BetDto implements Serializable {
    private Integer id;
    private Integer userId;
    private GameType gameType;
    private BigDecimal betAmount;
    private Integer result;
    private LocalDateTime date;
}
