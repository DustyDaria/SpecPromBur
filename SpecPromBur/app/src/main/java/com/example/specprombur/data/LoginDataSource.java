package com.example.specprombur.data;

import com.example.specprombur.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 *
 * Класс, который обрабатывает аутентификацию с учетными данными для входа
 * и извлекает информацию о пользователе.
 */
public class LoginDataSource {

    //метод login, созданный на основе класса Result со списком данных LoggedInUser
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            //точка входа в систему проверки подлинности пользователей
            //Пример попытки входа на основе "ложного пользователя"
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe"); //генерация уникального идентификатора
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        //отменить проверку подлинности
    }
}
