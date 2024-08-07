package com.test.casino.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UserDto implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private BigDecimal balance;
}
