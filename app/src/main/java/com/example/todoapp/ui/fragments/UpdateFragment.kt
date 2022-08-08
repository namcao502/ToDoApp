package com.example.todoapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add.view.description_et_add
import kotlinx.android.synthetic.main.fragment_add.view.title_et_add
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

@AndroidEntryPoint
class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update, container, false)

        view.title_et_update.setText(args.task.title)
        view.description_et_update.setText(args.task.description)

        view.update_btn.setOnClickListener {
            updateTask()
            findNavController().navigate(R.id.taskFragment)
        }

        createMenu()

        return view
    }

    private fun updateTask() {

        val title = title_et_update.text.toString()
        val des = description_et_update.text.toString()

        if (isValid(title, des)){
            val updatedTask = Task(title, des, args.task.id)
            taskViewModel.updateTask(updatedTask)
            Toast.makeText(requireContext(), "Updated!!!", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(requireContext(), "Invalid Input!!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteTask(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete ${args.task.title}")
        builder.setMessage("Do you really want to delete this task?")
        builder.setPositiveButton("Yes"){ _, _ ->
            taskViewModel.deleteTask(args.task)
            findNavController().navigate(R.id.taskFragment)
            Toast.makeText(requireContext(), "Deleted!!!", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.create().show()
    }

    private fun isValid(title: String, des: String): Boolean{
        return !(TextUtils.isEmpty(title)) && !(TextUtils.isEmpty(des))
    }

    private fun createMenu(){
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_delete, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                Toast.makeText(requireContext(), "Delete Clicked", Toast.LENGTH_SHORT).show()
//                return false
                if (menuItem.itemId == R.id.menu_delete){
                    deleteTask()
                    return true
                }
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}