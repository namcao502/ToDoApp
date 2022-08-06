package com.example.todoapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_task.view.*

@AndroidEntryPoint
class AddFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add, container, false)

        view.add_btn.setOnClickListener {
            addTask()
            findNavController().navigate(R.id.taskFragment)
        }

        return view
    }

    private fun addTask() {
        val title = title_et.text.toString()
        val des = description_et.text.toString()

        if (isValid(title, des)){
            val task = Task(title, des, 0)
            taskViewModel.addTask(task)
            Toast.makeText(requireContext(), "Added!!!", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(requireContext(), "Invalid Input!!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isValid(title: String, des: String): Boolean{
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(des))
    }

}