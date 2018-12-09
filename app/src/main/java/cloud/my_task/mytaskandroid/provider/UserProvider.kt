package cloud.my_task.mytaskandroid.provider

import android.arch.lifecycle.ViewModelProviders
import cloud.my_task.mytaskandroid.data.FakeDatabase
import cloud.my_task.mytaskandroid.data.User
import cloud.my_task.mytaskandroid.data.UserRepository
import cloud.my_task.mytaskandroid.data.users.UsersViewModel
import cloud.my_task.mytaskandroid.data.users.UsersViewModelFactory
import cloud.my_task.mytaskandroid.structure.UserStructure
import cloud.my_task.mytaskandroid.utilities.InjectorUtils

class UserProvider {

    private lateinit var user: UserStructure;

    private var userViewModeL: UsersViewModel =
        UsersViewModel(UserRepository.getInstance(FakeDatabase.getInstance().userDAO))

    constructor()

    constructor(user: User) {
        this.userViewModeL.setCurrentUser(user)
    }

    fun isLoggedUser(): Boolean {
        return true
    }


}