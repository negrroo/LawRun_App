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
import cyborg.kaka.lawrun.utils.Utils


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
            Utils.needRebootToast(activity)
            getZramTweaks()
        }

        // Touch Tweaks Switch Listener
        layout.switchTouchTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_TOUCH_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_TOUCH_TWEAKS, "0")
            Utils.needRebootToast(activity)
            getTouchTweaks()
        }

        // Touch Latest Switch Listener
        layout.switchTouchLatest.setOnCheckedChangeListener { _, isChecked ->
            if (Utils.rootCheck(activity)) {
                if (isChecked) {
                    ShellUtils.fastCmd("echo 1 > " + Constants.TOUCH_LATEST_FILE)
                } else {
                    ShellUtils.fastCmd("echo 0 > " + Constants.TOUCH_LATEST_FILE)
                }
            }
            getTouchLatest()
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
        layout.tvTouchLatest.setTextColor(themeColor)
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

    // Touch Latest Switch State
    private fun getTouchLatest() {
        if (!SuFile.open(Constants.TOUCH_LATEST_FILE).exists()) {
            layout.cardTouchLatest.visibility = View.GONE
            return
        }
        if (ShellUtils.fastCmd("cat " + Constants.TOUCH_LATEST_FILE) == "1") {
            layout.switchTouchLatest.isChecked = true
            return
        }
        layout.switchTouchLatest.isChecked = false
    }

    // Refresh Fragment
    override fun onStart() {
        super.onStart()
        resetColors()
        getDT2W()
        getZramTweaks()
        getTouchTweaks()
    }
}
