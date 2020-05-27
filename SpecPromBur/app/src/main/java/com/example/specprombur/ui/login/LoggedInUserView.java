package com.example.specprombur.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 *
 * Класс, предоставляющий аутентифицированные данные пользователя в пользовательский интерфейс.
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
