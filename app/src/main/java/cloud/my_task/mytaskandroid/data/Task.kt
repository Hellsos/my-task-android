package cloud.my_task.mytaskandroid.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Task(
    val id: Int? = null,
    val name: String,
    val description: String,
    val time: Long,
    val latitude: Float,
    val longitude: Float

) {

    class DeserializerSingle : ResponseDeserializable<Task> {
        override fun deserialize(content: String): Task? {
            return Gson().fromJson(content, Task::class.java)
        }
    }

    class DeserializerArray : ResponseDeserializable<Array<Task>> {
        override fun deserialize(content: String): Array<Task>? {
            return Gson().fromJson(content, Array<Task>::class.java)
        }
    }

}

class TaskDAO {
    private var currentTask = MutableLiveData<Task>()
    private val taskList = mutableListOf<Task>()
    private val taskObservable = MutableLiveData<List<Task>>()


    init {
        taskObservable.postValue(taskList)
    }

    fun setCurrentTask(task: Task) {
        currentTask.postValue(task)
    }

    fun addTask(task: Task) {
        taskList.add(task)
        taskObservable.postValue(taskList)
    }

    fun getCurrentTask() = currentTask as LiveData<Task>

    fun getTasks() = taskObservable as LiveData<List<Task>>

    fun clearTasks() {
        taskList.clear()
        taskObservable.postValue(taskList)
    }

}