package cyborg.kaka.lawrun.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.topjohnwu.superuser.ShellUtils
import com.topjohnwu.superuser.io.SuFile
import cyborg.kaka.lawrun.R
import cyborg.kaka.lawrun.databinding.FragmentKernelBinding
import cyborg.kaka.lawrun.utils.Constants
import cyborg.kaka.lawrun.utils.Constants.PROP_LAWRUN_SUPPORT
import cyborg.kaka.lawrun.utils.Utils
import cyborg.kaka.lawrun.utils.Utils.getProp


class KernelFragment : Fragment() {

    private lateinit var layout: FragmentKernelBinding // ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentKernelBinding.inflate(inflater, container, false)

        // Charging Amps Card OnClick
        layout.switchDt2w.setOnCheckedChangeListener { _, isChecked ->
            if (Utils.rootCheck(activity)) {
                if (isChecked) {
                    ShellUtils.fastCmd("echo 1 > " + Constants.DT2W_FILE)
                } else {
                    ShellUtils.fastCmd("echo 0 > " + Constants.DT2W_FILE)
                }
            }
            getDT2W()
        }

        // Zram Tweaks Switch Listener
        layout.switchZram.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_ZRAM_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_ZRAM_TWEAKS, "0")
            getZramTweaks()
        }

        // Touch Tweaks Switch Listener
        layout.switchTouchTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_TOUCH_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_TOUCH_TWEAKS, "0")
            getTouchTweaks()
        }

        // Power Tweaks Switch Listener
        layout.switchPowerTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_POWER_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_POWER_TWEAKS, "0")
            getPowerTweaks()
        }

        // System Tweaks Switch Listener
        layout.switchSystemTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_SYSTEM_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_SYSTEM_TWEAKS, "0")
            getSystemTweaks()
        }

        // I/O Tweaks Switch Listener
        layout.switchIoTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_IO_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_IO_TWEAKS, "0")
            getIoTweaks()
        }

        // Ram Tweaks Switch Listener
        layout.switchRamTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_RAM_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_RAM_TWEAKS, "0")
            getRamTweaks()
        }

        // Check LawRun Support
        if (getProp(PROP_LAWRUN_SUPPORT) != "1") {
            disableProfileScreen()
        }

        return layout.root
    }

    // Reset Colors
    private fun resetColors() {
        // GetTheme Color
        val mainContainer = activity!!.findViewById<LinearLayout>(R.id.mainContainer)
        val colorHolder = mainContainer.findViewById<TextView>(R.id.tab_color_holder)
        val themeColor = colorHolder.textColors.defaultColor

        // Card Default Colors
        layout.tvDt2w.setTextColor(themeColor)
        layout.tvZram.setTextColor(themeColor)
        layout.tvTouchTweaks.setTextColor(themeColor)
        layout.tvPowerTweaks.setTextColor(themeColor)
        layout.tvSystemTweaks.setTextColor(themeColor)
        layout.tvIoTweaks.setTextColor(themeColor)
        layout.tvRamTweaks.setTextColor(themeColor)
    }


    // Initiate DT2W Switch
    private fun getDT2W() {
        if (!SuFile.open(Constants.DT2W_FILE).exists()) {
            layout.cardDt2w.visibility = View.GONE
            return
        }
        if (ShellUtils.fastCmd("cat " + Constants.DT2W_FILE) == "1") {
            layout.switchDt2w.isChecked = true
            return
        }
        layout.switchDt2w.isChecked = false
    }

    // Zram Tweaks Switch State
    private fun getZramTweaks() {
        layout.switchZram.isChecked = Utils.getProp(Constants.PROP_ZRAM_TWEAKS) == "1"
    }

    // Touch Tweaks Switch State
    private fun getTouchTweaks() {
        layout.switchTouchTweaks.isChecked = Utils.getProp(Constants.PROP_TOUCH_TWEAKS) == "1"
    }

    // Power Tweaks Switch State
    private fun getPowerTweaks() {
        layout.switchPowerTweaks.isChecked = Utils.getProp(Constants.PROP_POWER_TWEAKS) == "1"
    }

    // System Tweaks Switch State
    private fun getSystemTweaks() {
        layout.switchSystemTweaks.isChecked = Utils.getProp(Constants.PROP_SYSTEM_TWEAKS) == "1"
    }

    // I/O Tweaks Switch State
    private fun getIoTweaks() {
        layout.switchIoTweaks.isChecked = Utils.getProp(Constants.PROP_IO_TWEAKS) == "1"
    }

    // Ram Tweaks Switch State
    private fun getRamTweaks() {
        layout.switchRamTweaks.isChecked = Utils.getProp(Constants.PROP_RAM_TWEAKS) == "1"
    }

    // lawRun Not Available
    private fun disableProfileScreen() {
        layout.switchZram.visibility = View.GONE
        layout.switchTouchTweaks.visibility = View.GONE
        layout.switchPowerTweaks.visibility = View.GONE
        layout.switchSystemTweaks.visibility = View.GONE
        layout.switchIoTweaks.visibility = View.GONE
        layout.switchRamTweaks.visibility = View.GONE
    }

    // Refresh Fragment
    override fun onStart() {
        super.onStart()
        resetColors()
        getDT2W()
        getZramTweaks()
        getTouchTweaks()
        getPowerTweaks()
        getSystemTweaks()
        getIoTweaks()
        getRamTweaks()
    }
}
