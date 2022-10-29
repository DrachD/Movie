package com.dmitriy.movie.model.account.entities

import com.dmitriy.movie.model.Field

data class SignUpData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String
) {
    fun validate() {
        if (email.isBlank()) throw RuntimeException(Field.Email.toString())
        if (username.isBlank()) throw RuntimeException(Field.Username.toString())
        if (password.isBlank()) throw RuntimeException(Field.Password.toString())
        if (password.length < 6) throw RuntimeException()
        if (password != repeatPassword) throw RuntimeException()
    }
}