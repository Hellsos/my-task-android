package cloud.my_task.mytaskandroid.data

class FakeDatabase private constructor() {

    var userDAO = UserDAO()
        private set

    var taskDAO = TaskDAO()
        private set


    companion object {
        @Volatile
        private var instance: FakeDatabase? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FakeDatabase().also {
                instance = it
            }
        }
    }
}