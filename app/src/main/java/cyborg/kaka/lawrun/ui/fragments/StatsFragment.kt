package cyborg.kaka.lawrun.ui.fragments

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cyborg.kaka.lawrun.R
import cyborg.kaka.lawrun.databinding.FragmentStatsBinding
import cyborg.kaka.lawrun.services.FPS
import cyborg.kaka.lawrun.services.FPSTile


class StatsFragment : Fragment() {

    private lateinit var layout: FragmentStatsBinding // ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentStatsBinding.inflate(inflater, container, false)

        // FPS On/Off Switch
        layout.switchFpsMeter.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!Settings.canDrawOverlays(activity)) {
                    val packageName = activity!!.packageName
                    val intent = Intent(
                        "android.settings.action.MANAGE_OVERLAY_PERMISSION",
                        Uri.parse("package:$packageName")
                    )
                    activity!!.startActivityForResult(intent, 1)
                } else {
                    activity!!.startForegroundService(Intent(activity, FPS::class.java))
                }
            } else {
                activity!!.stopService(Intent(activity, FPS::class.java))
            }

            // Get FPS Running
            getFPSMeter()
        }

        // Battery Monitor Click
        layout.switchBatteryMonitor.setOnCheckedChangeListener { _, _ ->
            Toast.makeText(
                activity,
                "Currently in beta - Working day/night to add features (Click heart icon on top right corner)",
                Toast.LENGTH_LONG
            ).show()
            layout.switchBatteryMonitor.isChecked = false
        }

        return layout.root
    }

    // Reset Colors
    private fun resetColors() {
        // GetTheme Color
        val mainContainer: LinearLayout = activity!!.findViewById(R.id.mainContainer)
        val colorHolder = mainContainer.findViewById<TextView>(R.id.tab_color_holder)
        val themeColor = colorHolder.textColors.defaultColor
        layout.tvFpsMeterTitle.setTextColor(themeColor)
        layout.tvBatteryMonitor.setTextColor(themeColor)
    }

    // Get FPS Running
    private fun getFPSMeter() {
        layout.switchFpsMeter.isChecked = isFPSServiceAlive()
        FPSTile.Service.isRunning = isFPSServiceAlive()
    }

    // Check FPS Service State
    private fun isFPSServiceAlive(): Boolean {
        val manager = activity!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (FPS::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    // Refresh Fragment
    override fun onStart() {
        super.onStart()
        resetColors()
        getFPSMeter()
    }

}