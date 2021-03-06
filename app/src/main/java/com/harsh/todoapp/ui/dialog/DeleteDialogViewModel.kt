package com.harsh.todoapp.ui.dialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.harsh.todoapp.data.TaskDao
import com.harsh.todoapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteDialogViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteCompletedTasks()
    }
}