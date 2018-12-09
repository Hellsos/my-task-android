package cloud.my_task.mytaskandroid.utilities

import cloud.my_task.mytaskandroid.data.FakeDatabase
import cloud.my_task.mytaskandroid.data.TaskRepository
import cloud.my_task.mytaskandroid.data.UserRepository
import cloud.my_task.mytaskandroid.data.users.TasksViewModelFactory
import cloud.my_task.mytaskandroid.data.users.UsersViewModelFactory

object InjectorUtils {
    fun provideUsersViewModelFactory(): UsersViewModelFactory {
        return UsersViewModelFactory(UserRepository.getInstance(FakeDatabase.getInstance().userDAO))
    }

    fun provideTasksViewModelFactory(): TasksViewModelFactory {
        return TasksViewModelFactory(TaskRepository.getInstance(FakeDatabase.getInstance().taskDAO))
    }
}