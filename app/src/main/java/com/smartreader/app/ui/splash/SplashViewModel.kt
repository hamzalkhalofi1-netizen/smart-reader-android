package com.smartreader.app.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smartreader.app.data.local.AppPreferences
import com.smartreader.app.util.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class SplashState {
    object Loading       : SplashState()
    object NoConnection  : SplashState()
    object GoToOnboarding: SplashState()
    object GoToMain      : SplashState()
}

class SplashViewModel(
    private val context: Context,
    private val prefs: AppPreferences
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state

    init { checkAndNavigate() }

    fun checkAndNavigate() {
        viewModelScope.launch {
            _state.value = SplashState.Loading
            // Show branding for 1.4 s even on fast connections
            delay(1_400)
            if (!NetworkUtils.isOnline(context)) {
                _state.value = SplashState.NoConnection
                return@launch
            }
            val userPrefs = prefs.userPreferences.first()
            _state.value = if (userPrefs.onboardingCompleted) {
                SplashState.GoToMain
            } else {
                SplashState.GoToOnboarding
            }
        }
    }

    class Factory(
        private val context: Context,
        private val prefs: AppPreferences
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            SplashViewModel(context.applicationContext, prefs) as T
    }
}
