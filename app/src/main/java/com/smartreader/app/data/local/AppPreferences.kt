package com.smartreader.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.smartreader.app.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences>
    by preferencesDataStore(name = "smart_reader_prefs")

/**
 * Single source of truth for all persisted user settings.
 * Uses Jetpack DataStore (Preferences) — fully coroutine-based, no ANR risk.
 */
class AppPreferences(context: Context) {

    private val ds = context.dataStore

    // ── Keys ─────────────────────────────────────────────────────────────────
    companion object {
        val KEY_COUNTRY           = stringPreferencesKey("country")
        val KEY_AGE               = intPreferencesKey("age")
        val KEY_PURPOSES          = stringSetPreferencesKey("purposes")
        val KEY_TARGET_LANGUAGE   = stringPreferencesKey("target_language")
        val KEY_APP_LANGUAGE      = stringPreferencesKey("app_language")
        val KEY_ONBOARDING_DONE   = booleanPreferencesKey("onboarding_done")
    }

    // ── Read ─────────────────────────────────────────────────────────────────
    val userPreferences: Flow<UserPreferences> = ds.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs ->
            UserPreferences(
                country            = prefs[KEY_COUNTRY]         ?: "",
                age                = prefs[KEY_AGE]             ?: 0,
                purposes           = prefs[KEY_PURPOSES]        ?: emptySet(),
                targetLanguage     = prefs[KEY_TARGET_LANGUAGE] ?: "ar",
                appLanguage        = prefs[KEY_APP_LANGUAGE]    ?: "en",
                onboardingCompleted = prefs[KEY_ONBOARDING_DONE] ?: false
            )
        }

    // ── Write ─────────────────────────────────────────────────────────────────
    suspend fun saveOnboardingData(
        country: String,
        age: Int,
        purposes: Set<String>,
        targetLanguage: String
    ) {
        ds.edit { prefs ->
            prefs[KEY_COUNTRY]          = country
            prefs[KEY_AGE]              = age
            prefs[KEY_PURPOSES]         = purposes
            prefs[KEY_TARGET_LANGUAGE]  = targetLanguage
            prefs[KEY_ONBOARDING_DONE]  = true
        }
    }

    suspend fun setAppLanguage(langCode: String) {
        ds.edit { it[KEY_APP_LANGUAGE] = langCode }
    }

    suspend fun setTargetLanguage(langCode: String) {
        ds.edit { it[KEY_TARGET_LANGUAGE] = langCode }
    }

    suspend fun resetOnboarding() {
        ds.edit { it[KEY_ONBOARDING_DONE] = false }
    }
}
