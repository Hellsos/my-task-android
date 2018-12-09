package cloud.my_task.mytaskandroid.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import cloud.my_task.mytaskandroid.R
import cloud.my_task.mytaskandroid.data.Task
import cloud.my_task.mytaskandroid.service.ApiService
import kotlinx.android.synthetic.main.fragment_fragment_new_task.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import cloud.my_task.mytaskandroid.R.drawable.calendar
import cloud.my_task.mytaskandroid.service.SharedPreferencesService
import java.sql.Timestamp


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [fragment_new_task.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [fragment_new_task.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentNewTask : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var onDateSetListener: DatePickerDialog.OnDateSetListener

    private lateinit var sharedPreferencesService: SharedPreferencesService

    private lateinit var apiService: ApiService

    private lateinit var task: Task

    private lateinit var taskName: TextView
    private lateinit var taskDescription: TextView
    private lateinit var calendar: Calendar


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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fragment_new_task, container, false)

        calendar = Calendar.getInstance()
        calendar.time = Date()


        val selectDate: Button = view.findViewById(R.id.select_date)

        val date =
            calendar.get(Calendar.YEAR).toString() + "-" + calendar.get(Calendar.MONTH).toString() + "-" + calendar.get(
                Calendar.DAY_OF_MONTH
            ).toString()
        selectDate.text = date

        selectDate.setOnClickListener() {

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog: DatePickerDialog =
                DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    val date = year.toString() + "-" + month.toString() + "-" + dayOfMonth.toString()
                    selectDate.text = date

                }, year, month, day)
            dialog.show()

        }

        val selectTime: Button = view.findViewById(R.id.select_time)


        selectTime.text = SimpleDateFormat("HH:mm").format(calendar.time)

        selectTime.setOnClickListener() {
            val timeSetListener = TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                selectTime.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }
            TimePickerDialog(
                view.context,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
                .show()
        }

        taskName = view.findViewById(R.id.new_task_name)
        taskDescription = view.findViewById(R.id.new_task_description)

        this.apiService = ApiService()


        sharedPreferencesService = SharedPreferencesService(view.context)

        val createNewTask: Button = view.findViewById(R.id.create_new_task)
        createNewTask.setOnClickListener() {

            GlobalScope.launch(Dispatchers.IO) {
                this.run {

                    val time: Long = calendar.time.time / 1000

                    apiService.createTask(
                        Task(
                            null,
                            taskName.text.toString(),
                            taskDescription.text.toString(),
                            time,
                            49.8337.toFloat(),
                            18.1636.toFloat()
                        )
                    )
                        .fold({ task: Task ->
                            goToDashboard()
                        }, { error ->
                            Log.i("", "eeror");
                        })


                }
            }
        }

        val manager = childFragmentManager
        val fr = manager.beginTransaction()

        fr.replace(R.id.container_frame_back, MapFragment(), "map")
        fr.commitNowAllowingStateLoss()


//        val aboutFragment: AboutFragment = AboutFragment.newInstance("a", "b")
//        val fragmentManager: FragmentManager = supportFragmentManager;
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.setCustomAnimations(
//            R.anim.enter_from_right,
//            R.anim.exit_to_right,
//            R.anim.enter_from_right,
//            R.anim.exit_to_right
//        )
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.add(R.id.frame_container, aboutFragment, "About").commit()


        return view
    }

    private fun goToDashboard() {

        sharedPreferencesService.saveCurrentState("Dashboard")
        val dashboardFragment: DashboardFragment = DashboardFragment.newInstance("a", "b")
        val fragmentManager: FragmentManager = getFragmentManager() as FragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_left
        )
        fragmentTransaction.add(R.id.frame_container, dashboardFragment, "Dashboard").commit()
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
         * @return A new instance of fragment fragment_new_task.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentNewTask().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
