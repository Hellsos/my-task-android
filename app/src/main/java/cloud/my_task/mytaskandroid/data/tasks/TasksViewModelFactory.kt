package cloud.my_task.mytaskandroid.data.users

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import cloud.my_task.mytaskandroid.data.TaskRepository
import cloud.my_task.mytaskandroid.data.Tasks.TasksViewModel
import cloud.my_task.mytaskandroid.data.UserRepository


class TasksViewModelFactory(private val taskRepository: TaskRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TasksViewModel(taskRepository) as T
    }
}