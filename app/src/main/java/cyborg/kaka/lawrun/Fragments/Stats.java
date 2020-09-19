package cyborg.kaka.lawrun.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import cyborg.kaka.lawrun.R;
import cyborg.kaka.lawrun.Services.FPS;
import cyborg.kaka.lawrun.databinding.FragmentStatsBinding;

public class Stats extends Fragment {

    private FragmentStatsBinding binding;
    private Activity mActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);

        // Get Context
        mActivity = getActivity();

        // Apply Theme Colors
        resetColors();

        // Get Running Services
        runningServices();

        // Battery Monitor Click
        binding.switchBatteryMonitor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(mActivity, "Currently in beta - Working day/night to add features (Click heart icon on top right corner)", Toast.LENGTH_LONG).show();
            binding.switchBatteryMonitor.setChecked(false);
        });

        // FPS On/Off Switch
        binding.switchFpsMeter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!Settings.canDrawOverlays(getActivity())) {
                    String packageName = mActivity.getPackageName();
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + packageName));
                    mActivity.startActivityForResult(intent, 1);
                    return;
                }
                Stats.this.mActivity.startForegroundService(new Intent(Stats.this.mActivity, FPS.class));
            } else {
                Stats.this.mActivity.stopService(new Intent(Stats.this.mActivity, FPS.class));
            }
        });

        return binding.getRoot();
    }

    // Reset Colors
    private void resetColors() {
        // GetTheme Color
        LinearLayout mainContainter = Objects.requireNonNull(getActivity()).findViewById(R.id.mainContainter);
        TextView color_holder = mainContainter.findViewById(R.id.tab_color_holder);
        int themeColor = color_holder.getTextColors().getDefaultColor();

        binding.tvFpsMeterTitle.setTextColor(themeColor);
        binding.tvBatteryMonitor.setTextColor(themeColor);
    }

    // Get Running Services
    private void runningServices() {
        // FPS Meter
        binding.switchFpsMeter.setChecked(FPS.serviceRunning);
    }

    // Refresh Fragment
    @Override
    public void onResume() {
        super.onResume();
        resetColors();
        runningServices();
    }

}
