package cyborg.kaka.lawrun.ui

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import cyborg.kaka.lawrun.R
import cyborg.kaka.lawrun.databinding.ActivityMainBinding
import cyborg.kaka.lawrun.ui.fragments.*
import cyborg.kaka.lawrun.ui.fragments.adapters.FragmentsPagerAdapter

class MainActivity : AppCompatActivity() {

    var themeColor = 0 // Self Explanatory
    private lateinit var layout: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)

        // Tab Title OnClick
        layout.tabProfiles.setOnClickListener { layout.mainViewPager.currentItem = 0 }
        layout.tabStats.setOnClickListener { layout.mainViewPager.currentItem = 1 }
        layout.tabKernel.setOnClickListener { layout.mainViewPager.currentItem = 2 }
        layout.tabBattery.setOnClickListener { layout.mainViewPager.currentItem = 3 }
        layout.tabGame.setOnClickListener { layout.mainViewPager.currentItem = 4 }

        // Heart Icon Click
        layout.tabHeart.setOnClickListener { layout.mainViewPager.currentItem = 5 }

        // Apply Colors To Tab Texts After Tab Change
        layout.mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                resetColors()
                when (position) {
                    0 -> {
                        layout.textProfiles.setTextColor(themeColor)
                        layout.tabIndicatorProfiles.visibility = VISIBLE
                        layout.tabIndicatorProfiles.setBackgroundColor(themeColor)
                    }
                    1 -> {
                        layout.textStats.setTextColor(themeColor)
                        layout.tabIndicatorStats.visibility = VISIBLE
                        layout.tabIndicatorStats.setBackgroundColor(themeColor)
                    }
                    2 -> {
                        layout.textKernel.setTextColor(themeColor)
                        layout.tabIndicatorKernel.visibility = VISIBLE
                        layout.tabIndicatorKernel.setBackgroundColor(themeColor)
                    }
                    3 -> {
                        layout.textBattery.setTextColor(themeColor)
                        layout.tabIndicatorBattery.visibility = VISIBLE
                        layout.tabIndicatorBattery.setBackgroundColor(themeColor)
                    }
                    4 -> {
                        layout.textGame.setTextColor(themeColor)
                        layout.tabIndicatorGame.visibility = VISIBLE
                        layout.tabIndicatorGame.setBackgroundColor(themeColor)
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        // Add Pages to Adapter
        addPages()
    }

    // Add Pages To FragmentsPager
    private fun addPages() {
        val pagerAdapter = FragmentsPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(ProfilesFragment())
        pagerAdapter.addFragment(StatsFragment())
        pagerAdapter.addFragment(KernelFragment())
        pagerAdapter.addFragment(BatteryFragment())
        pagerAdapter.addFragment(GameFragment())
        pagerAdapter.addFragment(AboutFragment())

        // Stop Refreshing Tabs
        layout.mainViewPager.offscreenPageLimit = 6

        // Set Adapter
        layout.mainViewPager.adapter = pagerAdapter
    }

    // Reset To Default Colors
    private fun resetColors() {
        themeColor = layout.tabColorHolder.textColors.defaultColor
        layout.textProfiles.setTextColor(getColor(R.color.text_dark))
        layout.tabIndicatorProfiles.visibility = INVISIBLE
        layout.textStats.setTextColor(getColor(R.color.text_dark))
        layout.tabIndicatorStats.visibility = INVISIBLE
        layout.textKernel.setTextColor(getColor(R.color.text_dark))
        layout.tabIndicatorKernel.visibility = INVISIBLE
        layout.textBattery.setTextColor(getColor(R.color.text_dark))
        layout.tabIndicatorBattery.visibility = INVISIBLE
        layout.textGame.setTextColor(getColor(R.color.text_dark))
        layout.tabIndicatorGame.visibility = INVISIBLE
    }
}
