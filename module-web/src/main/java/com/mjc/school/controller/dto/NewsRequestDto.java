package com.mjc.school.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsRequestDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private List<Long> tagsIds;
}
