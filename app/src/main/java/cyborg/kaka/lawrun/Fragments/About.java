package cyborg.kaka.lawrun.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import cyborg.kaka.lawrun.R;
import cyborg.kaka.lawrun.databinding.FragmentAboutBinding;

public class About extends Fragment {

    FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);

        // Kernel Telegram
        binding.cardContactNegrroo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=negrroo1"));
            startActivity(intent);
        });

        // App Telegram
        binding.cardContactCyborg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=CyborgKaka"));
            startActivity(intent);
        });

        // Negrroo Paypal
        binding.cardSupportNegrroo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lawrun-kernel.ml/2020/08/donation-link.html"));
            startActivity(intent);
        });

        // Cyborg Paypal
        binding.cardSupportCyborg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/SumanKhanra?locale.x=en_US"));
            startActivity(intent);
        });

        return binding.getRoot();
    }

    // Reset Colors
    private void resetColors() {
        // GetTheme Color
        LinearLayout mainContainter = Objects.requireNonNull(getActivity()).findViewById(R.id.mainContainter);
        TextView color_holder = mainContainter.findViewById(R.id.tab_color_holder);
        int themeColor = color_holder.getTextColors().getDefaultColor();

        binding.tvAboutUs.setTextColor(themeColor);
        binding.tvUpcoming.setTextColor(themeColor);
        binding.tvContactDevs.setTextColor(themeColor);
        binding.tvSupportUs.setTextColor(themeColor);
    }

    // Refresh Fragment
    @Override
    public void onResume() {
        super.onResume();
        resetColors();
    }
}
