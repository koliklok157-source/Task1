package com.example.task1

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)

        loadTasks()

        taskAdapter = TaskAdapter(tasks, ::saveTasks)
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = taskAdapter

        buttonAddTask.setOnClickListener {
            val taskText = editTextTask.text.toString()
            if (taskText.isNotEmpty()) {
                tasks.add(Task(taskText))
                taskAdapter.notifyItemInserted(tasks.size - 1)
                editTextTask.text.clear()
                saveTasks()
            }
        }
    }

    private fun saveTasks() {
        val sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val tasksJson = gson.toJson(tasks)
        editor.putString("taskList", tasksJson)
        editor.apply()
    }

    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val tasksJson = sharedPreferences.getString("taskList", null)
        if (tasksJson != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            tasks.addAll(gson.fromJson(tasksJson, type))
        }
    }
}
