package cloud.my_task.mytaskandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle;
import android.support.v4.view.ViewPager
import android.text.Html
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import cloud.my_task.mytaskandroid.adapter.SliderAdapter
import cloud.my_task.mytaskandroid.service.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    private lateinit var mSlideShowButtons: LinearLayout
    private lateinit var mSlideShowViewPager: ViewPager
    private lateinit var sliderAdapter: SliderAdapter
    private var mSlideShowDots: ArrayList<TextView> = arrayListOf()

    private var mCurrentPage: Int = 0

    private lateinit var mDoneButon: Button

    private lateinit var sharedPreferencesService: SharedPreferencesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        this.mSlideShowButtons = findViewById(R.id.slideShowButtons)
        this.mSlideShowViewPager = findViewById(R.id.slideViewPager)
        this.mDoneButon = findViewById(R.id.done)
        this.sliderAdapter = SliderAdapter(this)
        this.sharedPreferencesService = SharedPreferencesService(this)


        this.slideViewPager.adapter = this.sliderAdapter

        addDotsIndicator(0)

        this.slideViewPager.addOnPageChangeListener(onPageChangeListener)
        this.mDoneButon.setOnClickListener {
            callMe()
        }
    }

    private fun callMe() {
        this.sharedPreferencesService.welcomeDone = true
        startActivity(Intent(this, MainActivity::class.java).apply {

        })
        finish()
    }

    fun addDotsIndicator(position: Int) {

        this.mSlideShowButtons.removeAllViews()

        for (i in 0..(this.sliderAdapter.count - 1)) {
            this.mSlideShowDots.add(TextView(this))
            this.mSlideShowDots.get(i).setText(Html.fromHtml("&#8226;"))
            this.mSlideShowDots.get(i).setTextColor(resources.getColor(R.color.colorWelcomeDisabled))
            this.mSlideShowDots.get(i).setTextSize(35.toFloat())

            this.mSlideShowButtons.addView(mSlideShowDots.get(i))

        }

        if (this.mSlideShowDots.count() > 0) {
            this.mSlideShowDots.get(position).setTextColor(resources.getColor(R.color.colorWelcomeWhite))
        }
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(p0: Int) {

        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

        }

        override fun onPageSelected(position: Int) {
            addDotsIndicator(position)
            mCurrentPage = position

            if (mCurrentPage == (sliderAdapter.count - 1)) {
                mDoneButon.setEnabled(true)
                mDoneButon.setTextColor(resources.getColor(R.color.colorWelcomeWhite))
            } else {
                mDoneButon.setEnabled(false)
                mDoneButon.setTextColor(resources.getColor(R.color.colorWelcomeDisabled))
            }
        }

    }


}
