package com.dmitriy.movie.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dmitriy.movie.model.account.entities.Account

class RepositorySharedPreferences(
    private val context: Context
) {

    interface Callback {
        fun onLoadAccountData(account: Account)
    }

    private val pref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    fun saveAccountData(account: Account) {
       pref.edit()
           .putString(KEY_USERNAME, account.username)
           .putString(KEY_EMAIL, account.email)
           .putLong(KEY_CREATED_AT, account.createdAt)
           .apply()
    }

    fun loadAccountData(callback: Callback) {
        val account = Account(
            username = pref.getString(KEY_USERNAME, null),
            email = pref.getString(KEY_EMAIL, null),
            createdAt = pref.getLong(KEY_CREATED_AT, 0L)
        )
        callback.onLoadAccountData(account)
    }

    fun saveAccountSignIn(isSignIn: Boolean) {
        pref.edit()
            .putBoolean(KEY_SIGNIN, isSignIn)
            .apply()
    }

    fun loadAccoutSignIn(): Boolean {
        return pref.getBoolean(KEY_SIGNIN, false)
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "pref"

        // Keys
        private const val KEY_SIGNIN = "SIGNIN"
        private const val KEY_USERNAME = "USERNAME"
        private const val KEY_EMAIL = "EMAIL"
        private const val KEY_CREATED_AT = "CREATED_AT"
    }
}