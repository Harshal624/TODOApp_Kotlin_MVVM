package com.harsh.todoapp.ui.edittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.todoapp.data.Task
import com.harsh.todoapp.data.TaskDao
import com.harsh.todoapp.util.Constants.ADD_TASK_RESULT_OK
import com.harsh.todoapp.util.Constants.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    //safe args can be retrieve via savedstatehandle
    val task = state.get<Task>("task") //has to be the same as declared in nav_graph

    var taskName = state.get<String>("taskname") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskname", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }
        if (task != null) {
            val updatedTask = task.copy(name = taskName, important = taskImportance)
            updatedTask(updatedTask)
        } else {
            val newTask = Task(taskName, taskImportance)
            createTask(newTask)
        }
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(
            element = AddEditTaskEvent.NavigateBackWithResult(
                ADD_TASK_RESULT_OK
            )
        )
    }

    private fun updatedTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(
            element = AddEditTaskEvent.NavigateBackWithResult(
                EDIT_TASK_RESULT_OK
            )
        )
    }

    private fun showInvalidInputMessage(text: String) {
        viewModelScope.launch {
            addEditTaskEventChannel.send(element = AddEditTaskEvent.ShowInvalidInputMessage(text))
        }
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}