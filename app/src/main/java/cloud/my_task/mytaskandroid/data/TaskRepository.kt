package cloud.my_task.mytaskandroid.data

class TaskRepository private constructor(private val taskDAO: TaskDAO) {

    fun addTask(task: Task) {
        taskDAO.addTask(task)
    }

    fun getTasks() = taskDAO.getTasks()

    fun setCurrentTask(task: Task) {
        taskDAO.setCurrentTask(task)
    }

    fun getCurrentTask() = taskDAO.getCurrentTask()

    fun clearTasks() = taskDAO.clearTasks()

    companion object {
        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(taskDAO: TaskDAO) = instance ?: synchronized(this) {
            instance ?: TaskRepository(taskDAO).also {
                instance = it
            }
        }
    }
}