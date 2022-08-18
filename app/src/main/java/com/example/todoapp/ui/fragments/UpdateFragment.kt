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
import com.example.todoapp.databinding.FragmentUpdateBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat

@AndroidEntryPoint
class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val taskViewModel: TaskViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)

        val view = binding.root

        binding.titleEt.setText(args.task.title)

        binding.importantCb.isChecked = args.task.important

        binding.doneCb.isChecked = args.task.done

        binding.dateCreatedTxt.text = "Date created: ".plus(args.task.date)

        binding.updateBtn.setOnClickListener {
            updateTask()
            findNavController().navigate(R.id.taskFragment)
        }

        createMenu()

        return view
    }

    private fun updateTask() {

        //keep date information
        val title = binding.titleEt.text.toString()
//        val date = DateFormat.getDateTimeInstance().format(System.currentTimeMillis())
        val date = args.task.date

        var important = false
        if (binding.importantCb.isChecked) {
            important = true
        }

        var done = false
        if (binding.doneCb.isChecked) {
            done = true
        }

        if (isValid(title)){
            val updatedTask = Task(title, date, important, done, args.task.id)
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

    private fun isValid(title: String): Boolean{
        return !(TextUtils.isEmpty(title))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}