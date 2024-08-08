package com.test.casino.dtos;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String password;
    private BigDecimal balance;
}
