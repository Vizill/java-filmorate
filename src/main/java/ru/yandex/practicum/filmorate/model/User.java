package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@Data
public class User {
    private int id;

    @NotBlank(message = "Электронная почта не может быть пустой.")
    @Email(message = "Некорректный формат электронной почты. Должен содержать символ '@'.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы.")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой.")
    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name != null ? name : login;
        this.birthday = birthday;
    }

    public String getName() {
        return (name == null || name.isBlank()) ? login : name;
    }

}
