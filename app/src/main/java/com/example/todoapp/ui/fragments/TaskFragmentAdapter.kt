package com.example.todoapp.ui.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.Task
import kotlinx.android.synthetic.main.row_task.view.*

class TaskFragmentAdapter(private val itemClickListener: ItemClickListener): RecyclerView.Adapter<TaskFragmentAdapter.ViewHolder>() {

    var taskList = emptyList<Task>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

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
        holder.itemView.done_cb.isChecked = currentItem.done
        holder.itemView.complete_date_txt.text = "Complete: ".plus(currentItem.date_done)
        holder.itemView.create_date_txt.text = "Created: ".plus(currentItem.date)

        if (currentItem.important){
            holder.itemView.important_img.visibility = View.VISIBLE
        }
        else {
            holder.itemView.important_img.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val action = TaskFragmentDirections.actionTaskFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.done_cb.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                itemClickListener.onClick(p1, currentItem)
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(user: List<Task>){
        this.taskList = user
        notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun onClick(check: Boolean, task: Task)
    }

}