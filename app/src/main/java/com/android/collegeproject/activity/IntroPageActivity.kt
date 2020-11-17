package com.android.collegeproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.collegeproject.R
import com.android.collegeproject.helper.Constants
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_intro_page.*
import kotlinx.android.synthetic.main.intro_slider.view.*

class IntroPageActivity : AppCompatActivity() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_page)

        val from = intent.getStringExtra("from")
        if(from != "splash") {
            activity_intro_page_nextTutorialBtn.text = "CLOSE"
        }

        mSharedPreferences = this.getSharedPreferences(
            Constants().USER_INFO,
            Context.MODE_PRIVATE
        )

        val profilePagerAdapter = ViewPagerAdapter(this)
        val viewPager: ViewPager = findViewById(R.id.activity_intro_page_sliderView)
        viewPager.adapter = profilePagerAdapter
        val tabs: TabLayout = findViewById(R.id.activity_intro_page_sliderTabs)
        tabs.setupWithViewPager(viewPager, true)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if(from == "splash") {
                    if(position == 6) {
                        activity_intro_page_nextTutorialBtn.text = "FINISH"
                    } else {
                        activity_intro_page_nextTutorialBtn.text = "SKIP"
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        activity_intro_page_nextTutorialBtn.setOnClickListener {
            if(from == "splash") {
                mSharedPreferences.edit().putString(
                    Constants().USER_ISFIRSTLOGIN,
                    "NotFirst"
                ).apply()
                val intent = Intent(this, PermissionActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                finish()
            }
        }
    }

    class ViewPagerAdapter(val context : Context) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view.equals(`object`)
        }

        override fun getCount(): Int {
            return 7
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.intro_slider, container, false)
            container.addView(view, 0)

            when (position) {
                0 -> {
                    view!!.intro_slider_pageHeading.text = "Welcome to HearUs!"
                    view!!.intro_slider_pageImage.setImageResource(R.drawable.img_welcome)
                    view!!.intro_slider_pageDescription.text = "We are here to help you, perform your day-to-day activities more independently."
                }
                1 -> {
                    view!!.intro_slider_pageHeading.text = "Path hurdle"
                    view!!.intro_slider_pageImage.setImageResource(R.drawable.img_path_hurdle)
                    view!!.intro_slider_pageDescription.text = "Identify the objects by clicking a picture and hear the app speak the identification back to you."
                }
                2 -> {
                    view!!.intro_slider_pageHeading.text = "Barcode"
                    view!!.intro_slider_pageImage.setImageResource(R.drawable.img_barcode)
                    view!!.intro_slider_pageDescription.text = "Superfast Barcode reader available to identify various details of a product, in your hand."
                }
                3 -> {
                    view!!.intro_slider_pageHeading.text = "Weather"
                    view!!.intro_slider_pageImage.setImageResource(R.drawable.img_weather)
                    view!!.intro_slider_pageDescription.text = "Describes the environment by giving you updates of weather, like current temperature, humidity, and more!"
                }
                4 -> {
                    view!!.intro_slider_pageHeading.text = "News"
                    view!!.intro_slider_pageImage.setImageResource(R.drawable.img_news)
                    view!!.intro_slider_pageDescription.text = "Stay updated with the top headlines of your place, and more!"
                }
                5 -> {
                    view!!.intro_slider_pageHeading.text = "Text Recognition"
                    view!!.intro_slider_pageImage.setImageResource(R.drawable.img_text_recognition)
                    view!!.intro_slider_pageDescription.text = "Accessible reading tool with an advanced text-to-speech feature. Experience perfectly synchronized text & audio."
                }
                else -> {
                    view!!.intro_slider_pageHeading.text = "Keywords"
                    view!!.intro_slider_pageImage.setImageResource(R.drawable.img_keywords)
                    view!!.intro_slider_pageDescription.text = "All you have to do is to double tap your screen and speak some fixed keywords to access all the features. The keywords can be known by saying \"HELP\"."
                }
            }

            return view
        }
    }
}