package com.example.todoapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Query
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

        createMenu()

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }

        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel.readAllTasks().observe(viewLifecycleOwner) { task ->
            adapter.setData(task)
        }

        return view
    }

    private fun createMenu(){

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider, SearchView.OnQueryTextListener {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.menu_main, menu)
//                val search = menu.findItem(R.id.search)
//                val searchView = search?.actionView as? SearchView
//                searchView?.isSubmitButtonEnabled = true
//                searchView?.setOnQueryTextListener(this)

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.delete -> deleteAllTasks()
//                    R.id.search -> {
//
//                    }
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    searchTask(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    searchTask(newText)
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun searchTask(query: String) {
        val text = "%$query%"
        taskViewModel.searchTask(text).observe(viewLifecycleOwner) { task ->
            adapter.setData(task)
        }
    }

    private fun deleteAllTasks(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete all tasks")
        builder.setMessage("Do you really want to delete all tasks?")
        builder.setPositiveButton("Yes"){ _, _ ->
            taskViewModel.deleteAllTasks()
            Toast.makeText(requireContext(), "Deleted!!!", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        builder.create().show()
    }
}