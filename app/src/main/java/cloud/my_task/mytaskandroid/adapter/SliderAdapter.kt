package cloud.my_task.mytaskandroid.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cloud.my_task.mytaskandroid.R
import cloud.my_task.mytaskandroid.structure.SlideShowItem

class SliderAdapter : PagerAdapter {


    private var context: Context
    private lateinit var layoutInflater: LayoutInflater

    private var slideShowList: ArrayList<SlideShowItem> = arrayListOf()

    constructor(context: Context) : super() {
        this.context = context

        this.slideShowList.add(
            SlideShowItem(
                R.drawable.down,
                "Create Task",
                "Create task and watch your progress."
            )
        )
        this.slideShowList.add(
            SlideShowItem(
                R.drawable.down2,
                "Set up Notifications",
                "Set up notifications via Email, Phone and keep yourself updated."
            )
        )
        this.slideShowList.add(
            SlideShowItem(
                R.drawable.share,
                "Share with your friends",
                "Add people to your friend list and share tasks with them."
            )
        )
    }


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return this.slideShowList.count()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        this.layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slide_layout, container, false)

        val slideImageView: ImageView = view.findViewById(R.id.slide_image)
        val slideHeading: TextView = view.findViewById(R.id.slide_heading)
        val slideText: TextView = view.findViewById(R.id.slide_text)

        slideImageView.setImageResource(this.slideShowList.get(position).image)
        slideHeading.setText(this.slideShowList.get(position).headline)
        slideText.setText(this.slideShowList.get(position).text)

        container.addView(view)

        return view


    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView((view as View))
    }
}