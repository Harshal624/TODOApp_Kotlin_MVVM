package com.harsh.todoapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.harsh.todoapp.data.TaskDao

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
}