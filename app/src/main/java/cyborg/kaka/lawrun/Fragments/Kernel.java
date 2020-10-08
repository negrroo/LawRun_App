package cyborg.kaka.lawrun.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.topjohnwu.superuser.ShellUtils;
import com.topjohnwu.superuser.io.SuFile;

import java.util.Objects;

import cyborg.kaka.lawrun.R;
import cyborg.kaka.lawrun.Utility.Utils;
import cyborg.kaka.lawrun.databinding.FragmentKernelBinding;

import static cyborg.kaka.lawrun.Utility.Constants.BATTERY_THERMAL_COOL_FILE;
import static cyborg.kaka.lawrun.Utility.Constants.BATTERY_THERMAL_WARM_FILE;
import static cyborg.kaka.lawrun.Utility.Constants.CHARGING_MAX_FILE;
import static cyborg.kaka.lawrun.Utility.Constants.PUBG_HDR_FILE;

public class Kernel extends Fragment {

    private FragmentKernelBinding kernel_fragment;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        kernel_fragment = FragmentKernelBinding.inflate(inflater, container, false);

        // Charging Amps Card OnClick
        kernel_fragment.card1800.setOnClickListener(v -> setChargingSpeed(1800));
        kernel_fragment.card2300.setOnClickListener(v -> setChargingSpeed(2300));
        kernel_fragment.card2800.setOnClickListener(v -> setChargingSpeed(2800));
        kernel_fragment.card3000.setOnClickListener(v -> setChargingSpeed(3000));
        kernel_fragment.card3300.setOnClickListener(v -> setChargingSpeed(3300));
        kernel_fragment.card3500.setOnClickListener(v -> setChargingSpeed(3500));

