package com.example.calc.domain

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

    suspend fun saveDarkLightMode(isNightModeEnabled: Boolean) {
        context.dataStore.edit {
            it[DAY_NIGHT_MODE] = isNightModeEnabled
        }
    }

    fun retrieveDarkLightMode(): Flow<Boolean?> = context.dataStore.data
        .catch { if (it is IOException) it.printStackTrace() }
        .map { it[DAY_NIGHT_MODE] }

    companion object {
        private const val DATA_STORE_NAME = "preferences"
        val DAY_NIGHT_MODE = booleanPreferencesKey("DayNightModeKey")
    }

}