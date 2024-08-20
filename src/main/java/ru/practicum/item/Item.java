package ru.practicum.item;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Item {
    @Id
    private Long id;

    private Long userId;
    private String url;
}
