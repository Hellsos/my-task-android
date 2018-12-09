package cloud.my_task.mytaskandroid.data

class UserRepository private constructor(private val userDAO: UserDAO) {

    fun addUser(user: User) {
        userDAO.addUser(user)
    }

    fun getUsers() = userDAO.getUsers()

    fun setCurrentUser(user: User) {
        userDAO.setCurrentUser(user)
    }

    fun getCurrentUser() = userDAO.getCurrentUser()

    fun clearUsers() = userDAO.clearUsers()

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDAO: UserDAO) = instance ?: synchronized(this) {
            instance ?: UserRepository(userDAO).also {
                instance = it
            }
        }
    }
}