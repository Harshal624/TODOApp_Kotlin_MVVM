package com.harsh.todoapp.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.harsh.todoapp.ui.tasks.SortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val datastore = context.createDataStore("user_preferences")

    val preferencesFlow = datastore.data
        .catch {
            if (this is IOException) {
                this.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )
            val hideCompleted =
                preferences[PreferencesKeys.HIDE_COMPLETED] ?: false

            FilterPreferences(sortOrder, hideCompleted)
        }

    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        datastore.edit {
            it[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        datastore.edit {
            it[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }
}

data class FilterPreferences(
    val sortOrder: SortOrder, val hideCompleted: Boolean
)