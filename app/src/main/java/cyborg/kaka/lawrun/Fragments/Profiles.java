package cyborg.kaka.lawrun.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.topjohnwu.superuser.ShellUtils;

import java.util.Objects;

import cyborg.kaka.lawrun.BuildConfig;
import cyborg.kaka.lawrun.R;
import cyborg.kaka.lawrun.Utility.Utils;
import cyborg.kaka.lawrun.databinding.FragmentProfilesBinding;

import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_BALANCE;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_BATTERY;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_EXTRA_BALANCE;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_GAMING;
import static cyborg.kaka.lawrun.Utility.Constants.PROFILE_PERFORMANCE;

public class Profiles extends Fragment {

    private FragmentProfilesBinding binding;

    // App Bar Views
    private ImageView tab_heart;
    private TextView tab_profiles, tab_color_holder;
    private GradientDrawable tab_indicator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilesBinding.inflate(inflater, container, false);

        // Ids' From Parent (MainScreen)
        LinearLayout mainContainter = Objects.requireNonNull(getActivity()).findViewById(R.id.mainContainter);

        // AppBar
        tab_heart = mainContainter.findViewById(R.id.tab_heart);

        // Tabs
        tab_profiles = mainContainter.findViewById(R.id.tab_profiles);
        tab_indicator = (GradientDrawable) mainContainter.findViewById(R.id.tab_indicator).getBackground();
        tab_color_holder = mainContainter.findViewById(R.id.tab_color_holder);

        // Init No Root Needed Things
        initInfoCard();

        // Check LawRun Support
        if (!ShellUtils.fastCmd("getprop lawrun.support").equals("1")) {
            disableProfileSreen();
            return binding.getRoot();
        }

        // Apply Colors
        resetColors();

        // OnClickListners
        binding.cardBattery.setOnClickListener(v -> setProfile(PROFILE_BATTERY));
        binding.cardBattery.setOnClickListener(v -> setProfile(PROFILE_BATTERY));
        binding.cardBalance.setOnClickListener(v -> setProfile(PROFILE_BALANCE));
        binding.cardEbalance.setOnClickListener(v -> setProfile(PROFILE_EXTRA_BALANCE));
        binding.cardPerformance.setOnClickListener(v -> setProfile(PROFILE_PERFORMANCE));
        binding.cardGaming.setOnClickListener(v -> setProfile(PROFILE_GAMING));

