package cloud.my_task.mytaskandroid.structure

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class UserStructure(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val verified: Boolean,
    val taskGroupLimit: Int,
    val taskLimit: Int
) {
    class DeserializerSingle : ResponseDeserializable<UserStructure> {
        override fun deserialize(content: String): UserStructure? {
            return Gson().fromJson(content, UserStructure::class.java)
        }
    }
    class DeserializerArray : ResponseDeserializable<Array<UserStructure>> {
        override fun deserialize(content: String): Array<UserStructure>? {
            return Gson().fromJson(content, Array<UserStructure>::class.java)
        }
    }
}