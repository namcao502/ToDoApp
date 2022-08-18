package com.example.todoapp.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import java.text.SimpleDateFormat
import java.util.*

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

        binding.createdTxt.text = "Created: ".plus(args.task.date)

        val date = args.task.date_done.substring(0, 12)
        val time = args.task.date_done.substring(13)

        binding.completeDateTxt.text = date
        binding.completeTimeTxt.text = time

        binding.updateBtn.setOnClickListener {
            if (!updateTask()){
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

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]

            TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    binding.completeTimeTxt.text = "$selectedHour:$selectedMinute"
                }, hour, minute,
                true).show()
        }

        createMenu()

        return view
    }

    private fun updateTask(): Boolean {
        val title = binding.titleEt.text.toString()
        var important = false
        val createDate = args.task.date

        val completeDate = binding.completeDateTxt.text.toString()
        val completeTime = binding.completeTimeTxt.text.toString()

        if (binding.importantCb.isChecked) {
            important = true
        }

        if (isValid(title, completeDate, completeTime, createDate)){
            val completeDateTime = completeDate.plus(" ".plus(completeTime))
            val task = Task(title, createDate, important, false, completeDateTime,0)
            taskViewModel.updateTask(task)
            Toast.makeText(requireContext(), "Added!!!", Toast.LENGTH_LONG).show()
            return true
        }
        else {
            Toast.makeText(requireContext(), "Invalid Input!!!", Toast.LENGTH_LONG).show()
            return false
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

    private fun isValid(title: String, completeDate: String, completeTime: String, createDate: String): Boolean{

        val sdf = SimpleDateFormat("MMM dd, yyyy")

        return if (completeDate == "Date" || completeTime == "Time"){
            false
        } else {
            val create = sdf.parse(createDate)
            val complete = sdf.parse(completeDate)
            !((TextUtils.isEmpty(title)) || (complete < create))
        }
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