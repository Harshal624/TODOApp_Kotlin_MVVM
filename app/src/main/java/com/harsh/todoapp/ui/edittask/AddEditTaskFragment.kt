package com.harsh.todoapp.ui.edittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.harsh.todoapp.R
import com.harsh.todoapp.databinding.FragmentEditTaskBinding
import com.harsh.todoapp.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.coroutines.flow.collect


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

            etTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            cbImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            fab_AddTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.addEditTaskEvent.collect {
                when (it) {
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), it.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.etTaskName.clearFocus()
                        //using fragment result api
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to it.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}