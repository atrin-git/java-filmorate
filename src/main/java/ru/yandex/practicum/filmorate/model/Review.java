package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    @JsonProperty("reviewId")
    private Long id;
    private Long filmId;
    private Long userId;
    private Boolean isPositive;
    private String content;
    private long useful;

    public boolean hasIsPositive() {
        return isPositive != null;
    }

    public boolean hasContent() {
        return content != null;
    }

    public boolean hasUseful() {
        return content != null;
    }
}
