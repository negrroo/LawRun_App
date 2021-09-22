package cyborg.kaka.lawrun.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.topjohnwu.superuser.internal.Utils
import cyborg.kaka.lawrun.BuildConfig
import cyborg.kaka.lawrun.R
import cyborg.kaka.lawrun.databinding.FragmentProfilesBinding
import cyborg.kaka.lawrun.utils.Constants.PROFILE_BALANCE
import cyborg.kaka.lawrun.utils.Constants.PROFILE_BATTERY
import cyborg.kaka.lawrun.utils.Constants.PROFILE_EXTRA_BALANCE
import cyborg.kaka.lawrun.utils.Constants.PROFILE_GAMING
import cyborg.kaka.lawrun.utils.Constants.PROFILE_PERFORMANCE
import cyborg.kaka.lawrun.utils.Constants.PROP_APPLIED_PROFILE
import cyborg.kaka.lawrun.utils.Constants.PROP_KERNEL_NAME
import cyborg.kaka.lawrun.utils.Constants.PROP_LAWRUN_SUPPORT
import cyborg.kaka.lawrun.utils.Utils.getProp
import cyborg.kaka.lawrun.utils.Utils.rootCheck
import cyborg.kaka.lawrun.utils.Utils.setProp
import kotlin.math.abs


class ProfilesFragment : Fragment() {

    lateinit var layout: FragmentProfilesBinding // ViewBinding

    // Views of OutSide Fragment (MainActivity)
    private lateinit var mainContainer: LinearLayout
    private lateinit var tabHeart: ImageView
    private lateinit var textProfiles: TextView
    private lateinit var tabColorHolder: TextView
    private lateinit var tabIndicator: GradientDrawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentProfilesBinding.inflate(inflater, container, false)

        // Ids' From Parent (MainScreen)
        mainContainer = activity!!.findViewById(R.id.mainContainer)
        tabHeart = mainContainer.findViewById(R.id.tab_heart)

        // Tabs
        textProfiles = mainContainer.findViewById(R.id.text_profiles)
        tabIndicator =
            mainContainer.findViewById<View>(R.id.tab_indicator_profiles).background as GradientDrawable
        tabColorHolder = mainContainer.findViewById(R.id.tab_color_holder)

        // OnClickListeners
        layout.cardBattery.setOnClickListener { setProfile(PROFILE_BATTERY) }
        layout.cardBalance.setOnClickListener { setProfile(PROFILE_BALANCE) }
        layout.cardEbalance.setOnClickListener { setProfile(PROFILE_EXTRA_BALANCE) }
        layout.cardPerformance.setOnClickListener { setProfile(PROFILE_PERFORMANCE) }
        layout.cardGaming.setOnClickListener { setProfile(PROFILE_GAMING) }

        // Check LawRun Support
        if (getProp(PROP_LAWRUN_SUPPORT) != "1") {
            disableProfileScreen()
        }

        // Init No Root Needed Things
        initInfoCard()

