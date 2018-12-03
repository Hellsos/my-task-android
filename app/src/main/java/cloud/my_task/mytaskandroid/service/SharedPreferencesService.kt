package cloud.my_task.mytaskandroid.service

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesService(context: Context) {
    companion object {
        val WELCOME_DONE = "data.source.prefs.WELCOME_DONE"
        val CURRENT_STATE = "data.source.prefs.CURRENT_STATE"
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var welcomeDone: Boolean = false
        get() = sharedPreferences.getBoolean(WELCOME_DONE, false)

    var currentState: String = ""
        get() = sharedPreferences.getString(CURRENT_STATE, "")

    fun saveWelcomeDone(state: Boolean) {
        sharedPreferences.edit().putBoolean(WELCOME_DONE, state).commit()
    }

    fun saveCurrentState(state: String) {
        sharedPreferences.edit().putString(CURRENT_STATE, state).commit()
    }

    fun clear() {
        this.sharedPreferences.edit().clear().apply()
    }
}