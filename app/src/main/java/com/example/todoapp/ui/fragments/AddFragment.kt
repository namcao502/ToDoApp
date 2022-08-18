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
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat

@AndroidEntryPoint
class AddFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        binding.dateCreatedTxt.text = DateFormat.getDateTimeInstance().format(System.currentTimeMillis())

        binding.addBtn.setOnClickListener {
            addTask()
            findNavController().navigate(R.id.taskFragment)
        }

        return binding.root
    }

    private fun addTask() {

        val title = binding.titleEt.text.toString()
        var important = false
        val date = DateFormat.getDateTimeInstance().format(System.currentTimeMillis())

        if (binding.importantCb.isChecked) {
            important = true
        }

        if (isValid(title)){
            val task = Task(title, date, important, false,0)
            taskViewModel.addTask(task)
            Toast.makeText(requireContext(), "Added!!!", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(requireContext(), "Invalid Input!!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isValid(title: String): Boolean{
        return !(TextUtils.isEmpty(title))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}