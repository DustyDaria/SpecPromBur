package com.example.specprombur.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 *
 * Универсальный класс, содержащий данные успеха результата или исключение ошибки.
 */
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    //скройте частный конструктор, чтобы ограничить типы подклассов (Success, Error)
    private Result() {
    }

    @Override
    public String toString() {
        //обработка ошибок: если добавлено - вернуть успешные данные, иначе - вернуть сообщение об ошибке
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    //Производный класс - Успех
    public final static class Success<T> extends Result {
        private T data;

        public Success(T data) {
            this.data = data;
            //присваиваем полученные данные переменной data
        }

        public T getData() {
            return this.data;
            //возвращаем данные из переменной data
        }
    }

    // Error sub-class
    //Производный класс - ошибка
    public final static class Error extends Result {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
            //присваиваем полученную ошибку объекту error, класса Exception
        }

        public Exception getError() {
            return this.error;
            //возвращаем ошибку
        }
    }
}
