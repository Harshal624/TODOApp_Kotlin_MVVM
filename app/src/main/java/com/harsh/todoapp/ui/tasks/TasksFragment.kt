package com.harsh.todoapp.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.harsh.todoapp.R
import com.harsh.todoapp.data.Task
import com.harsh.todoapp.databinding.FragmentTasksBinding
import com.harsh.todoapp.util.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), OnTaskClickListener {

    private val viewModel: TasksViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val binding = FragmentTasksBinding.bind(view)
        val taskadapter = TasksAdapter(this)

        binding.apply {
            recyclerview.apply {
                adapter = taskadapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            taskadapter.submitList(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_task, menu)

        val searchItem = menu.findItem(R.id.searchbar)
        val searchView = searchItem.actionView as SearchView

        searchView.OnQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.preferencesFlow.first().hideCompleted // read the value from preferencesflow just one time
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.sort_datecreated -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_task -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTaskClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }
}