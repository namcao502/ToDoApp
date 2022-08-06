package com.example.todoapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task.view.*
import kotlinx.coroutines.flow.observeOn

@AndroidEntryPoint
class TaskFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val adapter: TaskFragmentAdapter by lazy { TaskFragmentAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_task, container, false)

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }

        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel.readAllTasks.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        return view
    }
}