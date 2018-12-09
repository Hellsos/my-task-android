package cloud.my_task.mytaskandroid.data.users

import android.arch.lifecycle.ViewModel
import cloud.my_task.mytaskandroid.data.User
import cloud.my_task.mytaskandroid.data.UserRepository

class UsersViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUsers() = userRepository.getUsers()

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun clearUsers() = userRepository.clearUsers()

    fun addUser(user: User) = userRepository.addUser(user)

    fun setCurrentUser(user: User) = userRepository.setCurrentUser(user)
}