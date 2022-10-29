package com.dmitriy.movie.view.tabs

import com.dmitriy.movie.model.account.AccountsRepository
import com.dmitriy.movie.model.account.InMemoryAccountRepository

object Repositories {

    val accountRepository: AccountsRepository = InMemoryAccountRepository()
}