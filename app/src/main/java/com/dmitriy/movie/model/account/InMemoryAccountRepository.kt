package com.dmitriy.movie.model.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmitriy.movie.model.Field
import com.dmitriy.movie.model.account.entities.Account
import com.dmitriy.movie.model.account.entities.SignUpData
import kotlinx.coroutines.delay

class InMemoryAccountRepository : AccountsRepository {

    private var currentAccountLiveData = MutableLiveData<Account?>(null)

    private val accounts = mutableListOf(
        AccountRecord(
            account = Account(
                username = "admin",
                email = "admin@google.com"
            ),
            password = "qwerty"
        )
    )

    init {
        currentAccountLiveData.value = accounts[0].account
    }

    override suspend fun isSignedIn(): Boolean {
        delay(2000)
        return currentAccountLiveData.value != null
    }

    override suspend fun signIn(email: String, password: String) {
        if (email.isBlank()) throw RuntimeException(Field.Email.toString())
        if (password.isBlank()) throw RuntimeException(Field.Password.toString())
        if (password.length < 6) throw RuntimeException()

        delay(1000)
        val record = getRecordByEmail(email)
        if (record != null && record.password == password) {
            currentAccountLiveData.value = record.account
        } else {
            throw RuntimeException()
        }
    }

    override suspend fun signUp(signUpData: SignUpData) {
        signUpData.validate()

        delay(2000)
        val accountRecord = getRecordByEmail(signUpData.email)
        if (accountRecord != null) throw RuntimeException()

        val newAccount = Account(
            username = signUpData.username,
            email = signUpData.email,
            createdAt = System.currentTimeMillis()
        )
        accounts.add(AccountRecord(newAccount, signUpData.password))
    }

    override fun logout() {
        currentAccountLiveData.value = null
    }

    override fun getAccount(): LiveData<Account?> = currentAccountLiveData

    override suspend fun updateAccountUsername(newUsername: String) {
        if (newUsername.isBlank()) throw RuntimeException(Field.Username.toString())

        delay(2000)
        val currentAccount = currentAccountLiveData.value ?: throw RuntimeException()

        val updatedAccount = currentAccount.copy(username = newUsername)
        currentAccountLiveData.value = updatedAccount
        val currentRecord = getRecordByEmail(currentAccount.email!!) ?: throw RuntimeException()
        currentRecord.account = updatedAccount
    }

    private fun getRecordByEmail(email: String) = accounts.firstOrNull { it.account.email == email }

    private class AccountRecord(
        var account: Account,
        val password: String
    )
}