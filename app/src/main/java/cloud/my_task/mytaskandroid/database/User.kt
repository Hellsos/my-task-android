package cloud.my_task.mytaskandroid.database

import android.arch.persistence.room.*

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val verified: Boolean,
    val taskGroupLimit: Int,
    val taskLimit: Int
)

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM User WHERE id == :id")
    fun getUserByUserId(id: Int): List<User>

}