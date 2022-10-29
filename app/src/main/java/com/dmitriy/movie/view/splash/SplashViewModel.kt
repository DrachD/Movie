package com.dmitriy.movie.view.splash

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmitriy.movie.model.account.AccountsRepository
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors

class SplashViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveData<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.value = accountsRepository.isSignedIn()
        }
    }
}