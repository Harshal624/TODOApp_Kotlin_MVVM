package com.harsh.todoapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.harsh.todoapp.data.PreferenceManager
import com.harsh.todoapp.data.Task
import com.harsh.todoapp.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    val searchQuery = MutableStateFlow("")
/*    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)*/

    val preferencesFlow = preferenceManager.preferencesFlow


    private val tasksFlow = combine(
        searchQuery, preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }
    val tasks = tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferenceManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferenceManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) {

    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) =
        viewModelScope.launch {
            taskDao.update(task.copy(completed_state = isChecked))
        }


}

enum class SortOrder {
    BY_NAME, BY_DATE
}