        // Show Layout
        return binding.getRoot();
    }

    // Set Info Card
    @SuppressLint("SetTextI18n")
    private void initInfoCard() {
        // Device Name
        if (Build.MODEL.length() < 20) {
            binding.tvDeviceName.setText(Build.BRAND + " " + Build.MODEL);
        } else {
            binding.tvDeviceName.setText(Build.MODEL);
        }

        // Kernel Name
        if (!ShellUtils.fastCmd("getprop lawrun.kernel.name").equals("")) {
            binding.tvKernelName.setText(ShellUtils.fastCmd("getprop lawrun.kernel.name"));
        } else {
            binding.tvKernelName.setVisibility(View.GONE);
            binding.tvInfoKernel.setVisibility(View.GONE);
            binding.deviderInfo1.setVisibility(View.GONE);
        }

        // Kernel Version
        binding.tvLkmVersion.setText(BuildConfig.VERSION_NAME);

        // Battery Temp
        Handler battryTempUpdater = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Intent intent = Objects.requireNonNull(getActivity()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                String batteryTemp = String.valueOf((float) Objects.requireNonNull(intent).getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10);
                binding.tvBatteryTemp.setText(batteryTemp.substring(0, batteryTemp.indexOf(".")) + "°C");
                battryTempUpdater.postDelayed(this, 1000);
            }
        };
        battryTempUpdater.post(run);
    }

    // Reset To Default Colors & Apply Color Of Selected Theme
    private void resetColors() {
        // Reset All Colors
        binding.cardBalance.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.primary_dark));
        binding.tvBattery.setTextColor(getActivity().getColor(R.color.battery_1800ma));
        binding.cardBattery.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark));
        binding.tvBalance.setTextColor(getActivity().getColor(R.color.balance_2300ma));
        binding.cardEbalance.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark));
        binding.tvEbalance.setTextColor(getActivity().getColor(R.color.e_balance_2800ma));
        binding.cardPerformance.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark));
        binding.tvPerformance.setTextColor(getActivity().getColor(R.color.performance_3300ma));
        binding.cardGaming.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark));
        binding.tvGaming.setTextColor(getActivity().getColor(R.color.gaming_3500ma));

        // Get Current Applied Theme Color
        String profile = ShellUtils.fastCmd("getprop persist.lawrun.profile");

        // Init Default Colors
        int theme_color = getActivity().getColor(R.color.e_balance_2800ma),
                default_dark_color = getActivity().getColor(R.color.primary_dark_black);

        // Check & Apply Colors
        if (profile.equals(String.valueOf(PROFILE_BATTERY))) {
            theme_color = getActivity().getColor(R.color.battery_1800ma);
            binding.cardBattery.setCardBackgroundColor(theme_color);
            binding.tvBattery.setTextColor(default_dark_color);
            tab_heart.setImageResource(R.mipmap.ic_heart_battery);
        } else if (profile.equals(String.valueOf(PROFILE_BALANCE))) {
            theme_color = getActivity().getColor(R.color.balance_2300ma);
            binding.cardBalance.setCardBackgroundColor(theme_color);
            binding.tvBalance.setTextColor(default_dark_color);
            tab_heart.setImageResource(R.mipmap.ic_heart_balance);
        } else if (profile.equals(String.valueOf(PROFILE_EXTRA_BALANCE))) {
            theme_color = getActivity().getColor(R.color.e_balance_2800ma);
            binding.cardEbalance.setCardBackgroundColor(theme_color);
            binding.tvEbalance.setTextColor(default_dark_color);
            tab_heart.setImageResource(R.mipmap.ic_heart_ebalance);
        } else if (profile.equals(String.valueOf(PROFILE_PERFORMANCE))) {
            theme_color = getActivity().getColor(R.color.performance_3300ma);
            binding.cardPerformance.setCardBackgroundColor(theme_color);
            binding.tvPerformance.setTextColor(default_dark_color);
            tab_heart.setImageResource(R.mipmap.ic_heart_performance);
        } else if (profile.equals(String.valueOf(PROFILE_GAMING))) {
            theme_color = getActivity().getColor(R.color.gaming_3500ma);
            binding.cardGaming.setCardBackgroundColor(theme_color);
            binding.tvGaming.setTextColor(default_dark_color);
            tab_heart.setImageResource(R.mipmap.ic_heart_gaming);
        }

        // Apply Info Text Colors
        binding.tvDeviceName.setTextColor(theme_color);
        binding.tvKernelName.setTextColor(theme_color);
        binding.tvLkmVersion.setTextColor(theme_color);
        binding.tvBatteryTemp.setTextColor(theme_color);

        // Apply Selected Tab Color
        tab_profiles.setTextColor(theme_color);
        tab_indicator.setColor(theme_color);

        // Color PlaceHolder For Other Views
        tab_color_holder.setTextColor(theme_color);

    }

    // lawRun Not Available
    private void disableProfileSreen() {
        binding.profiles.setVisibility(View.GONE);
        binding.profilesNotSupported.setVisibility(View.VISIBLE);
    }

    // Apply Profile
    private void setProfile(int profile) {
        if (!Utils.rootCheck(getActivity()))
            return;
        if (!ShellUtils.fastCmd("getprop persist.lawrun.profile").equals(String.valueOf(profile))) {
            ShellUtils.fastCmd("setprop persist.lawrun.profile " + profile);
            resetColors();
        }
    }

    // Refresh Fragment
    @Override
    public void onResume() {
        super.onResume();
        resetColors();
    }
}
