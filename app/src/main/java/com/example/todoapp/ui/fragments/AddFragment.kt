package com.example.todoapp.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddFragment : Fragment() {

    private val taskViewModel: TaskViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        binding.addBtn.setOnClickListener {
            if (!addTask()){
                return@setOnClickListener
            }
            findNavController().navigate(R.id.taskFragment)
        }

        binding.completeDateTxt.setOnClickListener {

            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val sdf = SimpleDateFormat("MMM dd, yyyy")
                    binding.completeDateTxt.text = sdf.format(cal.time)

            }, year, month, day).show()
        }

        binding.completeTimeTxt.setOnClickListener {

            val currentTime = Calendar.getInstance()
            val hour = currentTime[Calendar.HOUR_OF_DAY]
            val minute = currentTime[Calendar.MINUTE]

            TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    if (selectedHour < 10){
                        binding.completeTimeTxt.text = "0$selectedHour:$selectedMinute"
                    }
                    else {
                        binding.completeTimeTxt.text = "$selectedHour:$selectedMinute"
                    }
                }, hour, minute,
                true).show()
        }

        return binding.root
    }

    private fun addTask(): Boolean {

        val title = binding.titleEt.text.toString()
        var important = false
        val createDate = binding.clockTxt.text.toString()

        val completeDate = binding.completeDateTxt.text.toString()
        val completeTime = binding.completeTimeTxt.text.toString()

        if (binding.importantCb.isChecked) {
            important = true
        }

        return if (isValid(title, completeDate, completeTime, createDate)){
            val completeDateTime = completeDate.plus(" ".plus(completeTime))
            val task = Task(title, createDate, important, false, completeDateTime,0)
            taskViewModel.addTask(task)
            Toast.makeText(requireContext(), "Added!!!", Toast.LENGTH_LONG).show()
            true
        } else {
            Toast.makeText(requireContext(), "Invalid Input!!!", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun isValid(title: String, completeDate: String, completeTime: String, createDate: String): Boolean{
        return if (completeDate == "Date" || completeTime == "Time"){
            false
        } else {
            val sdf = SimpleDateFormat("MMM dd, yyyy")
            val create = sdf.parse(createDate)
            val complete = sdf.parse(completeDate)
            !((TextUtils.isEmpty(title)) || (complete < create))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}