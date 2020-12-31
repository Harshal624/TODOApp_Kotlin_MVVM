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
import androidx.recyclerview.widget.LinearLayoutManager
import com.harsh.todoapp.R
import com.harsh.todoapp.databinding.FragmentTasksBinding
import com.harsh.todoapp.util.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TasksViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val binding = FragmentTasksBinding.bind(view)
        val taskadapter = TasksAdapter()

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

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_name -> {
                true
            }
            R.id.sort_datecreated -> {
                true
            }
            R.id.action_delete_all_completed_task -> {
                item.isChecked = !item.isChecked
                true
            }
            R.id.action_delete_all_completed_task -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}