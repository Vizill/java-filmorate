package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDateConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительной")
    private int duration;

    public Film(int id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

}
