package com.acxdev.commonFunction.utils.data_store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acxdev.commonFunction.common.ConstantLib
import kotlinx.coroutines.flow.first

private val Context.myDataStore by preferencesDataStore(ConstantLib.APP_PREFS)

class DataPreferences(private val context: Context) {

    private val dataStore by lazy {
        context.myDataStore
    }

    private suspend fun save(
        key: String,
        value: Any?
    ) {
        dataStore.edit {
            when(value) {
                is Int -> {
                    it[intPreferencesKey(key)] = value
                }
                is Double -> {
                    it[doublePreferencesKey(key)] = value
                }
                is String -> {
                    it[stringPreferencesKey(key)] = value
                }
                is Boolean -> {
                    it[booleanPreferencesKey(key)] = value
                }
                is Float -> {
                    it[floatPreferencesKey(key)] = value
                }
                is Long -> {
                    it[longPreferencesKey(key)] = value
                }
                else -> {
                    it[stringPreferencesKey(key)] = value.toString()
                }
            }
        }
    }

    private suspend inline fun <reified T: Any> read(
        key: String
    ): T? {
        val clazz = T::class
        val preferences = dataStore.data.first()
        when(clazz) {
            Int::class -> {
                return preferences[intPreferencesKey(key)] as? T
            }
            Double::class -> {
                return preferences[doublePreferencesKey(key)] as? T
            }
            String::class -> {
                return preferences[stringPreferencesKey(key)] as? T
            }
            Boolean::class -> {
                return preferences[booleanPreferencesKey(key)] as? T
            }
            Float::class -> {
                return preferences[floatPreferencesKey(key)] as? T
            }
            Long::class -> {
                return preferences[longPreferencesKey(key)] as? T
            }
            else -> {
                return preferences[stringPreferencesKey(key)] as? T
            }
        }
    }
}
