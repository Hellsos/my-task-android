package cloud.my_task.mytaskandroid.service

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesService(context: Context) {
    companion object {
        val WELCOME_DONE = "data.source.prefs.WELCOME_DONE"
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var welcomeDone = sharedPreferences.getBoolean(WELCOME_DONE, false)
        set(value) = sharedPreferences.edit().putBoolean(WELCOME_DONE, value).apply()


    fun clear() {
        this.sharedPreferences.edit().clear().apply()
    }
}