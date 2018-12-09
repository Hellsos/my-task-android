package cloud.my_task.mytaskandroid.data.Tasks

import android.arch.lifecycle.ViewModel
import cloud.my_task.mytaskandroid.data.Task
import cloud.my_task.mytaskandroid.data.TaskRepository
import cloud.my_task.mytaskandroid.data.User
import cloud.my_task.mytaskandroid.data.UserRepository

class TasksViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    fun getTasks() = taskRepository.getTasks()

    fun getCurrentTask() = taskRepository.getCurrentTask()

    fun clearTasks() = taskRepository.clearTasks()

    fun addTask(task: Task) = taskRepository.addTask(task)

    fun setCurrentTask(task: Task) = taskRepository.setCurrentTask(task)
}