package cloud.my_task.mytaskandroid.adapter

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cloud.my_task.mytaskandroid.R
import cloud.my_task.mytaskandroid.data.Task
import cloud.my_task.mytaskandroid.data.Tasks.TasksViewModel
import cloud.my_task.mytaskandroid.data.User
import cloud.my_task.mytaskandroid.service.ApiService
import cloud.my_task.mytaskandroid.utilities.InjectorUtils
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

class SliderDashboardAdapter : PagerAdapter {


    private var context: Context
    private lateinit var layoutInflater: LayoutInflater

    private var slideShowList: ArrayList<Int> = arrayListOf()

    private lateinit var taskListAdapter: TaskListAdapter

    private lateinit var apiService: ApiService

    constructor(context: Context) : super() {
        this.context = context

        this.slideShowList.add(R.layout.slide_items_empty_layout)
        this.slideShowList.add(R.layout.slide_items_layout)

        apiService = ApiService()

    }


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return this.slideShowList.count()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        this.layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(slideShowList[position], container, false)

        if (slideShowList[position] == R.layout.slide_items_layout) {

            val factory = InjectorUtils.provideTasksViewModelFactory()
            val viewModelTasksViewModel = ViewModelProviders.of(view.context as FragmentActivity, factory)
                .get(TasksViewModel::class.java)

            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_task_list)
            taskListAdapter = TaskListAdapter(view.context)
            recyclerView.adapter = taskListAdapter
            taskListAdapter.resetTasks()
            recyclerView.layoutManager = LinearLayoutManager(view.context)


            GlobalScope.launch(Dispatchers.IO) {
                this.run {

                    apiService.getTaskList().fold({ tasks ->

                        viewModelTasksViewModel.clearTasks()
                        tasks.forEach { task ->
                            viewModelTasksViewModel.addTask(task)
                        }
                    }, { error ->
                        Log.i("", "eeror")
                    })

                }
            }

            viewModelTasksViewModel.getTasks().observe(view.context as FragmentActivity, Observer { tasks ->

                taskListAdapter.setTasks(tasks as List<Task>)

            })
        }

        container.addView(view)

        return view


    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView((view as View))
    }
}