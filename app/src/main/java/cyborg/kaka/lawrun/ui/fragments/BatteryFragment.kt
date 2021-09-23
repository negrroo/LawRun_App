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
import cyborg.kaka.lawrun.databinding.FragmentBatteryBinding
import cyborg.kaka.lawrun.utils.Constants
import cyborg.kaka.lawrun.utils.Constants.BATTERY_THERMAL_COOL_FILE
import cyborg.kaka.lawrun.utils.Constants.BATTERY_THERMAL_WARM_FILE
import cyborg.kaka.lawrun.utils.Constants.CHARGING_MAX_FILE
import cyborg.kaka.lawrun.utils.Constants.PROP_BATTERY_TWEAKS
import cyborg.kaka.lawrun.utils.Utils


class BatteryFragment : Fragment() {

    private lateinit var layout: FragmentBatteryBinding // ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentBatteryBinding.inflate(inflater, container, false)

        // Charging Amps Card OnClick
        layout.card1200.setOnClickListener { setChargingSpeed(1200) }
        layout.card1800.setOnClickListener { setChargingSpeed(1800) }
        layout.card2300.setOnClickListener { setChargingSpeed(2300) }
        layout.card2800.setOnClickListener { setChargingSpeed(2800) }
        layout.card3000.setOnClickListener { setChargingSpeed(3000) }
        layout.card3300.setOnClickListener { setChargingSpeed(3300) }
        layout.card3500.setOnClickListener { setChargingSpeed(3500) }

        // Battery Thermal Switch Listener
        layout.switchBatteryThermal.setOnCheckedChangeListener { _, isChecked ->
            if (Utils.rootCheck(activity)) {
                if (isChecked) {
                    ShellUtils.fastCmd("echo 450 > $BATTERY_THERMAL_COOL_FILE")
                    ShellUtils.fastCmd("echo 490 > $BATTERY_THERMAL_WARM_FILE")
                    return@setOnCheckedChangeListener
                }
                ShellUtils.fastCmd("echo 420 > $BATTERY_THERMAL_COOL_FILE")
                ShellUtils.fastCmd("echo 450 > $BATTERY_THERMAL_WARM_FILE")
            }
            getBatteryThermal()
        }

