package com.smartreader.app.domain.model

/**
 * Immutable snapshot of the user's persisted settings.
 * Produced by [AppPreferences] and consumed by the UI layer.
 */
data class UserPreferences(
    val country: String          = "",
    val age: Int                 = 0,
    val purposes: Set<String>    = emptySet(),
    val targetLanguage: String   = "ar",
    val appLanguage: String      = "en",
    val onboardingCompleted: Boolean = false
)
