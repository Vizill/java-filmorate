package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
@Data
public class User {
    private int id;

    @Email(message = "Некорректный email")
    @NotNull(message = "Email не может быть пустым")
    private String email;

    @NotNull(message = "Логин не может быть пустым")
    @Size(min = 1, message = "Логин не может быть пустым и не может содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name != null ? name : login;
        this.birthday = birthday;
    }

}
