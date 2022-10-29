package com.dmitriy.movie.view.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmitriy.movie.model.account.AccountsRepository

class MainActivityViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    var username = _username
}