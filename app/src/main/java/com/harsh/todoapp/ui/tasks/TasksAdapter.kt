package com.harsh.todoapp.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harsh.todoapp.data.Task
import com.harsh.todoapp.databinding.ItemTaskBinding

class TasksAdapter(private val listener: OnTaskClickListener) :
    ListAdapter<Task, TasksAdapter.ViewHolder>(
        DiffCallBack()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onTaskClick(task)
                    }
                }
                checkbox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkbox.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                tvName.text = task.name
                tvName.paint.isStrikeThruText = task.completed_state
                checkbox.isChecked = task.completed_state
                ivError.isVisible = task.important
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}