package cloud.my_task.mytaskandroid.data.users

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import cloud.my_task.mytaskandroid.data.UserRepository


class UsersViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UsersViewModel(userRepository) as T
    }
}