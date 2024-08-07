package com.test.casino.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "casino.bet")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    private GameType gameType;

    @Column(name = "bet_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal betAmount;

    @Column(name = "result", nullable = false)
    private Integer result;

    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime date;

    @PrePersist
    protected void onCreate() {
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (result == null) {
            result = new Random().nextInt(100); // Generates a random integer between 0 and 99
        }
    }

    @Override
    public String toString() {
        return "Bet{" +
                "id=" + id +
                ", gameType=" + (gameType != null ? gameType.name() : null) +
                ", betAmount=" + betAmount +
                ", result=" + result +
                ", date=" + (date != null ? date.toString() : null) +
                '}';
    }
}
