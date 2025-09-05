package com.example.taskflow.domain.auth.entity;

import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    private RefreshToken(User user, String token, LocalDateTime expireAt) {
        this.user = user;
        this.token = token;
        this.expireAt = expireAt;
    }

    public static RefreshToken create(User user, String token, long expireMillis) {
        return new RefreshToken(
                user,
                token,
                LocalDateTime.now().plusNanos(expireMillis * 1_000_000)
        );
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }

    public void updateToken(String newToken, long expireMillis) {
        this.token = newToken;
        this.expireAt = LocalDateTime.now().plusNanos(expireMillis * 1_000_000);
    }
}
