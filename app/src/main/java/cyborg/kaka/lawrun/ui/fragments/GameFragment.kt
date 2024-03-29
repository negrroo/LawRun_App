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
import cyborg.kaka.lawrun.databinding.FragmentGameBinding
import cyborg.kaka.lawrun.utils.Constants
import cyborg.kaka.lawrun.utils.Constants.PROP_HDR_FILE_PATH
import cyborg.kaka.lawrun.utils.Constants.PUBGM_HDR_FILE
import cyborg.kaka.lawrun.utils.Constants.PUBGM_IN_HDR_FILE
import cyborg.kaka.lawrun.utils.Constants.PUBGM_KR_HDR_FILE
import cyborg.kaka.lawrun.utils.Utils


class GameFragment : Fragment() {

    private lateinit var layout: FragmentGameBinding // ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentGameBinding.inflate(inflater, container, false)

        // HDR Extreme Switch Listener
        layout.switchHdrExtreme.setOnCheckedChangeListener { _, isChecked ->
            if (Utils.rootCheck(activity)) {
                if (isChecked) {
                    if (SuFile.open("/storage/emulated/0/Android/data/com.tencent.ig/").exists()) {
                        ShellUtils.fastCmd("cp -a ${Utils.getProp(PROP_HDR_FILE_PATH)} $PUBGM_HDR_FILE") // PUBGM HDR Extreme On
                    }
                    if (SuFile.open("/storage/emulated/0/Android/data/com.pubg.krmobile/")
                            .exists()
                    ) {
                        ShellUtils.fastCmd("cp -a ${Utils.getProp(PROP_HDR_FILE_PATH)} $PUBGM_KR_HDR_FILE") // PUBGM Korean HDR Extreme On
                    }
                    if (SuFile.open("/storage/emulated/0/Android/data/com.pubg.imobile/")
                            .exists()
                    ) {
                        ShellUtils.fastCmd("cp -a ${Utils.getProp(PROP_HDR_FILE_PATH)} $PUBGM_IN_HDR_FILE") // PUBGM Indian HDR Extreme On
                    }
                } else {
                    ShellUtils.fastCmd("rm $PUBGM_HDR_FILE") // PUBGM HDR Extreme Off
                    ShellUtils.fastCmd("rm $PUBGM_KR_HDR_FILE") // PUBGM Korean HDR Extreme Off
                    ShellUtils.fastCmd("rm $PUBGM_IN_HDR_FILE") // PUBGM Korean HDR Extreme Off
                }
            }
            getHDRExtreme() // Update HDR Extreme Switch State
        }

        // Gpu Tweaks Switch Listener
        layout.switchGpuTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_GPU_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_GPU_TWEAKS, "0")
            getGpuTweaks()
        }

        // Thermal Tweaks Switch Listener
        layout.switchThermalTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_THERMAL_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_THERMAL_TWEAKS, "0")
            getThermalTweaks()
        }

        // K Tweaks Switch Listener
        layout.switchKTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(Constants.PROP_K_TWEAKS, "1")
            else Utils.setProp(Constants.PROP_K_TWEAKS, "0")
            getKTweaks()
        }

        // Check LawRun Support
        if (Utils.getProp(Constants.PROP_LAWRUN_SUPPORT) != "1") {
            disableProfileScreen()
        }

        // Show Layout
        return layout.root
    }

    // Reset Colors
    private fun resetColors() {
        // GetTheme Color
        val mainContainer: LinearLayout = activity!!.findViewById(R.id.mainContainer)
        val colorHolder = mainContainer.findViewById<TextView>(R.id.tab_color_holder)
        val themeColor =
            colorHolder.textColors.defaultColor // Applied Profile Color From Main Activity

        // Change Card Title Colors To Theme
        layout.tvHdrExtreme.setTextColor(themeColor) // HDR Extreme Card Title Color
        layout.tvGpuTweaks.setTextColor(themeColor) // Gpu Tweaks Card Title Color
        layout.tvThermalTweaks.setTextColor(themeColor) // Thermal Tweaks Card Title Color
        layout.tvKTweaks.setTextColor(themeColor) // K Tweaks Card Title Color
    }

    // HDR Extreme Switch State
    private fun getHDRExtreme() {
        if ((!SuFile.open("/storage/emulated/0/Android/data/com.tencent.ig/").exists()
                    && !SuFile.open("/storage/emulated/0/Android/data/com.pubg.krmobile/").exists() && !SuFile.open("/storage/emulated/0/Android/data/com.pubg.imobile/").exists())
            || Utils.getProp(PROP_HDR_FILE_PATH) == ""
            || !SuFile.open(Utils.getProp(PROP_HDR_FILE_PATH)).exists()
        ) {
            layout.cardHdrExtreme.visibility = View.GONE // HDR Extreme Card Hide
            return
        }
        if (SuFile.open(PUBGM_HDR_FILE).exists() || SuFile.open(PUBGM_KR_HDR_FILE).exists() || SuFile.open(PUBGM_IN_HDR_FILE).exists()) {
            layout.switchHdrExtreme.isChecked = true // HDR Extreme Switch State On
            return
        }
        layout.switchHdrExtreme.isChecked = false // HDR Extreme Switch State Off
    }

    // Gpu Tweaks Switch State
    private fun getGpuTweaks() {
        layout.switchGpuTweaks.isChecked = Utils.getProp(Constants.PROP_GPU_TWEAKS) == "1"
    }

    // Thermal Tweaks Switch State
    private fun getThermalTweaks() {
        layout.switchThermalTweaks.isChecked = Utils.getProp(Constants.PROP_THERMAL_TWEAKS) == "1"
    }

    // K Tweaks Switch State
    private fun getKTweaks() {
        layout.switchKTweaks.isChecked = Utils.getProp(Constants.PROP_K_TWEAKS) == "1"
    }

    // lawRun Not Available
    private fun disableProfileScreen() {
        layout.switchHdrExtreme.visibility = View.GONE
        layout.switchGpuTweaks.visibility = View.GONE
        layout.switchThermalTweaks.visibility = View.GONE
        layout.switchKTweaks.visibility = View.GONE
    }

    // Refresh Fragment
    override fun onStart() {
        super.onStart()
        resetColors() // Apply Current Profile Theme Color
        getHDRExtreme() // Update HDR Extreme Switch State
        getGpuTweaks() // Update Gpu Tweaks Switch State
        getThermalTweaks() // Update Thermal Tweaks Switch State
        getKTweaks() // Update K Tweaks Switch State
    }
}
