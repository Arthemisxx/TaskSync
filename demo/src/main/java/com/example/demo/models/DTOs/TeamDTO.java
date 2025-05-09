package com.example.demo.models.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class TeamDTO {
    private Long id;
    private String teamName;
    private String description;
    private Set<Long> userIds;
}
