package cloud.my_task.mytaskandroid.service

import cloud.my_task.mytaskandroid.data.Task
import cloud.my_task.mytaskandroid.data.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class ApiService {

    private val baseUrl: String = "http://158.196.59.228:3001/"


    fun getUserById(userId: Int): Result<User, FuelError> {
        val url = baseUrl + "user/" + userId.toString()

        return url.httpGet()
            .responseObject(User.DeserializerSingle())
            .third

    }

    fun createTask(task: Task): Result<Task, FuelError> {
        val url = baseUrl + "task"

        return url.httpPost()
            .header(Pair("name", task.name))
            .header(Pair("description", task.description))
            .header(Pair("time", task.time))
            .header(Pair("latitude", task.latitude))
            .header(Pair("longitude", task.longitude))
            .responseObject(Task.DeserializerSingle())
            .third
    }

    fun getTaskList(): Result<Array<Task>, FuelError> {
        val url = baseUrl + "task/list"

        return url.httpGet()
            .responseObject(Task.DeserializerArray())
            .third

    }

    fun getUserList(): Result<Array<User>, FuelError> {
        val url = baseUrl + "user/list"

        return url.httpGet()
            .responseObject(User.DeserializerArray())
            .third

    }
}