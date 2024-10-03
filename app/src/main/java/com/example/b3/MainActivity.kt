package com.example.b3

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.b3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var binding: ActivityMainBinding
    private val taskList = mutableListOf<Task>()
    private val filteredTaskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Thiết lập View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập RecyclerView với LayoutManager
        binding.rvTaskList.layoutManager = LinearLayoutManager(this)

        // Khởi tạo adapter và RecyclerView
        taskAdapter = TaskAdapter(filteredTaskList, { position ->
            taskList.removeAt(position)
            updateFilteredList()
        }, { position, isChecked ->
            taskList[position].isCompleted = isChecked
            updateFilteredList()
        })
        binding.rvTaskList.adapter = taskAdapter

        // Thiết lập padding cho giao diện
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Thêm công việc mới
        binding.btnAddTask.setOnClickListener {
            val taskName = binding.etNewTask.text.toString()
            if (taskName.isNotEmpty()) {
                val newTask = Task(taskName)
                taskList.add(newTask)
                updateFilteredList()
                binding.etNewTask.text.clear()
            }
        }

        // Lọc danh sách công việc
        binding.rgFilter.setOnCheckedChangeListener { _, checkedId ->
            updateFilteredList(checkedId)
        }
    }

    private fun updateFilteredList(checkedId: Int? = null) {
        filteredTaskList.clear()
        when (checkedId) {
            R.id.rbCompleted -> {
                filteredTaskList.addAll(taskList.filter { it.isCompleted })
            }
            R.id.rbIncomplete -> {
                filteredTaskList.addAll(taskList.filter { !it.isCompleted })
            }
            else -> {
                filteredTaskList.addAll(taskList)
            }
        }
        taskAdapter.notifyDataSetChanged()
    }
}
