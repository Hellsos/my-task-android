package cloud.my_task.mytaskandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cloud.my_task.mytaskandroid.provider.UserProvider
import cloud.my_task.mytaskandroid.service.ApiService
import cloud.my_task.mytaskandroid.service.SharedPreferencesService
import cloud.my_task.mytaskandroid.structure.UserStructure
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userProvider: UserProvider
    private lateinit var sharedPreferencesService: SharedPreferencesService

    private lateinit var apiService: ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.apiService = ApiService()


        var self = this

        GlobalScope.launch(Dispatchers.IO) {
            this.run {

                apiService.getUserById(1).fold({ user ->
                    userProvider = UserProvider(user)

                    if (userProvider.isLoggedUser()) {

                        sharedPreferencesService = SharedPreferencesService(self)

                        val intent: Intent = if (sharedPreferencesService.welcomeDone) {
                            Intent(self, DashboardActivity::class.java).apply {
                                // putExtra(msg, "abc")
                            }
                        } else {
                            Intent(self, WelcomeActivity::class.java).apply {
                                // putExtra(msg, "abc")
                            }
                        }
                        startActivity(intent)
                        finish()

                    }
                }, { error ->
                    Log.i("", "eeror");
                })

            }
        }


    }


}
