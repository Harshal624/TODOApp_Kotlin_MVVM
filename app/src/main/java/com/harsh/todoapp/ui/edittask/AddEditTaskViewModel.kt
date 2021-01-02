package com.harsh.todoapp.ui.edittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.harsh.todoapp.data.Task
import com.harsh.todoapp.data.TaskDao

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


}