package cloud.my_task.mytaskandroid.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class User(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val username: String,
    val verified: Boolean,
    val taskGroupLimit: Int,
    val taskLimit: Int

) {
    override fun toString(): String {
        return this.firstName + " " + this.lastName
    }

    class DeserializerSingle : ResponseDeserializable<User> {
        override fun deserialize(content: String): User? {
            return Gson().fromJson(content, User::class.java)
        }
    }

    class DeserializerArray : ResponseDeserializable<Array<User>> {
        override fun deserialize(content: String): Array<User>? {
            return Gson().fromJson(content, Array<User>::class.java)
        }
    }

}

class UserDAO {
    private var currentUser = MutableLiveData<User>()
    private val userList = mutableListOf<User>()
    private val userObservable = MutableLiveData<List<User>>()


    init {
        userObservable.postValue(userList)
    }

    fun setCurrentUser(user: User) {
        currentUser.postValue(user)
    }

    fun addUser(user: User) {
        userList.add(user)
        userObservable.postValue(userList)
    }

    fun getCurrentUser() = currentUser as LiveData<User>

    fun getUsers() = userObservable as LiveData<List<User>>

    fun clearUsers() {
        userList.clear()
        userObservable.postValue(userList)
    }

}