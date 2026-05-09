package com.smartreader.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartreader.app.data.local.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel(private val prefs: AppPreferences) : ViewModel() {

    // ── Per-step state ────────────────────────────────────────────────────────

    private val _country = MutableStateFlow("")
    val country: StateFlow<String> = _country.asStateFlow()

    private val _age = MutableStateFlow(0)
    val age: StateFlow<Int> = _age.asStateFlow()

    private val _purposes = MutableStateFlow<Set<String>>(emptySet())
    val purposes: StateFlow<Set<String>> = _purposes.asStateFlow()

    private val _targetLanguage = MutableStateFlow("ar")
    val targetLanguage: StateFlow<String> = _targetLanguage.asStateFlow()

    // ── Mutators ──────────────────────────────────────────────────────────────

    fun setCountry(c: String)  { _country.value = c.trim() }
    fun setAge(a: Int)         { _age.value = a }
    fun setTargetLanguage(l: String) { _targetLanguage.value = l }

    fun togglePurpose(key: String) {
        _purposes.value = _purposes.value.toMutableSet().apply {
            if (!add(key)) remove(key)
        }
    }

    // ── Validation ────────────────────────────────────────────────────────────

    fun isPageValid(page: Int): Boolean = when (page) {
        0 -> _country.value.isNotBlank()
        1 -> _age.value in 10..120
        2 -> _purposes.value.isNotEmpty()
        3 -> _targetLanguage.value.isNotBlank()
        else -> true
    }

    fun validationMessage(page: Int): String = when (page) {
        0 -> "Please select your country"
        1 -> "Please enter a valid age (10–120)"
        2 -> "Please select at least one interest"
        3 -> "Please select a translation language"
        else -> ""
    }

    // ── Persist ───────────────────────────────────────────────────────────────

    suspend fun saveOnboarding() {
        prefs.saveOnboardingData(
            country        = _country.value,
            age            = _age.value,
            purposes       = _purposes.value,
            targetLanguage = _targetLanguage.value
        )
    }

    // ── Factory ───────────────────────────────────────────────────────────────

    class Factory(private val prefs: AppPreferences) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            OnboardingViewModel(prefs) as T
    }
}
