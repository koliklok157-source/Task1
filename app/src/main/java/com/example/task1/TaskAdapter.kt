package com.example.task1

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: MutableList<Task>, private val saveTasks: () -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val textViewTask: TextView = itemView.findViewById(R.id.textViewTask)
        val buttonDeleteTask: Button = itemView.findViewById(R.id.buttonDeleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textViewTask.text = task.text
        holder.checkBoxTask.isChecked = task.isCompleted

        if (task.isCompleted) {
            holder.textViewTask.paintFlags = holder.textViewTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.textViewTask.paintFlags = holder.textViewTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            if (isChecked) {
                holder.textViewTask.paintFlags = holder.textViewTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.textViewTask.paintFlags = holder.textViewTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            saveTasks()
        }

        holder.buttonDeleteTask.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                tasks.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                saveTasks()
            }
        }
    }

    override fun getItemCount() = tasks.size
}
