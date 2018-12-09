package cloud.my_task.mytaskandroid.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cloud.my_task.mytaskandroid.data.User
import cloud.my_task.mytaskandroid.R

class SearchUserListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<SearchUserListAdapter.SearchUserListHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var searchUserList = emptyList<User>()
    private var downloadedUserList = emptyList<User>()

    inner class SearchUserListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userItemName: TextView = itemView.findViewById(R.id.search_user_name)
        val userItemEmail: TextView = itemView.findViewById(R.id.search_user_email)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchUserListHolder {
        val itemView = inflater.inflate(R.layout.fragment_search_user_item, p0, false)
        return SearchUserListHolder(itemView)
    }

    override fun getItemCount(): Int {
        return searchUserList.size
    }

    internal fun resetSearchUsers() {
        downloadedUserList = emptyList()
        searchUserList = emptyList()
        notifyDataSetChanged()
    }

    internal fun setSearchUser(users: List<User>) {
        downloadedUserList = users
        searchUserList = users
        notifyDataSetChanged()
    }


    internal fun filterSearchUser(filter: String) {
        searchUserList = downloadedUserList.filter { user: User ->
            user.toString().contains(filter)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(p0: SearchUserListHolder, p1: Int) {
        val current = searchUserList[p1]
        p0.userItemName.text = current.toString()
        p0.userItemEmail.text = current.username
    }

}