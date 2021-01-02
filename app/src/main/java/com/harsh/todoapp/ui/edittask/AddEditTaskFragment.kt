package com.harsh.todoapp.ui.edittask

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.harsh.todoapp.R
import com.harsh.todoapp.databinding.FragmentEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditTaskBinding.bind(view)
        binding.apply {
            etTaskName.setText(viewModel.taskName)
            cbImportant.isChecked = viewModel.taskImportance
            cbImportant.jumpDrawablesToCurrentState() //skip checkbox check animation upon fragment load
            tvDateCreated.isVisible = viewModel.task != null
            tvDateCreated.text = "Created: ${viewModel.task?.createdDateFormatted}"
        }
    }
}