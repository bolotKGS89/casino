package com.test.casino.dtos;

import com.test.casino.model.entity.GameType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BetDto implements Serializable {
    private Long id;
    private Long userId;
    private GameType gameType;
    private BigDecimal betAmount;
    private Integer result;
    private LocalDateTime date;
}
