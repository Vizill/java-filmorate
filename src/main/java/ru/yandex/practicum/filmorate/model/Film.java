package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDateConstraint;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film {
    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private int duration;

    @Builder.Default
    private Set<Integer> likes = new HashSet<>();

    private int mpaId;
    private Set<Genre> genres;

    public Film(int id, String name, String description, LocalDate releaseDate,
                int duration, int mpaId, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpaId = mpaId;
        this.genres = genres;
    }
}