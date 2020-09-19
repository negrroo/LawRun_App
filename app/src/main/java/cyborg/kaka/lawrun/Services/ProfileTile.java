package cyborg.kaka.lawrun.Services;


import android.graphics.drawable.Icon;
import android.os.CountDownTimer;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.ShellUtils;

import cyborg.kaka.lawrun.R;

import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_BALANCE;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_BATTERY;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_EXTRA_BALANCE;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_GAMING;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_PERFORMANCE;

public class ProfileTile extends TileService {

    int activeProfile, newState;
    Icon newIcon;
    String newLabel;

    // Countdown Timer For Applying Profile Delayed
    CountDownTimer applyProfileDelayed = new CountDownTimer(1000, 1000) {

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            if (!ShellUtils.fastCmd("getprop persist.lawrun.profile").equals(String.valueOf(activeProfile)))
                ShellUtils.fastCmd("setprop persist.lawrun.profile " + activeProfile);
        }
    };

    @Override
    public void onStartListening() {
        if (!Shell.rootAccess() || !ShellUtils.fastCmd("getprop lawrun.support").equals("1")) {
            disableTile();
            return;
        }
        // Get Active Profile
        activeProfile = Integer.parseInt(ShellUtils.fastCmd("getprop persist.lawrun.profile"));
        setLabelIcon();
    }

    @Override
    public void onClick() {
        // Check Root And LawRun Support
        if (!Shell.rootAccess() || !ShellUtils.fastCmd("getprop lawrun.support").equals("1")) {
            disableTile();
            return;
        }

        // Apply Next Profile
        if (activeProfile == PROFILE_BATTERY) {
            activeProfile = PROFILE_BALANCE;
        } else if (activeProfile == PROFILE_EXTRA_BALANCE) {
            activeProfile = PROFILE_PERFORMANCE;
        } else if (activeProfile == PROFILE_PERFORMANCE) {
            activeProfile = PROFILE_GAMING;
        } else if (activeProfile == PROFILE_GAMING) {
            activeProfile = PROFILE_BATTERY;
        } else {
            activeProfile = PROFILE_EXTRA_BALANCE;
        }

        setLabelIcon();

        // Disable Old Apply Task If Running
        applyProfileDelayed.cancel();

        // Apply Profile After Waiting For Other Input
        applyProfileDelayed.start();
    }

    private void disableTile() {
        newLabel = "Unavailable";
        newIcon = Icon.createWithResource(getApplicationContext(), R.mipmap.ic_balance_tile);
        newState = Tile.STATE_UNAVAILABLE;
        updateTile();
    }

    // Set Icon & Label
    private void setLabelIcon() {
        if (activeProfile == PROFILE_BALANCE) {
            newLabel = getResources().getString(R.string.balance);
            newIcon = Icon.createWithResource(getApplicationContext(), R.mipmap.ic_balance_tile);
        } else if (activeProfile == PROFILE_PERFORMANCE) {
            newLabel = getResources().getString(R.string.performance);
            newIcon = Icon.createWithResource(getApplicationContext(), R.mipmap.ic_performance_tile);
        } else if (activeProfile == PROFILE_GAMING) {
            newLabel = getResources().getString(R.string.gaming);
            newIcon = Icon.createWithResource(getApplicationContext(), R.mipmap.ic_gaming_tile);
        } else if (activeProfile == PROFILE_BATTERY) {
            newLabel = getResources().getString(R.string.battery);
            newIcon = Icon.createWithResource(getApplicationContext(), R.mipmap.ic_battery_tile);
        } else {
            activeProfile = PROFILE_EXTRA_BALANCE;
            newLabel = getResources().getString(R.string.extra_balance);
            newIcon = Icon.createWithResource(getApplicationContext(), R.mipmap.ic_ebalance_tile);
        }
        newState = Tile.STATE_ACTIVE;
        updateTile();
    }

    // Update Tile UI
    private void updateTile() {
        Tile tile = this.getQsTile();

        // Set The Tile Icon And Label
        tile.setLabel(newLabel);
        tile.setIcon(newIcon);
        tile.setState(newState);
        tile.updateTile();
    }
}