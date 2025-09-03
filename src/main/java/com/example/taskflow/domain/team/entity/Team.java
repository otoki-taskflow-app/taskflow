package com.example.taskflow.domain.team.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private List<User> member = new ArrayList<>();

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addMember(User user) {
        member.add(user);
    }

    public void removeMember(User user) {
        member.remove(user);
    }
}
