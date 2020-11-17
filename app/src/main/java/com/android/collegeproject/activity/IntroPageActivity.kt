package com.android.collegeproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
                    if(position == 5) {
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
            return 6
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.intro_slider, container, false)
            container.addView(view, 0)
            Log.d("myCHECK", position.toString())
            return view
        }
    }
}