package com.example.taskflow.domain.team.entity;

import com.example.taskflow.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> member = new ArrayList<>();

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addMember(TeamMember teamMember) {
        this.member.add(teamMember);
        teamMember.setTeam(this);
    }

    public void removeMember(TeamMember teamMember) {
        this.member.remove(teamMember);
        teamMember.setTeam(null);
    }
}
