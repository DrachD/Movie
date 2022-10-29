package com.dmitriy.movie.model.account

import androidx.lifecycle.LiveData
import com.dmitriy.movie.model.account.entities.Account
import com.dmitriy.movie.model.account.entities.SignUpData

interface AccountsRepository {

    // Вошел ли пользователь в систему или нет
    suspend fun isSignedIn(): Boolean

    // Попытка войти в систему с помощью електронной почты и пароля
    suspend fun signIn(email: String, password: String)

    // Создание нового аккаунта
    suspend fun signUp(signUpData: SignUpData)

    // Выход из приложения
    fun logout()

    // Получение информации аккаунта текущего пользователя
    fun getAccount(): LiveData<Account?>

    // Изменение имени текущего пользователя
    suspend fun updateAccountUsername(newUsername: String)
}