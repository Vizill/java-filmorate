package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректный формат электронной почты")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public void setName(String name) {
        this.name = (name == null || name.isBlank()) ? login : name;
    }
}