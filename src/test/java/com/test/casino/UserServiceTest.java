package com.test.casino;

import com.test.casino.dtos.UserDto;
import com.test.casino.mapper.UserMapper;
import com.test.casino.model.entity.User;
import com.test.casino.repository.UserRepository;
import com.test.casino.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    public void setup(){
        //given
        user1 = new User(1L, "test", "passwd", BigDecimal.valueOf(100.00));
        user2 = new User(2L, "test1", "passwd1", BigDecimal.valueOf(200.00));
        userDto1 = new UserDto(1L, "test", "passwd", BigDecimal.valueOf(100.00));
        userDto2 = new UserDto(2L, "test", "passwd1", BigDecimal.valueOf(100.00));
    }

    @Test
    void getUserByIdTest() {
        //when
        given(userRepository.findById(1))
                .willReturn(Optional.ofNullable(user1));
        given(userMapper.toUserDto(user1)).willReturn(userDto1);

        UserDto user = userServiceImpl.getById(1L);

        //then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("test");
    }

    @Test
    void getUserByIdTest_RuntimeException() {
        // when
        given(userRepository.findById(1))
                .willReturn(Optional.ofNullable(user1));
        given(userMapper.toUserDto(user1)).willReturn(userDto1);

        // then
        Exception exception = assertThrows(RuntimeException.class, () -> userServiceImpl.getById(33L));

        assertThat(exception.getMessage()).isNotNull();
    }


    @Test
    void saveUserTest() {
        //when
        given(userRepository.save(user2)).willReturn(user2);
        given(userMapper.toUserDto(user2)).willReturn(userDto2);
        given(userMapper.toUser(userDto2)).willReturn(user2);
        given(passwordEncoder.encode(userDto2.getPassword())).willReturn("passwd1");

        //then
        UserDto userDto = userServiceImpl.save(userDto2);
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(2);
    }

    @Test
    void saveUserTest_IllegalArgumentException() {
        //when
        given(userMapper.toUser(userDto2)).willReturn(user2);
        user2.setBalance(BigDecimal.valueOf(-1));

        //then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.save(userDto2));
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    void updateBalanceTest() {
        //when
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user1));
        given(userRepository.save(user1)).willReturn(user1);
        userDto1.setBalance(BigDecimal.valueOf(80.00));
        given(userMapper.toUserDto(user1)).willReturn(userDto1);

        //then
        UserDto userDto = userServiceImpl.updateBalance(1L, BigDecimal.valueOf(20.00));
        assertThat(userDto).isNotNull();
        assertThat(userDto.getBalance()).isEqualTo(BigDecimal.valueOf(80.00));
    }

    @Test
    void updateBalanceTest_RuntimeException() {
        //when
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user1));

        //then
        Exception exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.updateBalance(2L, BigDecimal.valueOf(20.00)));
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    void updateBalanceTest_IllegalArgumentException() {
        //when
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user1));

        //then
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.updateBalance(1L, BigDecimal.valueOf(2000.00)));
        assertThat(exception.getMessage()).isNotNull();
    }
}