        // Battery Tweaks Switch Listener
        layout.switchBatteryTweaks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Utils.setProp(PROP_BATTERY_TWEAKS,"1")
            else Utils.setProp(PROP_BATTERY_TWEAKS,"0")
            getBatteryTweaks()
        }

        // Check LawRun Support
        if (Utils.getProp(Constants.PROP_LAWRUN_SUPPORT) != "1") {
            disableProfileScreen()
        }

        return layout.root
    }

    private fun setChargingSpeed(amps: Int) {
        if (!Utils.rootCheck(activity)) return
        ShellUtils.fastCmd("chmod 755 $CHARGING_MAX_FILE")
        ShellUtils.fastCmd("echo " + amps + "000 > " + CHARGING_MAX_FILE)
        ShellUtils.fastCmd("chmod 444 $CHARGING_MAX_FILE")

        // Update Views
        resetColors()
        getAppliedAmps()
    }

    // Reset Colors
    private fun resetColors() {
        // Card Default Colors
        layout.card1200.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark_black))
        layout.card1800.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark_black))
        layout.card2300.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark_black))
        layout.card2800.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark_black))
        layout.card3000.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark_black))
        layout.card3300.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark_black))
        layout.card3500.setCardBackgroundColor(activity!!.getColor(R.color.primary_dark_black))
        layout.tv1200.setTextColor(activity!!.getColor(R.color.battery_1200ma))
        layout.tv1800.setTextColor(activity!!.getColor(R.color.battery_1800ma))
        layout.tv2300.setTextColor(activity!!.getColor(R.color.balance_2300ma))
        layout.tv2800.setTextColor(activity!!.getColor(R.color.e_balance_2800ma))
        layout.tv3000.setTextColor(activity!!.getColor(R.color.charge_3000ma))
        layout.tv3300.setTextColor(activity!!.getColor(R.color.performance_3300ma))
        layout.tv3500.setTextColor(activity!!.getColor(R.color.gaming_3500ma))

        // GetTheme Color
        val mainContainer = activity!!.findViewById<LinearLayout>(R.id.mainContainer)
        val colorHolder = mainContainer.findViewById<TextView>(R.id.tab_color_holder)
        val themeColor = colorHolder.textColors.defaultColor

        // Change Card Title Colors To Theme
        layout.chargingSpeed.setTextColor(themeColor)
        layout.tvBatteryThermalRemover.setTextColor(themeColor)
        layout.tvBatteryTweaks.setTextColor(themeColor)
    }


    // Current Amps Card
    private fun getAppliedAmps() {
        if (!SuFile.open(CHARGING_MAX_FILE).exists() || ShellUtils.fastCmd("cat $CHARGING_MAX_FILE")
                .toInt() < 1200000
        ) {
            layout.cardChargingSpeed.visibility = View.GONE
            return
        }
        when (ShellUtils.fastCmd("cat $CHARGING_MAX_FILE")) {
            "1200000" -> {
                layout.card1200.setCardBackgroundColor(activity!!.getColor(R.color.battery_1200ma))
                layout.tv1200.setTextColor(activity!!.getColor(R.color.primary_dark))
            }
            "1800000" -> {
                layout.card1800.setCardBackgroundColor(activity!!.getColor(R.color.battery_1800ma))
                layout.tv1800.setTextColor(activity!!.getColor(R.color.primary_dark))
            }
            "2300000" -> {
                layout.card2300.setCardBackgroundColor(activity!!.getColor(R.color.balance_2300ma))
                layout.tv2300.setTextColor(activity!!.getColor(R.color.primary_dark))
            }
            "2800000" -> {
                layout.card2800.setCardBackgroundColor(activity!!.getColor(R.color.e_balance_2800ma))
                layout.tv2800.setTextColor(activity!!.getColor(R.color.primary_dark))
            }
            "3000000" -> {
                layout.card3000.setCardBackgroundColor(activity!!.getColor(R.color.charge_3000ma))
                layout.tv3000.setTextColor(activity!!.getColor(R.color.primary_dark))
            }
            "3300000" -> {
                layout.card3300.setCardBackgroundColor(activity!!.getColor(R.color.performance_3300ma))
                layout.tv3300.setTextColor(activity!!.getColor(R.color.primary_dark))
            }
            "3500000" -> {
                layout.card3500.setCardBackgroundColor(activity!!.getColor(R.color.gaming_3500ma))
                layout.tv3500.setTextColor(activity!!.getColor(R.color.primary_dark))
            }
        }
    }

    // Battery Thermal Switch State
    private fun getBatteryThermal() {
        if (!SuFile.open(BATTERY_THERMAL_COOL_FILE).exists() || !SuFile.open(
                BATTERY_THERMAL_WARM_FILE
            ).exists()
        ) {
            layout.cardBatteryThermal.visibility = View.GONE
            return
        }
        if (ShellUtils.fastCmd("cat $BATTERY_THERMAL_COOL_FILE")
                .toInt() > 420 || ShellUtils.fastCmd("cat $BATTERY_THERMAL_WARM_FILE").toInt() > 450
        ) {
            layout.switchBatteryThermal.isChecked = true
        }
    }


    // Battery Tweaks Switch State
    private fun getBatteryTweaks() {
        layout.switchBatteryTweaks.isChecked = Utils.getProp(PROP_BATTERY_TWEAKS) == "1"
    }

    // lawRun Not Available
    private fun disableProfileScreen() {
        layout.switchBatteryTweaks.visibility = View.GONE
    }

    // Refresh Fragment
    override fun onStart() {
        super.onStart()
        resetColors()
        getAppliedAmps()
        getBatteryThermal()
        getBatteryTweaks()
    }
}
