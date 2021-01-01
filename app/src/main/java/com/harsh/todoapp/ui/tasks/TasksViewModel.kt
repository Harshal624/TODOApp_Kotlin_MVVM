package com.harsh.todoapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.harsh.todoapp.data.PreferenceManager
import com.harsh.todoapp.data.Task
import com.harsh.todoapp.data.TaskDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferenceManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()

    val tasksEvent = tasksEventChannel.receiveAsFlow()


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

    fun onTaskSwiped(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
        }
    }

    fun onUndoDeleteClick(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }


}

enum class SortOrder {
    BY_NAME, BY_DATE
}