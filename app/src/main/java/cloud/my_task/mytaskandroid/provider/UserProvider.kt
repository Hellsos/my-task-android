package cloud.my_task.mytaskandroid.provider

class UserProvider {

    private var loggedUser: Boolean = false;


    public fun isLoggedUser(): Boolean {
        return this.loggedUser;
    }

}