        // Show Layout
        return layout.root
    }

    // Set Info Card
    @SuppressLint("SetTextI18n")
    private fun initInfoCard() {
        // Device Name
        if (Build.MODEL.length < 20) {
            layout.tvDeviceName.text = Build.BRAND + " " + Build.MODEL
        } else {
            layout.tvDeviceName.text = Build.MODEL
        }

        // Kernel Name
        if (getProp(PROP_KERNEL_NAME) != "") {
            layout.tvKernelName.text = getProp(PROP_KERNEL_NAME)
        } else {
            layout.tvKernelName.visibility = View.GONE
            layout.tvInfoKernel.visibility = View.GONE
            layout.dividerInfo1.visibility = View.GONE
        }

        // Kernel Version
        layout.tvLkmVersion.text = BuildConfig.VERSION_NAME

        // Battery Temp
        val batteryTempUpdater = Handler(Looper.getMainLooper())
        batteryTempUpdater.postDelayed(object : Runnable {
            override fun run() {
                val intent =
                    activity!!.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                val batteryTemp = (intent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
                    .toFloat() / 10).toString()
                layout.tvBatteryTemp.text =
                    batteryTemp.substring(0, batteryTemp.indexOf(".")) + "°C"
                batteryTempUpdater.postDelayed(this, 1000)
            }
        }, 1000)

        // Battery Current
        val batteryCurrentUpdater = Handler(Looper.getMainLooper())
        batteryCurrentUpdater.postDelayed(object : Runnable {
            override fun run() {
                val manager =
                    Utils.context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                val value =
                    manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
                val status =
                    manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)

                val isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING)
                val isCharged = (status == BatteryManager.BATTERY_STATUS_FULL)
                val value2 = abs(value / 1000)

                when {
                    isCharging -> {
                        layout.tvBatteryCurrent.text =
                            "+" + value2.toString() + "mA"
                    }
                    isCharged -> {
                        layout.tvBatteryCurrent.text =
                            "FULL"
                    }
                    else -> {
                        layout.tvBatteryCurrent.text =
                            "-" + value2.toString()  + "mA"
                    }
                }

                batteryCurrentUpdater.postDelayed(this, 1000)
            }
        }, 1000)
    }

    // Reset To Default Colors & Apply Color Of Selected Theme
    private fun resetColors() {
        // Reset All Colors
        layout.cardBalance.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark))
        layout.tvBalance.setTextColor(activity!!.getColor(R.color.balance_2300ma))
        layout.cardBattery.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark))
        layout.tvBattery.setTextColor(activity!!.getColor(R.color.battery_1800ma))
        layout.cardEbalance.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark))
        layout.tvEbalance.setTextColor(activity!!.getColor(R.color.e_balance_2800ma))
        layout.cardPerformance.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark))
        layout.tvPerformance.setTextColor(activity!!.getColor(R.color.performance_3300ma))
        layout.cardGaming.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark))
        layout.tvGaming.setTextColor(activity!!.getColor(R.color.gaming_3500ma))

        // Get Current Applied Theme Color
        val profile = getProp(PROP_APPLIED_PROFILE)

        // Init Default Colors
        var themeColor = activity!!.getColor(R.color.e_balance_2800ma)
        val defaultDarkColor = activity!!.getColor(R.color.primary_dark_black)

        // Check & Apply Colors
        when (profile) {
            PROFILE_BATTERY.toString() -> {
                themeColor = activity!!.getColor(R.color.battery_1800ma)
                layout.cardBattery.setCardBackgroundColor(themeColor)
                layout.tvBattery.setTextColor(defaultDarkColor)
                tabHeart.setImageResource(R.mipmap.ic_heart_battery)
            }
            PROFILE_BALANCE.toString() -> {
                themeColor = activity!!.getColor(R.color.balance_2300ma)
                layout.cardBalance.setCardBackgroundColor(themeColor)
                layout.tvBalance.setTextColor(defaultDarkColor)
                tabHeart.setImageResource(R.mipmap.ic_heart_balance)
            }
            PROFILE_EXTRA_BALANCE.toString() -> {
                themeColor = activity!!.getColor(R.color.e_balance_2800ma)
                layout.cardEbalance.setCardBackgroundColor(themeColor)
                layout.tvEbalance.setTextColor(defaultDarkColor)
                tabHeart.setImageResource(R.mipmap.ic_heart_ebalance)
            }
            PROFILE_PERFORMANCE.toString() -> {
                themeColor = activity!!.getColor(R.color.performance_3300ma)
                layout.cardPerformance.setCardBackgroundColor(themeColor)
                layout.tvPerformance.setTextColor(defaultDarkColor)
                tabHeart.setImageResource(R.mipmap.ic_heart_performance)
            }
            PROFILE_GAMING.toString() -> {
                themeColor = activity!!.getColor(R.color.gaming_3500ma)
                layout.cardGaming.setCardBackgroundColor(themeColor)
                layout.tvGaming.setTextColor(defaultDarkColor)
                tabHeart.setImageResource(R.mipmap.ic_heart_gaming)
            }
        }

        // Apply Info Text Colors
        layout.tvDeviceName.setTextColor(themeColor)
        layout.tvKernelName.setTextColor(themeColor)
        layout.tvLkmVersion.setTextColor(themeColor)
        layout.tvBatteryTemp.setTextColor(themeColor)
        layout.tvBatteryCurrent.setTextColor(themeColor)

        // Apply Selected Tab Color
        textProfiles.setTextColor(themeColor)
        tabIndicator.setColor(themeColor)

        // Color PlaceHolder For Other Views
        tabColorHolder.setTextColor(themeColor)
    }

    // lawRun Not Available
    private fun disableProfileScreen() {
        layout.profiles.visibility = View.GONE
        layout.profilesNotSupported.visibility = View.VISIBLE

        // Profiles Link
        layout.downloadProfiles.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://lawrun-kernel.blogspot.com/search/label/Profiles/")
            )
            startActivity(intent)
        }

        // How to Install
        layout.installProfiles.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://lawrun-kernel.blogspot.com/2020/04/installation-guide.html")
            )
            startActivity(intent)
        }
    }

    // Apply Profile
    private fun setProfile(profile: Int) {
        if (!rootCheck(activity)) return
        if (getProp(PROP_APPLIED_PROFILE) != profile.toString()) {
            setProp(PROP_APPLIED_PROFILE, profile.toString())
            resetColors()
        }
    }

    // Refresh Fragment
    override fun onResume() {
        super.onResume()
        resetColors()
    }
}