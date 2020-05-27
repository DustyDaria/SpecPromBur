package com.example.specprombur.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.specprombur.data.LoginRepository;
import com.example.specprombur.data.Result;
import com.example.specprombur.data.model.LoggedInUser;
import com.example.specprombur.R;

public class LoginViewModel extends ViewModel {

    /*Компонент LiveData — предназначен для хранения объекта и разрешает подписаться на его изменения.
    Ключевой особенностью является то, что компонент осведомлен о жизненном цикле и позволяет
    не беспокоится о том, на каком этапе сейчас находиться подписчик, в случае уничтожения подписчика,
    компонент отпишет его от себя. Для того, чтобы LiveData учитывала жизненный цикл используется
    компонент Lifecycle, но также есть возможность использовать без привязки к жизненному циклу.

    Сам компонент состоит из классов: LiveData, MutableLiveData, MediatorLiveData, LiveDataReactiveStreams,
    Transformations и интерфейса: Observer.

    Класс MutableLiveData, является расширением LiveData, с отличием в том что это
    не абстрактный класс и методы setValue(T) и postValue(T) выведены в api, то есть публичные.

    По факту класс является хелпером для тех случаев когда мы не хотим помещать логику обновления
    значения в LiveData, а лишь хотим использовать его как Holder.
    */

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    /*получаем данные об авторизации и присваем полученное значение в переменную loginRepository.
    Используется класс LoginRepository, который запрашивает аутентификацию и информацию
    о пользователе из удаленного источника данных и поддерживает в памяти кэш состояния входа
    и учетных данных пользователя.*/
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    //Возвращаем статус авторизации с помощью компонента LiveData
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    //Возвращаем итоговый логин с помощью компонента LiveData
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        //может быть запущен в отдельном асинхронном задании
        Result<LoggedInUser> result = loginRepository.login(username, password);

        /*Используем класс Result, который содержит данные успеха или исключение ошибки.
        Также используем класс loginRepository, который запрашивает аутентификацию и информацию о пользователе
        из удаленного источника данных и поддерживает в памяти кэш состояния входа и учетных данных пользователя.

        Создаем обект result класса Result и присваиваем ему значение, полученное из класса loginRepository ->
        -> метод login() с передачей логина и пароля пользователя
*/

        //Оператор instanceof нужен, чтобы проверить, был ли объект, на который ссылается переменная X,
        // создан на основе какого-либо класса Y.
        if (result instanceof Result.Success) {
            //если авторизация успешна, получаем данные пользователя, иначе выдаем ошибку входа
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) { //проверка имени пользователя на корректность
            //если имя пользователя некорректное, выводим сообщение "Неверное имя пользователя"
            // и присваиваем passwordError значение null
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) { //проверка пароля пользователя на корректность
            //если пароль пользователя некорректен, выводим сообщение "Пароль должен быть >5 символов"
            // и присваиваем usernameError значение null
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else { // Иначе - все введено верно, данные корректны
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    //Проверка валидации имени пользователя заполнителя
    private boolean isUserNameValid(String username) {
        if (username == null) { //Если имя пользователя не заполнено, возвращаем false
            return false;
        }
        if (username.contains("@")) {//Если имя пользователя содержит символ "@"
            // Метод matches() сообщает, соответствует ли или нет данная строка заданному регулярному выражению,
            //на основе шаблона из класса Patterns
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            //Метод trim() удаляет у строки пробелы с начала и с конца строки. Пробелы внутри строки никто не трогает.
            //Метод isEmpty() возвращает true, если этот список не содержит элементов.
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    //Проверка правильности пароля заполнителя
    private boolean isPasswordValid(String password) {
        //Возвращаем пароль != null, у которого удалили пробелы с начал и конца строки, и с длиной > 5 символов
        return password != null && password.trim().length() > 5;
    }
}
