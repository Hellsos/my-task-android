package cloud.my_task.mytaskandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cloud.my_task.mytaskandroid.provider.UserProvider
import cloud.my_task.mytaskandroid.service.SharedPreferencesService

class MainActivity : AppCompatActivity() {

    private lateinit var userProvider: UserProvider
    private lateinit var sharedPreferencesService: SharedPreferencesService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.userProvider = UserProvider()
        this.sharedPreferencesService = SharedPreferencesService(this)

        val intent: Intent = if (!this.sharedPreferencesService.welcomeDone) {
            Intent(this, WelcomeActivity::class.java).apply {
                // putExtra(msg, "abc")
            }
        } else {
            Intent(this, DashboardActivity::class.java).apply {
                // putExtra(msg, "abc")
            }
        }
        startActivity(intent)

        finish()

    }


}
