package com.test.casino.service.impl;


import com.test.casino.dtos.UserDto;
import com.test.casino.mapper.UserMapper;
import com.test.casino.model.entity.User;
import com.test.casino.repository.UserRepository;
import com.test.casino.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

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
    public UserDto getById(Long id) {

        User user = userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserDto(
                user
        );
    }

    @Transactional
    @Override
    public UserDto updateBalance(Long userId, BigDecimal amount) {
        User user = userRepository.findById(Math.toIntExact(userId))
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
