package com.example.todoapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_add.view.description_et
import kotlinx.android.synthetic.main.fragment_add.view.title_et
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

        view.title_et.setText(args.task.title)
        view.description_et.setText(args.task.description)

        view.update_btn.setOnClickListener {
            updateTask()
            findNavController().navigate(R.id.taskFragment)
        }

        return view
    }

    private fun updateTask() {
        TODO("Not yet implemented")
    }

}