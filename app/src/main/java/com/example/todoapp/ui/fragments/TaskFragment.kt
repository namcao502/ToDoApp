package com.example.todoapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class TaskFragment : Fragment(), TaskFragmentAdapter.ItemClickListener {

    private val taskViewModel: TaskViewModel by viewModels()
    private val taskAdapter: TaskFragmentAdapter by lazy { TaskFragmentAdapter(this) }

    private var _binding: FragmentTaskBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTaskBinding.inflate(layoutInflater, container, false)

        val view = binding.root

        createMenu()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }

        binding.apply {
            recyclerView.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.taskList[viewHolder.adapterPosition]
                    taskViewModel.deleteTask(task)
                }

            }).attachToRecyclerView(recyclerView)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            taskViewModel.taskEvent.collect { event ->
                when(event){
                    is TaskViewModel.TaskEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Deleted", Snackbar.LENGTH_LONG).setAction("UNDO"){
                            taskViewModel.undoDeleteTask(event.task)
                        }.show()
                    }
                }
            }
        }


        getAllTasks()

        return view
    }

    private fun getAllTasks() {
        taskViewModel.readAllTasks().observe(viewLifecycleOwner) { task ->
            taskAdapter.setData(task)
        }
    }

    private fun createMenu(){

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)

                val search = menu.findItem(R.id.main_search)
                val searchView = search?.actionView as? SearchView

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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

                })

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.main_delete -> deleteAllTasks()
                    R.id.main_search -> {

                    }
                    R.id.main_sort_date -> {
                        sortDateTask()
                    }
                    R.id.main_sort_title -> {
                        sortTitleTask()
                    }
                    R.id.main_hide -> {
                        if (menuItem.isChecked){
                            getAllTasks()
                            menuItem.isChecked = false
                        }
                        else {
                            hideCompletedTasks()
                            menuItem.isChecked = true
                        }
                    }
                    R.id.main_important -> {
                        if (menuItem.isChecked){
                            getAllTasks()
                            menuItem.isChecked = false
                        }
                        else {
                            showImportantTasks()
                            menuItem.isChecked = true
                        }
                    }
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun searchTask(query: String) {
        val text = "%$query%"
        taskViewModel.searchTask(text).observe(viewLifecycleOwner) { task ->
            taskAdapter.setData(task)
        }
    }

    private fun sortTitleTask() {
        taskViewModel.sortTitleTask().observe(viewLifecycleOwner) { task ->
            taskAdapter.setData(task)
        }
    }

    private fun sortDateTask() {
        taskViewModel.sortDateTask().observe(viewLifecycleOwner) { task ->
            taskAdapter.setData(task)
        }
    }

    private fun hideCompletedTasks() {
        taskViewModel.hideCompletedTasks().observe(viewLifecycleOwner) { task ->
            taskAdapter.setData(task)
        }
    }

    private fun showImportantTasks() {
        taskViewModel.showImportantTasks().observe(viewLifecycleOwner) { task ->
            taskAdapter.setData(task)
        }
    }

    private fun deleteAllTasks(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete all tasks")
        builder.setMessage("Do you really want to delete all tasks?\n\nYou can delete each task by swipe them to left or right.")
        builder.setPositiveButton("Yes"){ _, _ ->
            taskViewModel.deleteAllTasks()
            Toast.makeText(requireContext(), "Deleted!!!", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(check: Boolean, task: Task) {
        task.done = check
        taskViewModel.updateTask(task)
    }
}