        // Battery Thermal Switch Listener
        kernel_fragment.switchBatteryThermal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Utils.rootCheck(getActivity())) {
                if (isChecked) {
                    ShellUtils.fastCmd("echo 450 > " + BATTERY_THERMAL_COOL_FILE);
                    ShellUtils.fastCmd("echo 490 > " + BATTERY_THERMAL_WARM_FILE);
                    return;
                }
                ShellUtils.fastCmd("echo 420 > " + BATTERY_THERMAL_COOL_FILE);
                ShellUtils.fastCmd("echo 450 > " + BATTERY_THERMAL_WARM_FILE);
            }
            getBatteryThermal();
        });

        // HDR Extreme Switch Listener
        kernel_fragment.switchHdrExtreme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Utils.rootCheck(getActivity())) {
                if (isChecked) {
                    ShellUtils.fastCmd("cp -a " + ShellUtils.fastCmd("getprop lawrun.hdr_extreme_path") + " " + PUBG_HDR_FILE);
                    return;
                }
                ShellUtils.fastCmd("rm " + PUBG_HDR_FILE);
            }
            getHDRExteme();
        });

        // Init Views & Colors
        resetColors();
        getAppliedAmps();
        getBatteryThermal();
        getHDRExteme();

        return kernel_fragment.getRoot();
    }

    private void setChargingSpeed(int amps) {
        if (!Utils.rootCheck(getActivity()))
            return;
        ShellUtils.fastCmd("chmod 755 " + CHARGING_MAX_FILE);
        ShellUtils.fastCmd("echo " + amps + "000 > " + CHARGING_MAX_FILE);
        ShellUtils.fastCmd("chmod 444 " + CHARGING_MAX_FILE);

        // Update Views
        resetColors();
        getAppliedAmps();
    }

    // Reset Colors
    private void resetColors() {
        // Card Default Colors
        kernel_fragment.card1800.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.primary_dark_black));
        kernel_fragment.card2300.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark_black));
        kernel_fragment.card2800.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark_black));
        kernel_fragment.card3000.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark_black));
        kernel_fragment.card3300.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark_black));
        kernel_fragment.card3500.setCardBackgroundColor(getActivity().getColor(R.color.primary_dark_black));

        kernel_fragment.tv1800.setTextColor(getActivity().getColor(R.color.battery_1800ma));
        kernel_fragment.tv2300.setTextColor(getActivity().getColor(R.color.balance_2300ma));
        kernel_fragment.tv2800.setTextColor(getActivity().getColor(R.color.e_balance_2800ma));
        kernel_fragment.tv3000.setTextColor(getActivity().getColor(R.color.charge_3000ma));
        kernel_fragment.tv3300.setTextColor(getActivity().getColor(R.color.performance_3300ma));
        kernel_fragment.tv3500.setTextColor(getActivity().getColor(R.color.gaming_3500ma));

        // GetTheme Color
        LinearLayout mainContainter = Objects.requireNonNull(getActivity()).findViewById(R.id.mainContainter);
        TextView color_holder = mainContainter.findViewById(R.id.tab_color_holder);
        int themeColor = color_holder.getTextColors().getDefaultColor();

        // Change Card Title Colors To Theme
        kernel_fragment.chargingSpeed.setTextColor(themeColor);
        kernel_fragment.tvBatteryThermalRemover.setTextColor(themeColor);
        kernel_fragment.tvHdrExtreme.setTextColor(themeColor);
    }

    // Initiate Current Amps Card
    private void getAppliedAmps() {
        if (!SuFile.open(CHARGING_MAX_FILE).exists() || Integer.parseInt(ShellUtils.fastCmd("cat " + CHARGING_MAX_FILE)) < 1800000) {
            kernel_fragment.cardChargingSpeed.setVisibility(View.GONE);
            return;
        }
        switch (ShellUtils.fastCmd("cat " + CHARGING_MAX_FILE)) {
            case "1800000":
                kernel_fragment.card1800.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.battery_1800ma));
                kernel_fragment.tv1800.setTextColor(getActivity().getColor(R.color.primary_dark));
                break;
            case "2300000":
                kernel_fragment.card2300.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.balance_2300ma));
                kernel_fragment.tv2300.setTextColor(getActivity().getColor(R.color.primary_dark));
                break;
            case "2800000":
                kernel_fragment.card2800.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.e_balance_2800ma));
                kernel_fragment.tv2800.setTextColor(getActivity().getColor(R.color.primary_dark));
                break;
            case "3000000":
                kernel_fragment.card3000.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.charge_3000ma));
                kernel_fragment.tv3000.setTextColor(getActivity().getColor(R.color.primary_dark));
                break;
            case "3300000":
                kernel_fragment.card3300.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.performance_3300ma));
                kernel_fragment.tv3300.setTextColor(getActivity().getColor(R.color.primary_dark));
                break;
            case "3500000":
                kernel_fragment.card3500.setCardBackgroundColor(Objects.requireNonNull(getActivity()).getColor(R.color.gaming_3500ma));
                kernel_fragment.tv3500.setTextColor(getActivity().getColor(R.color.primary_dark));
                break;
        }
    }

    // Initiate Battery Thermal Switch
    private void getBatteryThermal() {
        if (!SuFile.open(BATTERY_THERMAL_COOL_FILE).exists() || !SuFile.open(BATTERY_THERMAL_WARM_FILE).exists()) {
            kernel_fragment.cardBatteryThermal.setVisibility(View.GONE);
            return;
        }
        if (Integer.parseInt(ShellUtils.fastCmd("cat " + BATTERY_THERMAL_COOL_FILE)) > 420 || Integer.parseInt(ShellUtils.fastCmd("cat " + BATTERY_THERMAL_WARM_FILE)) > 450) {
            kernel_fragment.switchBatteryThermal.setChecked(true);
        }
    }

    // Initiate HDR Extreme Switch
    private void getHDRExteme() {
        if (ShellUtils.fastCmd("getprop lawrun.hdr_extreme_path").equals("")
                || !SuFile.open(ShellUtils.fastCmd("getprop lawrun.hdr_extreme_path")).exists()
                || !SuFile.open("/storage/emulated/0/Android/data/com.tencent.ig/").exists()
        ) {
            kernel_fragment.cardHdrExtreme.setVisibility(View.GONE);
            return;
        }
        if (SuFile.open(PUBG_HDR_FILE).exists()) {
            kernel_fragment.switchHdrExtreme.setChecked(true);
        }
    }

    // Refresh Fragment
    @Override
    public void onResume() {
        super.onResume();
        resetColors();
        getAppliedAmps();
        getBatteryThermal();
        getHDRExteme();
    }
}
