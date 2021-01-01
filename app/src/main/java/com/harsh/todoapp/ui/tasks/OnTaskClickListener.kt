package com.harsh.todoapp.ui.tasks

import com.harsh.todoapp.data.Task

interface OnTaskClickListener {
    fun onTaskClick(task: Task)
    fun onCheckBoxClick(task: Task, isChecked: Boolean)
}