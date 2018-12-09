package cloud.my_task.mytaskandroid.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cloud.my_task.mytaskandroid.R
import cloud.my_task.mytaskandroid.data.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TaskListAdapter.TaskListHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var taskList = emptyList<Task>()
    private var downloadedTaskList = emptyList<Task>()

    inner class TaskListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskTime: TextView = itemView.findViewById(R.id.task_time)
        val taskUserName: TextView = itemView.findViewById(R.id.task_user_name)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TaskListHolder {
        val itemView = inflater.inflate(R.layout.fragment_task_item, p0, false)
        return TaskListHolder(itemView)
    }

    override fun getItemCount(): Int {
        return downloadedTaskList.size
    }

    internal fun resetTasks() {
        downloadedTaskList = emptyList()
        taskList = emptyList()
        notifyDataSetChanged()
    }

    internal fun setTasks(tasks: List<Task>) {
        downloadedTaskList = tasks
        taskList = tasks.sortedWith(compareBy({ it.time }))
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(p0: TaskListHolder, p1: Int) {
        if (!taskList.isEmpty()) {
            val current: Task = taskList[p1]
            p0.taskName.text = current.name
            p0.taskDescription.text = current.description
            p0.taskUserName.text = "Petr Novak"
            p0.taskTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(current.time * 1000))
        }
    }

}