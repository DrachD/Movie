package com.dmitriy.movie.model.account.entities

data class Account(
    var username: String? = "",
    var email: String? = "",
    var createdAt: Long = UNKNOWN_CREATED_AT
) {
    companion object {
        const val UNKNOWN_CREATED_AT = 0L
    }
}
