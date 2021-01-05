package cyborg.kaka.lawrun.services

import android.graphics.drawable.Icon
import android.os.CountDownTimer
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ShellUtils
import cyborg.kaka.lawrun.R
import cyborg.kaka.lawrun.utils.Constants.PROFILE_BALANCE
import cyborg.kaka.lawrun.utils.Constants.PROFILE_BATTERY
import cyborg.kaka.lawrun.utils.Constants.PROFILE_EXTRA_BALANCE
import cyborg.kaka.lawrun.utils.Constants.PROFILE_GAMING
import cyborg.kaka.lawrun.utils.Constants.PROFILE_PERFORMANCE
import cyborg.kaka.lawrun.utils.Constants.PROP_APPLIED_PROFILE
import cyborg.kaka.lawrun.utils.Constants.PROP_LAWRUN_SUPPORT
import cyborg.kaka.lawrun.utils.Utils


class ProfileTile : TileService() {
    private var activeProfile: Int = 0
    private var newState: Int = 0
    private lateinit var newIcon: Icon
    private lateinit var newLabel: String

    // Countdown Timer For Applying Profile Delayed
    private var applyProfileDelayed: CountDownTimer = object : CountDownTimer(1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            if (Utils.getProp(PROP_APPLIED_PROFILE) != activeProfile.toString())
                Utils.setProp(PROP_APPLIED_PROFILE, activeProfile.toString())
        }
    }

    override fun onStartListening() {
        if (!Shell.rootAccess() || Utils.getProp(PROP_LAWRUN_SUPPORT) != "1") {
            disableTile()
            return
        }
        // Get Active Profile
        activeProfile = Utils.getProp(PROP_APPLIED_PROFILE).toInt()
        setLabelIcon()
    }

    override fun onClick() {
        // Check Root And LawRun Support
        if (!Shell.rootAccess() || ShellUtils.fastCmd("getprop lawrun.support") != "1") {
            disableTile()
            return
        }

        // Apply Next Profile
        when (activeProfile) {
            PROFILE_BATTERY -> {
                activeProfile = PROFILE_BALANCE
            }
            PROFILE_EXTRA_BALANCE -> {
                activeProfile = PROFILE_PERFORMANCE
            }
            PROFILE_PERFORMANCE -> {
                activeProfile = PROFILE_GAMING
            }
            PROFILE_GAMING -> {
                activeProfile = PROFILE_BATTERY
            }
            else -> {
                activeProfile = PROFILE_EXTRA_BALANCE
            }
        }
        setLabelIcon()

        // Disable Old Apply Task If Running
        applyProfileDelayed.cancel()

        // Apply Profile After Waiting For Other Input
        applyProfileDelayed.start()
    }

    private fun disableTile() {
        newLabel = "Unavailable"
        newIcon = Icon.createWithResource(applicationContext, R.mipmap.ic_balance_tile)
        newState = Tile.STATE_UNAVAILABLE
        updateTile()
    }

    // Set Icon & Label
    private fun setLabelIcon() {
        when (activeProfile) {
            PROFILE_BALANCE -> {
                newLabel = resources.getString(R.string.balance)
                newIcon = Icon.createWithResource(applicationContext, R.mipmap.ic_balance_tile)
            }
            PROFILE_PERFORMANCE -> {
                newLabel = resources.getString(R.string.performance)
                newIcon = Icon.createWithResource(applicationContext, R.mipmap.ic_performance_tile)
            }
            PROFILE_GAMING -> {
                newLabel = resources.getString(R.string.gaming)
                newIcon = Icon.createWithResource(applicationContext, R.mipmap.ic_gaming_tile)
            }
            PROFILE_BATTERY -> {
                newLabel = resources.getString(R.string.battery)
                newIcon = Icon.createWithResource(applicationContext, R.mipmap.ic_battery_tile)
            }
            else -> {
                activeProfile = PROFILE_EXTRA_BALANCE
                newLabel = resources.getString(R.string.extra_balance)
                newIcon = Icon.createWithResource(applicationContext, R.mipmap.ic_ebalance_tile)
            }
        }
        newState = Tile.STATE_ACTIVE
        updateTile()
    }

    // Update Tile UI
    private fun updateTile() {
        val tile = this.qsTile

        // Set The Tile Icon And Label
        tile.label = newLabel
        tile.icon = newIcon
        tile.state = newState
        tile.updateTile()
    }
}