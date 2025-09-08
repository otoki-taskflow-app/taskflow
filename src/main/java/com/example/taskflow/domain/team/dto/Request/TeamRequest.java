package com.example.taskflow.domain.team.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TeamRequest {
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 50, message = "이름은 최대 50글자까지 가능합니다.")
    private String name;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;
}