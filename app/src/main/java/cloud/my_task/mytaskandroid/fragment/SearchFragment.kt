package cloud.my_task.mytaskandroid.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.SearchEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView

import cloud.my_task.mytaskandroid.R
import cloud.my_task.mytaskandroid.adapter.SearchUserListAdapter
import cloud.my_task.mytaskandroid.adapter.SliderAdapter
import cloud.my_task.mytaskandroid.data.User
import cloud.my_task.mytaskandroid.data.users.UsersViewModel
import cloud.my_task.mytaskandroid.service.ApiService
import cloud.my_task.mytaskandroid.utilities.InjectorUtils
import kotlinx.android.synthetic.main.content_dashboard.*
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchFragment : Fragment(), SearchView.OnQueryTextListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var searchView: SearchView
    private lateinit var searchText: TextView

    private lateinit var apiService: ApiService
    private lateinit var searchListAdapter: SearchUserListAdapter

    private lateinit var viewModelUsersViewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_user_list)
        searchListAdapter = SearchUserListAdapter(inflater.context)
        recyclerView.adapter = searchListAdapter
        searchListAdapter.resetSearchUsers()
        recyclerView.layoutManager = LinearLayoutManager(context)

        initializeUI()


        searchView = view.findViewById(R.id.search_view)
        searchView.queryHint = "Search among Users"
        searchView.setOnQueryTextListener(this)


        // Inflate the layout for this fragment
        return view
    }


    private fun initializeUI() {
        val factory = InjectorUtils.provideUsersViewModelFactory()
        viewModelUsersViewModel = ViewModelProviders.of(this, factory)
            .get(UsersViewModel::class.java)


        apiService = ApiService()


        GlobalScope.launch(Dispatchers.IO) {
            this.run {

                apiService.getUserList().fold({ users ->

                    viewModelUsersViewModel.clearUsers()
                    users.forEach { user ->
                        viewModelUsersViewModel.addUser(user)
                    }
                }, { error ->
                    Log.i("", "eeror")
                })

            }
        }

        viewModelUsersViewModel.getUsers().observe(this, Observer { users ->

            searchListAdapter.setSearchUser(users as List<User>)

        })
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchListAdapter.filterSearchUser(query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        searchListAdapter.filterSearchUser(newText)
        return false
    }
}
