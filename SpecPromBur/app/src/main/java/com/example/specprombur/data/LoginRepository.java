package com.example.specprombur.data;

import com.example.specprombur.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 *
 * Класс, который запрашивает аутентификацию и информацию о пользователе
 * из удаленного источника данных и поддерживает в памяти кэш состояния входа
 * и учетных данных пользователя.
 */
public class LoginRepository {

    //Ключевое слово volatile, позволяет в процессе работы потока кэшировать значение переменной
    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    //Если учетные данные пользователя будут кэшироваться в локальном хранилище, рекомендуется их зашифровать
    // @see https://developer.android.com/training/articles/keystore

    private LoggedInUser user = null;
    //изначальное значение авторизаванного пользователя = null, то есть никто не авторизован

    // private constructor : singleton access
    //Этот метод позволяет получить и присвоить данные авторизации
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Этот метод позволяет получить данные авторизации и присвоить экземпляру instance
    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    //Проверка: если пользователь авторизован, возвращаем НЕ null
    public boolean isLoggedIn() {
        return user != null;
    }

    //Авторизованный пользователь -> выход
    public void logout() {
        user = null;
        dataSource.logout();
    }

    //Получаем авторизованного пользователя
    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        //Если учетные данные пользователя будут кэшироваться в локальном хранилище, рекомендуется их зашифровать
        // @see https://developer.android.com/training/articles/keystore
    }

    //Создаем метод на основе данных класса Result
    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        //Создаем объект класса Result, содержащего данные успеха результата или исключение ошибки.
        //Приваеваем ему данные о логине и пароле, если авторизация прошла успешно,
        //авторизуем пользователя и получаем его данные
        Result<LoggedInUser> result = dataSource.login(username, password);
        //Оператор instanceof нужен, чтобы проверить, был ли объект, на который ссылается переменная X,
        // создан на основе какого-либо класса Y.
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}
