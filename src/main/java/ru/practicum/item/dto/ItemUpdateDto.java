package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateDto {
    private long id;
    private boolean read;
    private boolean replaceTags;
    private Set<String> tags;
}
