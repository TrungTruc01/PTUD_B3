package com.example.b3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskDeleted: (Int) -> Unit,
    private val onTaskStatusChanged: (Int, Boolean) -> Unit // Thêm callback cho việc thay đổi trạng thái
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.name

        // Đặt trạng thái checkbox
        holder.checkBox.setOnCheckedChangeListener(null) // Đặt listener thành null để tránh trigger không mong muốn
        holder.checkBox.isChecked = task.isCompleted // Đặt trạng thái checkbox

        // Lắng nghe sự thay đổi trạng thái checkbox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked // Cập nhật trạng thái hoàn thành của công việc
            onTaskStatusChanged(position, isChecked) // Gọi callback để xử lý thay đổi trạng thái
        }

        // Xử lý sự kiện nút xóa
        holder.deleteButton.setOnClickListener {
            onTaskDeleted(position) // Gọi callback để xử lý xóa công việc
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}

