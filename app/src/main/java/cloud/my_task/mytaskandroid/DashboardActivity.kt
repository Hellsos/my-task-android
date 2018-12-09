package cloud.my_task.mytaskandroid

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import cloud.my_task.mytaskandroid.adapter.SearchUserListAdapter
import cloud.my_task.mytaskandroid.adapter.SliderAdapter
import cloud.my_task.mytaskandroid.adapter.SliderDashboardAdapter
import cloud.my_task.mytaskandroid.adapter.TaskListAdapter
import cloud.my_task.mytaskandroid.data.users.UsersViewModel
import cloud.my_task.mytaskandroid.database.AppDatabase
import cloud.my_task.mytaskandroid.database.UserDAO
import cloud.my_task.mytaskandroid.fragment.*
import cloud.my_task.mytaskandroid.service.SharedPreferencesService
import cloud.my_task.mytaskandroid.utilities.InjectorUtils
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AboutFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener,
    SearchFragment.OnFragmentInteractionListener, FragmentNewTask.OnFragmentInteractionListener,
    MapFragment.OnFragmentInteractionListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fragmentContainer: FrameLayout

    private lateinit var sharedPreferencesService: SharedPreferencesService

    private lateinit var viewModelUsersViewModel: UsersViewModel



    private var db: AppDatabase? = null

    private var userDAO: UserDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        initializeUI()

        fab.setOnClickListener { view ->
            openNewTaskFragment()
        }

        sharedPreferencesService = SharedPreferencesService(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fragmentContainer = findViewById(R.id.frame_container)

        try {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available");
        }

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("myTag", "Permission to record denied")
            makeRequest()
        }


        if (savedInstanceState == null) {
            openDashboardFragment(true)
            nav_view.setCheckedItem(R.id.nav_dashboard)
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )
    }

    private fun initializeUI() {
        val factory = InjectorUtils.provideUsersViewModelFactory()
        viewModelUsersViewModel = ViewModelProviders.of(this, factory)
            .get(UsersViewModel::class.java)

        viewModelUsersViewModel.getCurrentUser().observe(this, Observer { user ->
            user_name.text = user.toString()
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_home -> {
                openDashboardFragment()
                return true
            }
            R.id.action_search -> {
                opeSearchFragment()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("myTag", location.longitude.toString() + ":" + location.latitude.toString());
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun openDashboardFragment(force: Boolean = false) {
        if (!force && sharedPreferencesService.currentState == "Dashboard") {
            return
        }
        fab.show()
        nav_view.hideKeyboard()

        sharedPreferencesService.saveCurrentState("Dashboard")
        val dashboardFragment: DashboardFragment = DashboardFragment.newInstance("a", "b")
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_left
        )
        fragmentTransaction.add(R.id.frame_container, dashboardFragment, "Dashboard").commit()
    }

    fun openAboutFragment() {
        if (sharedPreferencesService.currentState == "About") {
            return
        }
        fab.show()
        nav_view.hideKeyboard()
        sharedPreferencesService.saveCurrentState("About")
        val aboutFragment: AboutFragment = AboutFragment.newInstance("a", "b")
        val fragmentManager: FragmentManager = supportFragmentManager;
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_right
        )
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.frame_container, aboutFragment, "About").commit()
    }


    fun openNewTaskFragment() {
        if (sharedPreferencesService.currentState == "New Task") {
            return
        }
        fab.hide()
        nav_view.hideKeyboard()
        sharedPreferencesService.saveCurrentState("New Task")
        val fragmentNewTask: FragmentNewTask = FragmentNewTask.newInstance("a", "b")
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom
        )


        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.frame_container, fragmentNewTask, "New Task").commit()
    }


    fun opeSearchFragment() {
        if (sharedPreferencesService.currentState == "Search") {
            return
        }
        fab.show()
        nav_view.hideKeyboard()

        sharedPreferencesService.saveCurrentState("Search")
        val searchFragment: SearchFragment = SearchFragment.newInstance("a", "b")
        val fragmentManager: FragmentManager = supportFragmentManager;
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_right
        )
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.frame_container, searchFragment, "Search").commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                openDashboardFragment()
            }
            R.id.nav_about -> {
                openAboutFragment()
            }
            R.id.nav_help -> {
                this.sharedPreferencesService.clear()
                startActivity(Intent(this, MainActivity::class.java).apply {

                })
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        onBackPressed()
    }

    private fun printText(text: String) {
        Snackbar.make(nav_view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun onRefresh() {
//        swipe.isRefreshing = false
    }
}
