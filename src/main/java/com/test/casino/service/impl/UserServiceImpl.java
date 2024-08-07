package com.test.casino.service.impl;


import com.test.casino.dtos.UserDto;
import com.test.casino.mapper.UserMapper;
import com.test.casino.model.entity.User;
import com.test.casino.repository.UserRepository;
import com.test.casino.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        if (user.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be less than zero.");
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Integer id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserDto(
                user
        );
    }

    @Transactional
    @Override
    public UserDto updateBalance(Integer userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal newBalance = user.getBalance().subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be less than zero.");
        }

        user.setBalance(newBalance);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public User login(UserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()
                )
        );
        return userRepository.findByUsername(userDto.getUsername())
                        .orElseThrow(() -> new RuntimeException("Can't login"));
    }
}
