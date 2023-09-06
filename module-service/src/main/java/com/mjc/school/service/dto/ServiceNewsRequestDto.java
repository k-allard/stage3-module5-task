package com.mjc.school.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceNewsRequestDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private List<Long> newsTagsIds;

    public ServiceNewsRequestDto(String content) {
        this(null, "Unknown", content, null, null);
    }
    public ServiceNewsRequestDto(Long id, String content) {
        this(id, "Unknown", content, null, null);
    }
}
