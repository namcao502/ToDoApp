package com.example.todoapp.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.Task
import kotlinx.android.synthetic.main.row_task.view.*

class TaskFragmentAdapter: RecyclerView.Adapter<TaskFragmentAdapter.ViewHolder>() {

    private var taskList = emptyList<Task>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = taskList[position]

        holder.itemView.title_txt.text = currentItem.title
        holder.itemView.des_txt.text = currentItem.description

        holder.itemView.setOnClickListener {
            val action = TaskFragmentDirections.actionTaskFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

    }

    fun setData(user: List<Task>){
        this.taskList = user
        notifyDataSetChanged()
    }
}